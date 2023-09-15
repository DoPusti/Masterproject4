package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.AttributeGroupedByName;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Mapper.RequirementTable;
import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;


@Component
@Data
@Builder
public class RessourceChecker {


    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker.class);
    List<AssuranceMapper> assuranceMap = new ArrayList<>();


    public void callRestService(List<Constraints> matchedAssurances) {
        matchedAssurances.forEach(assurance -> {
            if (!(assurance.getRestApi() == null)) {
                String restCall;
                restCall = assurance.getRestApi() + "/getPayload?AssetId=" + assurance.getIdShort();
                Log.info("RestCall " + restCall);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> call = restTemplate.getForEntity(restCall, String.class);
                Log.info(call.getBody());
            }
        });

    }

    public AttributeGroupedByName searchForGripper(AttributeGroupedByName attributeGroupedByNameIn, List<AssuranceMapper> assuranceMapperIn, Map<String, String> listOfRelevantParametersIn) {
        AttributeGroupedByName attributeGroupedByNameOut = new AttributeGroupedByName();
        return attributeGroupedByNameOut;
    }

    /***
     * Parsen einer Liste von Zusicherungen auf eine Liste von Zusicherungen im Map-Format
     * @param assuranceListIn   : Liste von Zusicherungen, die aus der Datenbank direkt kommen
     * @return assuranceListOut : Liste von Zusicherungen auf eine Map angepasst
     */
    public List<AssuranceMapper> fillAssuranceMapper(List<AssuranceFullObject> assuranceListIn) {
        List<AssuranceMapper> assuranceListOut = new ArrayList<>();

        for (AssuranceFullObject fullObject : assuranceListIn) {
            AssuranceMapper mapper = new AssuranceMapper();
            mapper.setAssetId(fullObject.getAssetId());
            mapper.setId((fullObject.getId()));
            mapper.setConnectionType(fullObject.getConnectionType());
            Map<String, PropertyInformation> propertyParameters = new TreeMap<>();
            // Verwende Reflection, um alle Double-Attribute zu extrahieren
            Field[] fields = fullObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == double.class) {
                    try {
                        double value = field.getDouble(fullObject);
                        PropertyInformation newPropertyInformationForAssuranceMapper = new PropertyInformation();
                        newPropertyInformationForAssuranceMapper.setValueOfParameter(value);
                        propertyParameters.put(field.getName(), newPropertyInformationForAssuranceMapper);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            mapper.setPropertyParameters(propertyParameters);
            assuranceListOut.add(mapper);
        }
        return assuranceListOut;
    }

    public void getRequirementSnakes(RequirementTable rootRequirement) {
        // 1. Anforderungsset rein (bereits sortiert absteigend)
        // 2. Greifersuche Loop
        // Stabilitätsprüfung bei jeder Schlange
        // Schlange neues Anforderungsset + Anforderung von Greifer (alle möglichen Schlangen) die wo am weitestens oben
        // x | y | z -> Suche Achse, die mindestens eine position ändert ( Anforderungen wieder addieren)
        // immer wenn die nächste Ressource nicht erfüllt -> Abbruch
        // -> solange bis alle Positionen geändert sind
        // -> Ergebnis: kombinierte Ressource, die Schlange erfüllt
        // Zu jeder Schlange komplementärset (tv drüber die nicht erfüllt sind) -> gleiches spiel nochmal
        System.out.println("<<Ermittlung aller kinematischen Schlangen>>");

        // Anforderungen prüfen und Liste holen
        Map<Integer, Map<String, PropertyInformation>> requirementSet = rootRequirement.getRequirementAttributes();
        Map<String, PropertyInformation> relevantAttributes = requirementSet.get(1);
        // Nicht relevante Zeilen löschen
        requirementSet.entrySet().removeIf(integerMapEntry -> integerMapEntry.getKey() > 3);
        // Passende Greifer suchen
        Map<AssuranceMapper, Map<String, PropertyInformation>> newRequirementSnake = checkForGripper(requirementSet, relevantAttributes);
        // TODO: Kraft in z muss angepasst werden
        // Prüfung ob Stabilität vorhanden ist
    }

    public Map<AssuranceMapper, Map<String, PropertyInformation>> checkForGripper(Map<Integer, Map<String, PropertyInformation>> requirementSetIn, Map<String, PropertyInformation> relevantAttributesIn) {
        Map<AssuranceMapper, Map<String, PropertyInformation>> cachedMatchedAssurances = new HashMap<>();
        // Jedes Attribut einer Zusicherung wird seperat durchsucht
        assuranceMap.forEach(assurance -> {
            boolean assuranceIsRelevant = true;
            Map<String, PropertyInformation> requirementSnake = new HashMap<>();
            // Betrachtung von Greifern
            if (assurance.getConnectionType().equals("AutomaticallyRemoveable")) {
                System.out.println("Betrachtung von Zusicherung " + assurance.getId());
                // Attribute der Zusicherung
                Map<String, PropertyInformation> propertiesOfAssurance = assurance.getPropertyParameters();
                // Durchsuchen jedes Attribut der Zusicherung
                loopForAttribute:
                for (Map.Entry<String, PropertyInformation> assuranceAttribute : propertiesOfAssurance.entrySet()) {
                    String attributeNameOfAssurance = assuranceAttribute.getKey(); // z.B. ForceX
                    // Nur wenn das Attribut gebraucht wird
                    if (relevantAttributesIn.containsKey(attributeNameOfAssurance)) {
                        if (relevantAttributesIn.get(attributeNameOfAssurance).getDataSpecification().equals("Constraints")) {
                            System.out.println("Prüfung des Attributs " + attributeNameOfAssurance + " mit dem Wert =" + assuranceAttribute.getValue().getValueOfParameter());
                            boolean relevantAttributeFound = false;
                            // Durchsuchen von jeder Zeile
                            loopForRows:
                            for (Map.Entry<Integer, Map<String, PropertyInformation>> outerEntry : requirementSetIn.entrySet()) {
                                int rowNumber = outerEntry.getKey();
                                Map<String, PropertyInformation> requirementAttribute = outerEntry.getValue();
                                System.out.println("Zeile : " + rowNumber + " wird betrachtet. Wert = " + requirementAttribute.get(attributeNameOfAssurance).getValueOfParameter());
                                if (assuranceAttribute.getValue().getValueOfParameter() >= requirementAttribute.get(attributeNameOfAssurance).getValueOfParameter()
                                        && relevantAttributesIn.containsKey(attributeNameOfAssurance)) {
                                    // Zeile wurde gefunden
                                    PropertyInformation informationForAssuranceProperty = requirementAttribute.get(attributeNameOfAssurance);
                                    informationForAssuranceProperty.setValency(rowNumber);
                                    requirementSnake.put(attributeNameOfAssurance, informationForAssuranceProperty);
                                    System.out.println("Wert ist größer -> wird eingetragen");
                                    relevantAttributeFound = true;
                                    break loopForRows;
                                }
                            }
                            // Es wurde nichts passendes gefunden -> kompletter Abbruch für diese Zusicherung
                            if (!relevantAttributeFound) {
                                System.out.println("Attribut ");
                                assuranceIsRelevant = false;
                                break loopForAttribute;
                            }
                        }
                    }
                }
                if (assuranceIsRelevant) {
                    cachedMatchedAssurances.put(assurance, requirementSnake);
                }
            }
        });
        System.out.println("Gefundene Greifer");
        for (Map.Entry<AssuranceMapper, Map<String, PropertyInformation>> entry : cachedMatchedAssurances.entrySet()) {
            AssuranceMapper assuranceMapper = entry.getKey();
            Map<String, PropertyInformation> propertyInformationMap = entry.getValue();
            System.out.println("ZusicherungsID : " + assuranceMapper.getId());
            for (Map.Entry<String, PropertyInformation> entryIn : propertyInformationMap.entrySet()) {
                System.out.println("Attributsname = " + entryIn.getKey() +
                        "/Wert = " + entryIn.getValue().getValueOfParameter() +
                        "/Position = " + entryIn.getValue().getValency() +
                        "/TV = " + entryIn.getValue().getSubProcessId());
            }
            // Dein Code hier, um mit assuranceMapper und propertyInformationMap zu arbeiten
        }

        return cachedMatchedAssurances;
    }


}
