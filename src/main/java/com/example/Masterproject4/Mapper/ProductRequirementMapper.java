package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.CombinedRessources.AttributeGroupedByName;
import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.XMLAttributeHolder.ProcessRequirement;
import com.example.Masterproject4.XMLAttributeHolder.ProductRequirementFullObject;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Data
@Component
public class ProductRequirementMapper {
    private static final Logger Log = LoggerFactory.getLogger(ProductRequirementMapper.class);

    ProductRequirementFullObject fullObjectProductRequirement = new ProductRequirementFullObject();
    Map<String, Map<String, Double>> productPropertiesOfParts = new HashMap<>();
    List<ProcessRequirement> processRequirements = new ArrayList<>();
    Map<String, String> listOfRelevantParameters;
    int idForProcessRequirements = 0;

    public static void addOrUpdateValue(Map<String, PropertyInformation> map, String key, double value, PropertyInformation newAttributeValueIn) {
        if (!map.containsKey(key) || map.get(key).getValueOfParameter() < value) {
            //System.out.println("Neues Attribut " + key + " hinzugefügt" );
            map.put(key, newAttributeValueIn);
        }
    }

    public ProductRequirementFullObject mapXMLToClass(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);
        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            String idShortOfSM = subModelObject.getIdShort();
            switch (idShortOfSM) {
                case "Identification" -> {
                    List<SubModelElement> subModelElements = subModelObject.getSubmodelElements().getSubmodelElement();
                    fillSubModelIdentification(subModelElements);
                }
                case "ProductProperty" -> {
                    List<SubModelElement> subModelElements = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElements.forEach(subElementsLevel1 -> {
                        List<SubModelElement> subModelElementsLevel2 = subElementsLevel1.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsLevel2.forEach(subModelElementsLevel3 -> {
                            // Jedes einzelne Element in Single Parts und Combined Parts
                            productPropertiesOfParts.put(subModelElementsLevel3.getSubmodelElementCollection().getIdShort(), fillSubModelProductProperty(subModelElementsLevel3.getSubmodelElementCollection()));
                        });
                    });
                }
                case "ProcessRequirement" -> {
                    List<SubModelElement> subModelElements = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElements.forEach(subElementsLevel1 -> {
                        ProcessRequirement newProcessRequirement = fillSubModelProcessRequirement(subElementsLevel1.getSubmodelElementCollection());
                        idForProcessRequirements++;
                        newProcessRequirement.setId(idForProcessRequirements);
                        processRequirements.add(newProcessRequirement);
                    });
                }
            }
        });
        fullObjectProductRequirement.setProductPropertiesOfParts(productPropertiesOfParts);
        fullObjectProductRequirement.setProcessRequirement(processRequirements);
        return fullObjectProductRequirement;
    }

    private void fillSubModelIdentification(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            if (propertyIdentification.getIdShort().equals("AssetId")) {
                fullObjectProductRequirement.setAssetId(propertyIdentification.getValue());
            }
        });

    }

    private Map<String, Double> fillSubModelProductProperty(SubModelElementCollection collection) {
        Map<String, Double> casheListForProperties = new HashMap<>();
        // IDShort z.B. Combined Parts
        List<SubModelElement> subModelElementsInProductPropertyDeep2 = collection.getValue().getSubmodelElement();
        subModelElementsInProductPropertyDeep2.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            if (property1 != null) {
                if ((property1.getIdShort().equals("mass") || property1.getIdShort().equals("MeanRoughness") || property1.getIdShort().equals("FerroMagentic"))
                        && listOfRelevantParameters.containsKey(property1.getIdShort())
                ) {
                    casheListForProperties.put(property1.getIdShort(), Double.parseDouble(property1.getValue()));
                }
            } else {
                List<SubModelElement> subModelElementsInProductPropertyDeep3 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementsInProductPropertyDeep3.forEach(subModelElementObject2 -> {
                    Property property2 = subModelElementObject2.getProperty();
                    if ((property2.getIdShort().equals("Length") ||
                            property2.getIdShort().equals("Width") ||
                            property2.getIdShort().equals("Height") ||
                            property2.getIdShort().equals("X") ||
                            property2.getIdShort().equals("Y") ||
                            property2.getIdShort().equals("Z")) && listOfRelevantParameters.containsKey(property2.getIdShort())) {
                        casheListForProperties.put(property2.getIdShort(), Double.parseDouble(property2.getValue()));
                    }
                });
            }
        });
        return casheListForProperties;
    }

    private ProcessRequirement fillSubModelProcessRequirement(SubModelElementCollection collection) {
        Map<String, PropertyInformation> attributeDefinitions = new HashMap<>();
        ProcessRequirement processRequirement = new ProcessRequirement();
        String idShortOfProcessRequirement = collection.getIdShort();
        List<SubModelElement> subModelElementsInElement = collection.getValue().getSubmodelElement();
        processRequirement.setTvName(idShortOfProcessRequirement);
        subModelElementsInElement.forEach(subModelElementDeep1 -> {
            Property property = subModelElementDeep1.getProperty();
            if (property != null) {
                if (property.getIdShort().equals("ReferenceParts")) {
                    processRequirement.setReferenceParts(property.getValue());
                }
                if (property.getIdShort().equals("Stability")) {
                    processRequirement.setStability(Boolean.parseBoolean(property.getValue()));
                }
            } else {

                List<SubModelElement> listOfElements = subModelElementDeep1.getSubmodelElementCollection().getValue().getSubmodelElement();
                listOfElements.forEach(subModelElementDeep2 -> {
                    List<SubModelElement> subModelElements3 = subModelElementDeep2.getSubmodelElementCollection().getValue().getSubmodelElement();
                    String dataSpecification = subModelElementDeep2.getSubmodelElementCollection().getIdShort();
                    if (subModelElements3 != null) {
                        subModelElements3.forEach(subModelElementDeep3 -> {
                            Property property2 = subModelElementDeep3.getProperty();
                            if (listOfRelevantParameters.containsKey(property2.getIdShort())) {
                                double propertyParsedToDouble = Double.parseDouble(property2.getValue());
                                PropertyInformation newAttributeValue = PropertyInformation.builder()
                                        .subProcessId(idShortOfProcessRequirement)
                                        .valueOfParameter(propertyParsedToDouble)
                                        .dataSpecification(dataSpecification)
                                        .build();
                                addOrUpdateValue(attributeDefinitions, property2.getIdShort(), propertyParsedToDouble, newAttributeValue);
                            }
                        });
                    }
                });
            }
        });
        processRequirement.setAttributeDefinitions(attributeDefinitions);
        for (PropertyInformation propertyInformation : processRequirement.getAttributeDefinitions().values()) {
            propertyInformation.setStabilityGiven(processRequirement.isStability());
        }
        return processRequirement;
    }

    public AttributeGroupedByName mapProductRequirementFullObjectToSequence(ProductRequirementFullObject productRequirementFullObjectIn) {
        Map<String, Map<String, Double>> productPropertiesOfParts = productRequirementFullObjectIn.getProductPropertiesOfParts();
        List<ProcessRequirement> processRequirement = productRequirementFullObjectIn.getProcessRequirement();
        Map<String, Map<String, PropertyInformation>> outerMap = new HashMap<>();
        // Map für Referenz von Parts und TV`s
        Map<String, PropertyInformation> partToTVReference = new HashMap<>();
        //AttributeGroupedByName nun befüllen mit ProcessRequirement
        for (ProcessRequirement process : processRequirement) {
            for (Map.Entry<String, PropertyInformation> entry : process.getAttributeDefinitions().entrySet()) {
                PropertyInformation newPropertyInformation = PropertyInformation.builder()
                        .stabilityGiven(entry.getValue().isStabilityGiven())
                        .valueOfParameter(entry.getValue().getValueOfParameter())
                        .subProcessId(entry.getValue().getSubProcessId())
                        .partReference(process.getReferenceParts())
                        .requirementFullFilled(false)
                        .dataSpecification("PersistentStateChange")
                        .build();
                if (outerMap.containsKey(entry.getKey())) {
                    // Einträge für diesen Key holen
                    Map<String, PropertyInformation> innerMap = outerMap.get(entry.getKey());
                    // Neuen Eintrag für den Key hinzufügen
                    innerMap.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                    // Eintrag für Referenztabelle erstellen
                    partToTVReference.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                } else {
                    Map<String, PropertyInformation> innerMap = new HashMap<>();
                    innerMap.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                    outerMap.put(entry.getKey(), innerMap);
                    partToTVReference.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                }
            }
        }
        //AttributeGroupedByName nun befüllen mit ProductProperties
        for (Map.Entry<String, Map<String, Double>> outerEntry : productPropertiesOfParts.entrySet()) {
            String partName = outerEntry.getKey();
            Map<String, Double> innerMapOfProductProperties = outerEntry.getValue();
            String keyOfMap = "";
            Boolean stabilityGiven = false;
            // gesuchten TV suchen
            for (Map.Entry<String, PropertyInformation> entry : partToTVReference.entrySet()) {
                if (entry.getValue().getPartReference().equals(partName)) {
                    keyOfMap = entry.getKey();
                    stabilityGiven = entry.getValue().isStabilityGiven();
                    break;
                }
            }
            // Nur wenn das Teil auch benötigt wird, wird es hinzugefügt
            if (!(keyOfMap.isBlank() || keyOfMap.isEmpty())) {
                // Alle Attribute der Map durchsuchen (Mass, Length, Height etc.)
                for (Map.Entry<String, Double> innerEntry : innerMapOfProductProperties.entrySet()) {
                    String propertyName = innerEntry.getKey();
                    Double propertyValue = innerEntry.getValue();
                    PropertyInformation newPropertyInformation = PropertyInformation.builder()
                            .stabilityGiven(stabilityGiven)
                            .valueOfParameter(propertyValue)
                            .subProcessId(keyOfMap)
                            .requirementFullFilled(false)
                            .build();
                    if (outerMap.containsKey(propertyName)) {
                        Map<String, PropertyInformation> innerMap = outerMap.get(propertyName);
                        innerMap.put(keyOfMap, newPropertyInformation);
                    } else {
                        Map<String, PropertyInformation> innerMap = new HashMap<>();
                        innerMap.put(keyOfMap, newPropertyInformation);
                        outerMap.put(propertyName, innerMap);
                    }
                }
            }
        }
        AttributeGroupedByName attributeGroupedByNameOut = AttributeGroupedByName.builder()
                .propertyParameters(outerMap)
                .build();

        return attributeGroupedByNameOut;
    }

    public void sortPropertiesInAscendingOrder(AttributeGroupedByName attributeGroupedByNameIn) {
        Map<String, Map<String, PropertyInformation>> outerMap = attributeGroupedByNameIn.getPropertyParameters();
        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : outerMap.entrySet()) {
            Map<String, PropertyInformation> innerMap = outerEntry.getValue();
            List<Map.Entry<String, PropertyInformation>> sortedEntries = new ArrayList<>(innerMap.entrySet());
            // Sortieren der Einträge absteigend anhand valueOfParameter
            sortedEntries.sort((entry1, entry2) ->
                    Double.compare(entry2.getValue().getValueOfParameter(), entry1.getValue().getValueOfParameter()));
            // Erstellen einer neuen LinkedHashMap mit sortierten Einträgen
            LinkedHashMap<String, PropertyInformation> sortedInnerMap = new LinkedHashMap<>();
            for (Map.Entry<String, PropertyInformation> sortedEntry : sortedEntries) {
                sortedInnerMap.put(sortedEntry.getKey(), sortedEntry.getValue());
            }
            // Ersetzen der ursprünglichen inneren Map durch die sortierte Map
            outerEntry.setValue(sortedInnerMap);
        }
    }

    public void mapToTableOfRequirement(AttributeGroupedByName attributeGroupedByNameIn, PropertyInformation[][] tableOfRequirement) {

        int counterForColumns = 0;

        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : attributeGroupedByNameIn.getPropertyParameters().entrySet()) {
            String outerKey = outerEntry.getKey();  // z.B. PositionX
            // Iteriere über die innere Map . Liste von einem Key z.B. positionX
            //System.out.print("Attribut " + outerKey + " wird geprüft.");
            // Schleife über alle Zeilen eines Attributs -> wird zu
            int counterForRows = 0;
            for (Map.Entry<String, PropertyInformation> innerEntry : outerEntry.getValue().entrySet()) {
                PropertyInformation newInformationForAttribute = innerEntry.getValue();
                //System.out.println("/Wert= " + newInformationForAttribute.getValueOfParameter());
                newInformationForAttribute.setNumberOfSequence(Character.getNumericValue(innerEntry.getKey().charAt(2)));
                newInformationForAttribute.setAttributeName(outerKey);
                newInformationForAttribute.setDataSpecification(listOfRelevantParameters.get(outerKey));

                tableOfRequirement[counterForRows][counterForColumns] = newInformationForAttribute;

                //System.out.println("Attribut " + outerKey + " mit Wert = " + innerEntry.getValue().getValueOfParameter() +
                //        " wird auf Zeile=" + counterForRows + " und Spalte= " + counterForColumns + " gesetzt.");

                counterForRows++;
            }
            counterForColumns++;

        }
        /*
        for (int row = 0; row < tableOfRequirement.length; row++) {
            // Zeilennummer am Anfang der Zeile anzeigen
            System.out.printf("Zeile %d: ", row);

            for (int col = 0; col < tableOfRequirement[row].length; col++) {
                PropertyInformation propertyInfo = tableOfRequirement[row][col];
                if (propertyInfo != null) {
                    // Informationen der PropertyInformation ausgeben
                    System.out.printf("%s/%d/%.2f",
                            propertyInfo.getAttributeName(), propertyInfo.getNumberOfSequence(), propertyInfo.getValueOfParameter()
                    );
                } else {
                    System.out.print("N/A"); // Wenn das Element null ist, "N/A" ausgeben
                }

                System.out.print("\t"); // Tabulator zur Trennung der Spalten
            }
            System.out.println(); // Zeilenumbruch am Ende der Zeile
        }
        */

    }

}


