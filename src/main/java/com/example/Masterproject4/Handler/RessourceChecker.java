package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.RequirementSequenceTree;
import com.example.Masterproject4.Entity.AssuranceFullObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Component
@Data
@Builder
public class RessourceChecker {

    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker.class);

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

    public RequirementSequenceTree searchForGripper(RequirementSequenceTree requirementSequenceTreeIn, List<AssuranceMapper> assuranceMapperIn, Map<String, String> listOfRelevantParametersIn) {
        RequirementSequenceTree requirementSequenceTreeOut = new RequirementSequenceTree();
        return requirementSequenceTreeOut;
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

    public void findKinematicChains(RequirementSequenceTree requirementSequenceTreeIn, List<AssuranceMapper> assuranceMapperIn, Map<String, String> listOfRelevantParametersIn) {

        System.out.println(requirementSequenceTreeIn);
        // 1. Schleife über die tabellerarischen Felder der Requirement
        checkRequirementTableForMatchingAssurance(requirementSequenceTreeIn.getPropertyParameters(), assuranceMapperIn, listOfRelevantParametersIn);

    }

    public void checkRequirementTableForMatchingAssurance(Map<String, Map<String, PropertyInformation>> propertyParametersIn,
                                                          List<AssuranceMapper> assuranceMapperIn,
                                                          Map<String, String> listOfRelevantParametersIn) {
        System.out.println("Prüfung der Tabelle: ");
        for (Map.Entry<String, Map<String, PropertyInformation>> entry : propertyParametersIn.entrySet()) {
            String outerKey = entry.getKey();
            Map<String, PropertyInformation> innerMap = entry.getValue();
            // Innere Schleife durchläuft die innere Map
            for (Map.Entry<String, PropertyInformation> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                if (listOfRelevantParametersIn.containsKey(outerKey)) {
                    PropertyInformation innerPropertyInformation = innerEntry.getValue();
                    // Hier kannst du auf die Werte zugreifen, z.B.:
                    System.out.println("Attribut | " + outerKey);
                    System.out.println("Innerer Schlüssel: " + innerKey);
                    System.out.println("Wert | " + innerPropertyInformation.getValueOfParameter());
                }

            }
            System.out.println("\n");
        }


    }

}
