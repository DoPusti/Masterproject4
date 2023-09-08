package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.CombinedRessources.RequirementSequence;
import com.example.Masterproject4.CombinedRessources.StateOfStability;
import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.XMLAttributeHolder.ProcessRequirement;
import com.example.Masterproject4.XMLAttributeHolder.ProductRequirementFullObject;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import com.example.Masterproject4.XMLAttributeHolder.RessourceHolder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class ProductRequirementMapper {

    ProductRequirementFullObject fullObjectProductRequirement = new ProductRequirementFullObject();
    //List<ProductProperty> productProperties = new ArrayList<>();
    Map<String, Map<String, Double>> productPropertiesOfParts = new HashMap<>();
    List<ProcessRequirement> processRequirements = new ArrayList<>();
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

    private Map<String, Double> fillSubModelProductProperty(SubmodelElementCollection collection) {
        Map<String, Double> casheListForProperties = new HashMap<>();
        // IDShort z.B. Combined Parts
        List<SubModelElement> subModelElementsInProductPropertyDeep2 = collection.getValue().getSubmodelElement();
        subModelElementsInProductPropertyDeep2.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            if (property1 != null) {
                if (property1.getIdShort().equals("Mass") || property1.getIdShort().equals("MeanRoughness") || property1.getIdShort().equals("FerroMagentic")) {
                    casheListForProperties.put(property1.getIdShort(), Double.parseDouble(property1.getValue()));

                }
            } else {
                List<SubModelElement> subModelElementsInProductPropertyDeep3 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementsInProductPropertyDeep3.forEach(subModelElementObject2 -> {
                    Property property2 = subModelElementObject2.getProperty();
                    if (property2.getIdShort().equals("Length") ||
                            property2.getIdShort().equals("Width") ||
                            property2.getIdShort().equals("Height") ||
                            property2.getIdShort().equals("X") ||
                            property2.getIdShort().equals("Y") ||
                            property2.getIdShort().equals("Z")) {
                        casheListForProperties.put(property2.getIdShort(), Double.parseDouble(property2.getValue()));
                    }
                });
            }
        });
        return casheListForProperties;
    }

    private ProcessRequirement fillSubModelProcessRequirement(SubmodelElementCollection collection) {
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
                    if (subModelElements3 != null) {
                        subModelElements3.forEach(subModelElementDeep3 -> {
                            Property property2 = subModelElementDeep3.getProperty();
                            double propertyParsedToDouble = Double.parseDouble(property2.getValue());
                            //System.out.println("TVID: " + idShortOfProcessRequirement + ", Elements " + property2.getIdShort() + "/" + propertyParsedToDouble);
                            PropertyInformation newAttributeValue = PropertyInformation.builder()
                                    .subProcessId(idShortOfProcessRequirement)
                                    .valueOfParameter(propertyParsedToDouble)
                                    .build();
                            addOrUpdateValue(attributeDefinitions, property2.getIdShort(), propertyParsedToDouble, newAttributeValue);

                        });
                    }
                });
            }

        });

        processRequirement.setAttributeDefinitions(attributeDefinitions);
        for (PropertyInformation propertyInformation : processRequirement.getAttributeDefinitions().values()) {
            propertyInformation.setStabilityGiven(processRequirement.isStability());
        }
        //System.out.println(processRequirement);
        return processRequirement;
    }

    /*
    public List<RessourceHolder> fillAndSortRequirementList(List<ProcessRequirement> processRequirementListIn,List<ProductProcessReference> productProcessReferenceIn) {

        List<RessourceHolder> newRessourceHolderList = new ArrayList<>();

        List<List<Map<String, PropertyInformation>>> parameter;


        Map<Integer, Double> unsortedMapPositionX = new HashMap<>();
        Map<Integer, Double> unsortedMapPositionY = new HashMap<>();
        Map<Integer, Double> unsortedMapPositionZ = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationX = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationY = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationZ = new HashMap<>();
        Map<Integer, Double> unsortedMapForceX = new HashMap<>();
        Map<Integer, Double> unsortedMapForceY = new HashMap<>();
        Map<Integer, Double> unsortedMapForceZ = new HashMap<>();
        Map<Integer, Double> unsortedMapTorqueX = new HashMap<>();
        Map<Integer, Double> unsortedMapTorqueY = new HashMap<>();
        Map<Integer, Double> unsortedMapTorqueZ = new HashMap<>();
        Map<Integer, Double> unsortedMapPositionRepetitionAccuracyX = new HashMap<>();
        Map<Integer, Double> unsortedMapPositionRepetitionAccuracyY = new HashMap<>();
        Map<Integer, Double> unsortedMapPositionRepetitionAccuracyZ = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationRepetitionAccuracyX = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationRepetitionAccuracyY = new HashMap<>();
        Map<Integer, Double> unsortedMapRotationRepetitionAccuracyZ = new HashMap<>();

        processRequirementListIn.forEach(processRequirement -> {
            unsortedMapRotationX.put(processRequirement.getId(), max(processRequirement.getRotationXRsC(), processRequirement.getRotationXSsC()));
            unsortedMapRotationY.put(processRequirement.getId(), max(processRequirement.getRotationYRsC(), processRequirement.getRotationYSsC()));
            unsortedMapRotationZ.put(processRequirement.getId(), max(processRequirement.getRotationZRsC(), processRequirement.getRotationZSsC()));
            unsortedMapPositionX.put(processRequirement.getId(), max(processRequirement.getPositionXRsC(), processRequirement.getPositionXSsC()));
            unsortedMapPositionY.put(processRequirement.getId(), max(processRequirement.getPositionYRsC(), processRequirement.getPositionYSsC()));
            unsortedMapPositionZ.put(processRequirement.getId(), max(processRequirement.getPositionZRsC(), processRequirement.getPositionZSsC()));
            unsortedMapForceX.put(processRequirement.getId(), max(processRequirement.getForceXRsC(), processRequirement.getForceXSsC()));
            unsortedMapForceY.put(processRequirement.getId(), max(processRequirement.getForceYRsC(), processRequirement.getForceYSsC()));
            unsortedMapForceZ.put(processRequirement.getId(), max(processRequirement.getForceZRsC(), processRequirement.getForceZSsC()));
            unsortedMapTorqueX.put(processRequirement.getId(), max(processRequirement.getTorqueXRsC(), processRequirement.getTorqueXSsC()));
            unsortedMapTorqueY.put(processRequirement.getId(), max(processRequirement.getTorqueYRsC(), processRequirement.getTorqueYSsC()));
            unsortedMapTorqueZ.put(processRequirement.getId(), max(processRequirement.getTorqueZRsC(), processRequirement.getTorqueZSsC()));
            unsortedMapPositionRepetitionAccuracyX.put(processRequirement.getId(), max(processRequirement.getPositionRepetitionAccuracyXRsC(), processRequirement.getPositionRepetitionAccuracyXSsC()));
            unsortedMapPositionRepetitionAccuracyY.put(processRequirement.getId(), max(processRequirement.getPositionRepetitionAccuracyYRsC(), processRequirement.getPositionRepetitionAccuracyYSsC()));
            unsortedMapPositionRepetitionAccuracyZ.put(processRequirement.getId(), max(processRequirement.getPositionRepetitionAccuracyZRsC(), processRequirement.getPositionRepetitionAccuracyZSsC()));
            unsortedMapRotationRepetitionAccuracyX.put(processRequirement.getId(), max(processRequirement.getRotationRepetitionAccuracyXRsC(), processRequirement.getRotationRepetitionAccuracyXSsC()));
            unsortedMapRotationRepetitionAccuracyY.put(processRequirement.getId(), max(processRequirement.getRotationRepetitionAccuracyYRsC(), processRequirement.getRotationRepetitionAccuracyYSsC()));
            unsortedMapRotationRepetitionAccuracyZ.put(processRequirement.getId(), max(processRequirement.getRotationRepetitionAccuracyZRsC(), processRequirement.getRotationRepetitionAccuracyZSsC()));


        });
        List<Map.Entry<Integer, Double>> sortedListRotationX = new ArrayList<>(unsortedMapRotationX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationX);

        List<Map.Entry<Integer, Double>> sortedListRotationY = new ArrayList<>(unsortedMapRotationY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationY);

        List<Map.Entry<Integer, Double>> sortedListRotationZ = new ArrayList<>(unsortedMapRotationZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationZ);

        List<Map.Entry<Integer, Double>> sortedListPositionX = new ArrayList<>(unsortedMapPositionX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionX);

        List<Map.Entry<Integer, Double>> sortedListPositionY = new ArrayList<>(unsortedMapPositionY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionY);

        List<Map.Entry<Integer, Double>> sortedListPositionZ = new ArrayList<>(unsortedMapPositionZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionZ);

        List<Map.Entry<Integer, Double>> sortedListForceX = new ArrayList<>(unsortedMapForceX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceX);

        List<Map.Entry<Integer, Double>> sortedListForceY = new ArrayList<>(unsortedMapForceY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceY);

        List<Map.Entry<Integer, Double>> sortedListForceZ = new ArrayList<>(unsortedMapForceZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceZ);

        List<Map.Entry<Integer, Double>> sortedListTorqueX = new ArrayList<>(unsortedMapTorqueX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueX);

        List<Map.Entry<Integer, Double>> sortedListTorqueY = new ArrayList<>(unsortedMapTorqueY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueY);

        List<Map.Entry<Integer, Double>> sortedListTorqueZ = new ArrayList<>(unsortedMapTorqueZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueZ);

        List<Map.Entry<Integer, Double>> sortedListPositionRepetitionAccuracyX = new ArrayList<>(unsortedMapPositionRepetitionAccuracyX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyX);

        List<Map.Entry<Integer, Double>> sortedListPositionRepetitionAccuracyY = new ArrayList<>(unsortedMapPositionRepetitionAccuracyY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyY);

        List<Map.Entry<Integer, Double>> sortedListPositionRepetitionAccuracyZ = new ArrayList<>(unsortedMapPositionRepetitionAccuracyZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyZ);

        List<Map.Entry<Integer, Double>> sortedListRotationRepetitionAccuracyX = new ArrayList<>(unsortedMapRotationRepetitionAccuracyX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyX);

        List<Map.Entry<Integer, Double>> sortedListRotationRepetitionAccuracyY = new ArrayList<>(unsortedMapRotationRepetitionAccuracyY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyY);

        List<Map.Entry<Integer, Double>> sortedListRotationRepetitionAccuracyZ = new ArrayList<>(unsortedMapRotationRepetitionAccuracyZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyZ);


        for (int i = 0; i < sortedListForceX.size(); i++) {
            List<String> tvList = new ArrayList<>();
            RessourceHolder newRessource = RessourceHolder.builder()
                    .idShort(i)
                    .positionX(new AbstractMap.SimpleEntry<>(sortedListPositionX.get(i).getKey(), sortedListPositionX.get(i).getValue()))
                    .positionY(new AbstractMap.SimpleEntry<>(sortedListPositionY.get(i).getKey(), sortedListPositionY.get(i).getValue()))
                    .positionZ(new AbstractMap.SimpleEntry<>(sortedListPositionZ.get(i).getKey(), sortedListPositionZ.get(i).getValue()))
                    .rotationX(new AbstractMap.SimpleEntry<>(sortedListRotationX.get(i).getKey(), sortedListRotationX.get(i).getValue()))
                    .rotationY(new AbstractMap.SimpleEntry<>(sortedListRotationY.get(i).getKey(), sortedListRotationY.get(i).getValue()))
                    .rotationZ(new AbstractMap.SimpleEntry<>(sortedListRotationZ.get(i).getKey(), sortedListRotationZ.get(i).getValue()))
                    .forceX(new AbstractMap.SimpleEntry<>(sortedListForceX.get(i).getKey(), sortedListForceX.get(i).getValue()))
                    .forceY(new AbstractMap.SimpleEntry<>(sortedListForceY.get(i).getKey(), sortedListForceY.get(i).getValue()))
                    .forceZ(new AbstractMap.SimpleEntry<>(sortedListForceZ.get(i).getKey(), sortedListForceZ.get(i).getValue()))
                    .torqueX(new AbstractMap.SimpleEntry<>(sortedListTorqueX.get(i).getKey(), sortedListTorqueX.get(i).getValue()))
                    .torqueY(new AbstractMap.SimpleEntry<>(sortedListTorqueY.get(i).getKey(), sortedListTorqueY.get(i).getValue()))
                    .torqueZ(new AbstractMap.SimpleEntry<>(sortedListTorqueZ.get(i).getKey(), sortedListTorqueZ.get(i).getValue()))
                    .mass(0)
                    .meanRoughness(0)
                    .ferroMagnetic(true)
                    .height(0)
                    .width(0)
                    .length(0)
                    .centerOfMassX(0)
                    .centerOfMassY(0)
                    .centerOfMassZ(0)
                    .positionRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyX.get(i).getKey(), sortedListPositionRepetitionAccuracyX.get(i).getValue()))
                    .positionRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyY.get(i).getKey(), sortedListPositionRepetitionAccuracyY.get(i).getValue()))
                    .positionRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyZ.get(i).getKey(), sortedListPositionRepetitionAccuracyZ.get(i).getValue()))
                    .rotationRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyX.get(i).getKey(), sortedListRotationRepetitionAccuracyX.get(i).getValue()))
                    .rotationRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyY.get(i).getKey(), sortedListRotationRepetitionAccuracyY.get(i).getValue()))
                    .rotationRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyZ.get(i).getKey(), sortedListRotationRepetitionAccuracyZ.get(i).getValue()))
                    .tvList(tvList)
                    .build();

            newRessourceHolderList.add(newRessource);
        }

        // Jetzt noch die restlichen Werte befüllen. Geht erst jetzt, da die Sequenz der Teilvorgänge bekannt ist
        newRessourceHolderList.forEach(ressourceHolderInList -> {
            List<String> tvList = ressourceHolderInList.getTvList();
            tvList.forEach(tv -> {
                productProcessReferenceIn.forEach(productProcessReferenceInList -> {
                    if (productProcessReferenceInList.getTvName().equals(tv)) {
                        ressourceHolderInList.setMass(max(ressourceHolderInList.getMass(),productProcessReferenceInList.getMass()));
                        ressourceHolderInList.setMass(max(ressourceHolderInList.getMeanRoughness(),productProcessReferenceInList.getMeanRoughness()));
                        ressourceHolderInList.setHeight(max(ressourceHolderInList.getHeight(),productProcessReferenceInList.getHeight()));
                        ressourceHolderInList.setWidth(max(ressourceHolderInList.getWidth(),productProcessReferenceInList.getWidth()));
                        ressourceHolderInList.setLength(max(ressourceHolderInList.getLength(),productProcessReferenceInList.getLength()));
                        ressourceHolderInList.setCenterOfMassX(max(ressourceHolderInList.getCenterOfMassX(),productProcessReferenceInList.getCenterOfMassX()));
                        ressourceHolderInList.setCenterOfMassY(max(ressourceHolderInList.getCenterOfMassY(),productProcessReferenceInList.getCenterOfMassY()));
                        ressourceHolderInList.setCenterOfMassZ(max(ressourceHolderInList.getCenterOfMassZ(),productProcessReferenceInList.getCenterOfMassZ()));
                        if(!productProcessReferenceInList.isFerroMagnetic()) {
                            productProcessReferenceInList.setFerroMagnetic(false);
                        }
                    }
                });

            });

        });


        return newRessourceHolderList;
    }

     */
    public RequirementSequence mapProductRequirementFullObjectToSequence(ProductRequirementFullObject productRequirementFullObjectIn) {

        Map<String, Map<String, Double>> productPropertiesOfParts = productRequirementFullObjectIn.getProductPropertiesOfParts();
        List<ProcessRequirement> processRequirement = productRequirementFullObjectIn.getProcessRequirement();
        Map<String, Map<String, PropertyInformation>> outerMap = new HashMap<>();
        // Map für Referenz von Parts und TV`s
        Map<String, PropertyInformation> partToTVReference = new HashMap<>();

        //RequirementSequence nun befüllen mit ProcessRequirement
        for (ProcessRequirement process : processRequirement) {
            for (Map.Entry<String, PropertyInformation> entry : process.getAttributeDefinitions().entrySet()) {
                PropertyInformation newPropertyInformation = PropertyInformation.builder()
                        .stabilityGiven(entry.getValue().isStabilityGiven())
                        .valueOfParameter(entry.getValue().getValueOfParameter())
                        .subProcessId(entry.getValue().getSubProcessId())
                        .partReference(process.getReferenceParts())
                        .requirementFullFilled(false)
                        .build();
                if (outerMap.containsKey(entry.getKey())) {
                    //System.out.println("Attribut " + entry.getKey() + " bereits vorhanden. Eintrag wird hinzugefügt.");
                    // Einträge für diesen Key holen
                    Map<String, PropertyInformation> innerMap = outerMap.get(entry.getKey());
                    // Neuen Eintrag für den Key hinzufügen
                    innerMap.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                    // Eintrag für Referenztabelle erstellen
                    partToTVReference.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                } else {
                    //System.out.println("Attribut " + entry.getKey() + " noch nicht vorhanden.");
                    Map<String, PropertyInformation> innerMap = new HashMap<>();
                    innerMap.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                    outerMap.put(entry.getKey(), innerMap);
                    partToTVReference.put(entry.getValue().getSubProcessId(), newPropertyInformation);
                }
            }
        }
        //System.out.println("Liste von PartTVReference wurde vollständig befüllt.");
        /*
        for (Map.Entry<String, PropertyInformation> entry : partToTVReference.entrySet()) {
            String key = entry.getKey();
            PropertyInformation value = entry.getValue();
            System.out.println("Eintrag mit Key: " + key + " und Wert " + value);
        }

         */


        //System.out.println("OuterMap nun mit ProcessRequirement befüllt.");
        /*
        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : outerMap.entrySet()) {
            String outerKey = outerEntry.getKey();
            Map<String, PropertyInformation> innerMap = outerEntry.getValue();
            System.out.println("Äußerer Schlüssel: " + outerKey);

            // Jetzt können wir die innere Map durchlaufen
            for (Map.Entry<String, PropertyInformation> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                PropertyInformation propertyInfo = innerEntry.getValue();
                System.out.println("Innerer Schlüssel: " + innerKey);
                System.out.println("Property Information: " + propertyInfo.toString());
            }
        }

         */




        //RequirementSequence nun befüllen mit ProductProperties
        for (Map.Entry<String, Map<String, Double>> outerEntry : productPropertiesOfParts.entrySet()) {
            String partName = outerEntry.getKey();
            Map<String, Double> innerMapOfProductProperties = outerEntry.getValue();

            //System.out.println("Name des Produkts: " + partName);
            String keyOfMap = "";
            Boolean stabilityGiven = false;

            // gesuchten TV suchen
            for (Map.Entry<String, PropertyInformation> entry : partToTVReference.entrySet()) {
                //System.out.println("Vergleich " + entry.getValue().getPartReference() + " mit  " + partName);
                if (entry.getValue().getPartReference().equals(partName)) {
                    keyOfMap = entry.getKey();
                    stabilityGiven = entry.getValue().isStabilityGiven();
                    //System.out.println("Key gefunden"); // entspricht dem Teilvorgang
                    break;
                }
            }
            // Nur wenn das Teil auch benötigt wird, wird es hinzugefügt
            if(!(keyOfMap.isBlank() || keyOfMap.isEmpty())) {
                // Alle Attribute der Map durchsuchen (Mass, Length, Height etc.)
                for (Map.Entry<String, Double> innerEntry : innerMapOfProductProperties.entrySet()) {
                    String propertyName = innerEntry.getKey();
                    Double propertyValue = innerEntry.getValue();
                    //System.out.println("Property Name: " + propertyName + ", Property Value: " + propertyValue);
                    PropertyInformation newPropertyInformation = PropertyInformation.builder()
                            .stabilityGiven(stabilityGiven)
                            .valueOfParameter(propertyValue)
                            .subProcessId(keyOfMap)
                            .requirementFullFilled(false)
                            .build();
                    if (outerMap.containsKey(propertyName)) {
                        //System.out.println("Property Name: " + propertyName + "bereits vorhanden.");
                        Map<String, PropertyInformation> innerMap = outerMap.get(propertyName);
                        //System.out.println("Eintrag wird hinzugefügt mit TV: " + keyOfMap + " und Information " + newPropertyInformation  );
                        innerMap.put(keyOfMap, newPropertyInformation);
                    } else {
                        Map<String, PropertyInformation> innerMap = new HashMap<>();
                        //System.out.println("Property Name: " + propertyName + " noch nicht vorhanden.");
                        innerMap.put(keyOfMap, newPropertyInformation);
                        //System.out.println("Eintrag wird hinzugefügt mit TV: " + keyOfMap + " und Information " + newPropertyInformation  );
                        outerMap.put(propertyName, innerMap);
                    }

                }

            }


        }
        // Überprüfen des Ergebnisses
        System.out.println("OuterMap nun mit ProductRequirement befüllt.");
        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : outerMap.entrySet()) {
            String outerKey = outerEntry.getKey();
            Map<String, PropertyInformation> innerMap = outerEntry.getValue();
            System.out.println("\n" + "Äußerer Schlüssel: " + outerKey);
            // Jetzt können wir die innere Map durchlaufen
            for (Map.Entry<String, PropertyInformation> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                PropertyInformation propertyInfo = innerEntry.getValue();
                System.out.println("Innerer Schlüssel: " + innerKey);
                System.out.println("Property Information: " + propertyInfo.toString());
            }
        }
        RequirementSequence requirementSequenceOut = RequirementSequence.builder()
                .parameter(outerMap)
                .build();

        return requirementSequenceOut;
    }
    public void sortPropertiesInAscendingOrder(RequirementSequence requirementSequenceIn) {

    }





    /*
    public List<RequirementSequence> getAllSequencesOfRequirements(ProductRequirementFullObject productRequirementFullObjectIn,
                                                                   Map<String, String> listOfRelevantParametersIn) throws IllegalAccessException {

        List<RequirementSequence> requirementSequencesOut = new ArrayList<>();
        List<ProductProperty> productProperty = productRequirementFullObjectIn.getProductProperty();
        List<ProcessRequirement> processRequirement = productRequirementFullObjectIn.getProcessRequirement();

        Map<String, List<PropertyInformation>> parametersForGivenAttributes = new HashMap<>();

        for (ProcessRequirement process : processRequirement) {
            //System.out.println("TV-Vorgang " + process.getTvName());
            Class<?> clazz = process.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (listOfRelevantParametersIn.containsKey(fieldName)) {
                    field.setAccessible(true);
                    double valueOfRessourceAttribute = (double) field.get(process);
                    //System.out.println("Attributsname " + fieldName);
                    //System.out.println("Wert " + valueOfRessourceAttribute);
                }
            }

        }


        return requirementSequencesOut;
    }

     */


}


