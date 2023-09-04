package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.ProduktAnforderung.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class ProductRequirementMapper {

    ProductRequirementFullObject fullObjectProductRequirement = new ProductRequirementFullObject();
    List<ProductProperty> productProperties = new ArrayList<>();
    List<ProcessRequirement> processRequirements = new ArrayList<>();
    int idForProcessRequirements = 0;

    private static void printEntry(String description, AbstractMap.SimpleEntry<String, Double> entry) {
        System.out.println(description + ": " + entry.getKey() + " - " + entry.getValue());
    }

    public static void addOrUpdateValue(Map<String, AttributeToValue> map, String key, double value, AttributeToValue newAttributeValueIn) {
        if (!map.containsKey(key) || map.get(key).getValueOfParameter() < value) {
            //System.out.println("Neues Attribut " + key + " hinzugefügt" );
            map.put(key, newAttributeValueIn);
        }
    }

    public ProductRequirementFullObject mapXMLToClass(File file) throws JAXBException {
        //File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi3.xml");
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
                            ProductProperty newProductProperty = fillSubModelProductProperty(subModelElementsLevel3.getSubmodelElementCollection());
                            // z.B. Single Parts und Combined Parts
                            newProductProperty.setTyp(subElementsLevel1.getSubmodelElementCollection().getIdShort());
                            productProperties.add(newProductProperty);
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
        fullObjectProductRequirement.setProductProperty(productProperties);
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

    private ProductProperty fillSubModelProductProperty(SubmodelElementCollection collection) {
        ProductProperty newProductProperty = new ProductProperty();
        // IDShort z.B. Combined Parts
        newProductProperty.setIdShort(collection.getIdShort());
        List<SubModelElement> subModelElementsInProductPropertyDeep2 = collection.getValue().getSubmodelElement();
        subModelElementsInProductPropertyDeep2.forEach(subModelElementObject1 -> {
            // Property ausprobieren
            //productProperties
            Property property1 = subModelElementObject1.getProperty();
            if (property1 != null) {
                switch (property1.getIdShort()) {
                    case "Mass" -> newProductProperty.setMass(Double.parseDouble(property1.getValue()));
                    case "MeanRoughness" ->
                            newProductProperty.setMeanRoughness(Double.parseDouble(property1.getValue()));
                    case "FerroMagnetic" ->
                            newProductProperty.setFerroMagnetic(Boolean.parseBoolean(property1.getValue()));
                }
            } else {

                List<SubModelElement> subModelElementsInProductPropertyDeep3 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementsInProductPropertyDeep3.forEach(subModelElementObject2 -> {
                    Property property2 = subModelElementObject2.getProperty();
                    switch (property2.getIdShort()) {
                        case "Length" -> newProductProperty.setLength(Double.parseDouble(property2.getValue()));
                        case "Width" -> newProductProperty.setWidth(Double.parseDouble(property2.getValue()));
                        case "Height" -> newProductProperty.setHeight(Double.parseDouble(property2.getValue()));
                        case "X" -> newProductProperty.setCenterOfMassX(Double.parseDouble(property2.getValue()));
                        case "Y" -> newProductProperty.setCenterOfMassY(Double.parseDouble(property2.getValue()));
                        case "Z" -> newProductProperty.setCenterOfMassZ(Double.parseDouble(property2.getValue()));
                    }
                });
            }

        });


        //System.out.println("Fertiges Produkt");
        //System.out.println(newProductProperty);
        return newProductProperty;
    }

    private ProcessRequirement fillSubModelProcessRequirement(SubmodelElementCollection collection) {
        Map<String, AttributeToValue> attributeDefinitions = new HashMap<>();

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
                            System.out.println("TVID: " + idShortOfProcessRequirement + ", Elements " + property2.getIdShort() + "/" + propertyParsedToDouble);
                            AttributeToValue newAttributeValue = AttributeToValue.builder()
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
        for (AttributeToValue attributeToValue : processRequirement.getAttributeDefinitions().values()) {
            attributeToValue.setStabilityGiven(processRequirement.isStability());
        }
        System.out.println(processRequirement);
        return processRequirement;
    }

    /*
    public List<RessourceHolder> fillAndSortRequirementList(List<ProcessRequirement> processRequirementListIn,List<ProductProcessReference> productProcessReferenceIn) {

        List<RessourceHolder> newRessourceHolderList = new ArrayList<>();

        List<List<Map<String, AttributeToValue>>> parameter;


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

    public List<ProductProcessReference> getAllProductProcessReference(ProductRequirementFullObject productRequirementFullObjectIn) {
        List<ProductProcessReference> productProcessReferenceOut = new ArrayList<>();

        List<ProductProperty> productProperty = productRequirementFullObjectIn.getProductProperty();
        List<ProcessRequirement> processRequirement = productRequirementFullObjectIn.getProcessRequirement();

        //System.out.println("Größe : " + processRequirement.size());
        processRequirement.forEach(process -> {
            productProperty.forEach(product -> {
                if (process.getReferenceParts().equals(product.getIdShort())) {
                    productProcessReferenceOut.add(ProductProcessReference.builder()
                            .tvName(process.getTvName())
                            .partName(product.getIdShort())
                            .mass(product.getMass())
                            .meanRoughness(product.getMeanRoughness())
                            .ferroMagnetic(product.isFerroMagnetic())
                            .length(product.getLength())
                            .width(product.getWidth())
                            .height(product.getHeight())
                            .centerOfMassX(product.getCenterOfMassX())
                            .centerOfMassY(product.getCenterOfMassY())
                            .centerOfMassZ(product.getCenterOfMassZ())
                            .build());
                }

            });
        });


        return productProcessReferenceOut;
    }

    public void setNewProperties(List<RessourceHolder> ressourceHolderListIn) {

        ressourceHolderListIn.forEach(ressourceHolder -> {
            if (!(ressourceHolder.getGripper() == null)) {
                ressourceHolder.setMass(ressourceHolder.getMass() + ressourceHolder.getGripper().getMass());
                ressourceHolder.setLength(ressourceHolder.getLength() + ressourceHolder.getGripper().getLength());
                ressourceHolder.setWidth(ressourceHolder.getWidth() + ressourceHolder.getGripper().getWidth());
                ressourceHolder.setHeight(ressourceHolder.getHeight() + ressourceHolder.getGripper().getHeight());
                ressourceHolder.setCenterOfMassX(ressourceHolder.getCenterOfMassX() + ressourceHolder.getGripper().getCenterOfMassX());
                ressourceHolder.setCenterOfMassY(ressourceHolder.getCenterOfMassY() + ressourceHolder.getGripper().getCenterOfMassY());
                ressourceHolder.setCenterOfMassZ(ressourceHolder.getCenterOfMassZ() + ressourceHolder.getGripper().getCenterOfMassZ());
                ressourceHolder.setPositionX(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionX().getKey(), ressourceHolder.getPositionX().getValue() + ressourceHolder.getGripper().getPositionX()));
                ressourceHolder.setPositionY(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionY().getKey(), ressourceHolder.getPositionY().getValue() + ressourceHolder.getGripper().getPositionY()));
                ressourceHolder.setPositionZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionZ().getKey(), ressourceHolder.getPositionZ().getValue() + ressourceHolder.getGripper().getPositionZ()));
                ressourceHolder.setRotationX(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationX().getKey(), ressourceHolder.getRotationX().getValue() + ressourceHolder.getGripper().getRotationX()));
                ressourceHolder.setRotationY(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationY().getKey(), ressourceHolder.getRotationY().getValue() + ressourceHolder.getGripper().getRotationY()));
                ressourceHolder.setRotationZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationZ().getKey(), ressourceHolder.getRotationZ().getValue() + ressourceHolder.getGripper().getRotationZ()));
                ressourceHolder.setForceX(new AbstractMap.SimpleEntry<>(ressourceHolder.getForceX().getKey(), ressourceHolder.getForceX().getValue() + ressourceHolder.getGripper().getForceX()));
                ressourceHolder.setForceY(new AbstractMap.SimpleEntry<>(ressourceHolder.getForceY().getKey(), ressourceHolder.getForceY().getValue() + ressourceHolder.getGripper().getForceY()));
                ressourceHolder.setForceZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getForceZ().getKey(), ressourceHolder.getForceZ().getValue() + ressourceHolder.getGripper().getForceZ()));
                ressourceHolder.setTorqueX(new AbstractMap.SimpleEntry<>(ressourceHolder.getTorqueX().getKey(), ressourceHolder.getTorqueX().getValue() + ressourceHolder.getGripper().getTorqueX()));
                ressourceHolder.setTorqueY(new AbstractMap.SimpleEntry<>(ressourceHolder.getTorqueY().getKey(), ressourceHolder.getTorqueX().getValue() + ressourceHolder.getGripper().getTorqueY()));
                ressourceHolder.setTorqueZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getTorqueZ().getKey(), ressourceHolder.getTorqueZ().getValue() + ressourceHolder.getGripper().getTorqueZ()));
                ressourceHolder.setPositionRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionRepetitionAccuracyX().getKey(), ressourceHolder.getPositionRepetitionAccuracyX().getValue() + ressourceHolder.getGripper().getPositionRepetitionAccuracyX()));
                ressourceHolder.setPositionRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionRepetitionAccuracyY().getKey(), ressourceHolder.getPositionRepetitionAccuracyY().getValue() + ressourceHolder.getGripper().getPositionRepetitionAccuracyY()));
                ressourceHolder.setPositionRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getPositionRepetitionAccuracyZ().getKey(), ressourceHolder.getPositionRepetitionAccuracyZ().getValue() + ressourceHolder.getGripper().getPositionRepetitionAccuracyZ()));
                ressourceHolder.setRotationRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationRepetitionAccuracyX().getKey(), ressourceHolder.getRotationRepetitionAccuracyX().getValue() + ressourceHolder.getGripper().getRotationRepetitionAccuracyX()));
                ressourceHolder.setRotationRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationRepetitionAccuracyY().getKey(), ressourceHolder.getRotationRepetitionAccuracyY().getValue() + ressourceHolder.getGripper().getRotationRepetitionAccuracyY()));
                ressourceHolder.setRotationRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(ressourceHolder.getRotationRepetitionAccuracyZ().getKey(), ressourceHolder.getRotationRepetitionAccuracyZ().getValue() + ressourceHolder.getGripper().getRotationRepetitionAccuracyZ()));

            }
        });


    }

    public List<StateOfStability> setStateOfStability(List<ProcessRequirement> processRequirementListIn) {
        List<StateOfStability> stateOfStabilityListOut = new ArrayList<>();
        processRequirementListIn.forEach(processRequirement -> {
            StateOfStability stateOfStability = StateOfStability.builder()
                    .idOfSubProcess(processRequirement.getId())
                    .givenStability(processRequirement.isStability())
                    .build();
            stateOfStabilityListOut.add(stateOfStability);
        });

        return stateOfStabilityListOut;
    }

    public List<RequirementSequence> getAllSequencesOfRequirements(ProductRequirementFullObject productRequirementFullObjectIn,
                                                                   Map<String, String> listOfRelevantParametersIn) throws IllegalAccessException {

        List<RequirementSequence> requirementSequencesOut = new ArrayList<>();
        List<ProductProperty> productProperty = productRequirementFullObjectIn.getProductProperty();
        List<ProcessRequirement> processRequirement = productRequirementFullObjectIn.getProcessRequirement();

        Map<String, List<AttributeToValue>> parametersForGivenAttributes = new HashMap<>();

        for (ProcessRequirement process : processRequirement) {
            System.out.println("TV-Vorgang " + process.getTvName());
            Class<?> clazz = process.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (listOfRelevantParametersIn.containsKey(fieldName)) {
                    field.setAccessible(true);
                    double valueOfRessourceAttribute = (double) field.get(process);
                    System.out.println("Attributsname " + fieldName);
                    System.out.println("Wert " + valueOfRessourceAttribute);
                }
            }

        }


        return requirementSequencesOut;
    }


}


