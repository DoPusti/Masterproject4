package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.Handler.RessourceHolder;
import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.Handler.Constraints;
import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
import com.example.Masterproject4.ProduktAnforderung.ProductProperty;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.*;

public class ProductRequirementMapper {

    ProductRequirementFullObject fullObjectProductRequirement = new ProductRequirementFullObject();
    List<ProductProperty> productProperties = new ArrayList<>();
    List<ProcessRequirement> processRequirements = new ArrayList<>();

    public ProductRequirementFullObject mapXMLToClass(File file) throws JAXBException {
        //File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi3.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {

            String idShortOfSM = subModelObject.getIdShort();
            //System.out.println(idShortOfSM);
            //System.out.println(subModelObject);

            switch (idShortOfSM) {
                case "Identification":
                    List<SubModelElement> subModelElements1 = subModelObject.getSubmodelElements().getSubmodelElement();
                    fillSubModelIdentification(subModelElements1);
                    break;
                case "ProductProperty":
                    List<SubModelElement> subModelElements2 = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElements2.forEach(subElements21 -> {
                        //System.out.println("Product Properties");
                        //System.out.println(subElements21.getSubmodelElementCollection().getIdShort());
                        List<SubModelElement> subModelElementsDeep2 = subElements21.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsDeep2.forEach(subModelElementsDeep3 -> {
                            // Jedes einzelne Element in Single Parts und Combined Parts
                            //System.out.println();
                            ProductProperty newProductProperty = fillSubModelProductProperty(subModelElementsDeep3.getSubmodelElementCollection());
                            // z.B. Single Parts und Combined Parts
                            newProductProperty.setTyp(subElements21.getSubmodelElementCollection().getIdShort());
                            productProperties.add(newProductProperty);
                        });
                    });
                    break;
                case "ProcessRequirement":
                    List<SubModelElement> subModelElements3 = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElements3.forEach(subElements31 -> {
                        ProcessRequirement newProcessRequirement = fillSubModelProcessRequirement(subElements31.getSubmodelElementCollection());
                        processRequirements.add(newProcessRequirement);
                    });
                    break;
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
                    }
                });
            }

        });


        //System.out.println("Fertiges Produkt");
        //System.out.println(newProductProperty);
        return newProductProperty;
    }

    private ProcessRequirement fillSubModelProcessRequirement(SubmodelElementCollection collection) {
        //System.out.println(collection);
        ProcessRequirement processRequirement = new ProcessRequirement();
        String idShortOfProcessRequirement = collection.getIdShort();

        List<SubModelElement> subModelElementsInElement = collection.getValue().getSubmodelElement();
        processRequirement.setTvName(idShortOfProcessRequirement);
        subModelElementsInElement.forEach(subModelElementDeep1 -> {
            //System.out.println(subModelElementDeep1);
            Property property = subModelElementDeep1.getProperty();
            if (property != null) {
                if (property.getIdShort().equals("ReferenceParts")) {
                    processRequirement.setReferenceParts(property.getValue());
                }
            } else {
                String idShortInnerhalbSCM = subModelElementDeep1.getSubmodelElementCollection().getIdShort();
                List<SubModelElement> listOfElements = subModelElementDeep1.getSubmodelElementCollection().getValue().getSubmodelElement();
                listOfElements.forEach(subModelElementDeep2 -> {
                    List<SubModelElement> subModelElements3 = subModelElementDeep2.getSubmodelElementCollection().getValue().getSubmodelElement();
                    // bisher noch null bei PreCondition und PostCondition
                    if (subModelElements3 != null) {
                        subModelElements3.forEach(subModelElementDeep3 -> {
                            Property property2 = subModelElementDeep3.getProperty();
                            switch (property2.getIdShort()) {
                                case "PositionX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "PositionY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "PositionZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxSpeedX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxSpeedXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxSpeedXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxSpeedY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxSpeedYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxSpeedYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxSpeedZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxSpeedZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxSpeedZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxAccelerationX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxAccelerationXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxAccelerationXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxAccelerationY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxAccelerationYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxAccelerationYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "MaxAccelerationZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setMaxAccelerationZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setMaxAccelerationZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "ForceX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setForceXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setForceXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "ForceY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setForceYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setForceYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "ForceZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setForceZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setForceZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "PositionRepetitionAccuracyX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionRepetitionAccuracyXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionRepetitionAccuracyXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "PositionRepetitionAccuracyY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionRepetitionAccuracyYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionRepetitionAccuracyYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "PositionRepetitionAccuracyZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setPositionRepetitionAccuracyZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setPositionRepetitionAccuracyZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationRepetitionAccuracyX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationRepetitionAccuracyXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationRepetitionAccuracyXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationRepetitionAccuracyY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationRepetitionAccuracyYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationRepetitionAccuracyYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "RotationRepetitionAccuracyZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setRotationRepetitionAccuracyZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setRotationRepetitionAccuracyZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "TorqueX":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setTorqueXRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setTorqueXSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "TorqueY":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setTorqueYRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setTorqueYSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                                case "TorqueZ":
                                    if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                        processRequirement.setTorqueZRsC(Double.parseDouble(property2.getValue()));
                                    } else {
                                        processRequirement.setTorqueZSsC(Double.parseDouble(property2.getValue()));
                                    }
                                    break;
                            }
                        });
                    }
                });
            }

        });

        //System.out.println("Fertiges Process Requirement");
        //System.out.println(processRequirement);
        return processRequirement;
    }

    public List<RessourceHolder> fillAndSortRequirementList(List<ProcessRequirement> processRequirementListIn,double value) {

        List<RessourceHolder> newRessourceHolderList = new ArrayList<RessourceHolder>();
        Map<String,Double> unsortedMapForceX                        = new HashMap<>();
        Map<String,Double> unsortedMapForceY                        = new HashMap<>();
        Map<String,Double> unsortedMapForceZ                        = new HashMap<>();
        Map<String,Double> unsortedMapTorqueX                       = new HashMap<>();
        Map<String,Double> unsortedMapTorqueY                       = new HashMap<>();
        Map<String,Double> unsortedMapTorqueZ                       = new HashMap<>();
        Map<String,Double> unsortedMapPositionRepetitionAccuracyX   = new HashMap<>();
        Map<String,Double> unsortedMapPositionRepetitionAccuracyY   = new HashMap<>();
        Map<String,Double> unsortedMapPositionRepetitionAccuracyZ   = new HashMap<>();
        Map<String,Double> unsortedMapRotationRepetitionAccuracyX   = new HashMap<>();
        Map<String,Double> unsortedMapRotationRepetitionAccuracyY   = new HashMap<>();
        Map<String,Double> unsortedMapRotationRepetitionAccuracyZ	= new HashMap<>();

        processRequirementListIn.forEach(processRequirement -> {
            unsortedMapForceX.put(processRequirement.getTvName(),processRequirement.getForceXRsC());
            unsortedMapForceY.put(processRequirement.getTvName(),processRequirement.getForceYRsC());
            unsortedMapForceZ.put(processRequirement.getTvName(),processRequirement.getForceZRsC());
            unsortedMapTorqueX.put(processRequirement.getTvName(),processRequirement.getTorqueXRsC());
            unsortedMapTorqueY.put(processRequirement.getTvName(),processRequirement.getTorqueYRsC());
            unsortedMapTorqueZ.put(processRequirement.getTvName(),processRequirement.getTorqueZRsC());
            unsortedMapPositionRepetitionAccuracyX.put(processRequirement.getTvName(),processRequirement.getPositionRepetitionAccuracyXRsC());
            unsortedMapPositionRepetitionAccuracyY.put(processRequirement.getTvName(),processRequirement.getPositionRepetitionAccuracyYRsC());
            unsortedMapPositionRepetitionAccuracyZ.put(processRequirement.getTvName(),processRequirement.getPositionRepetitionAccuracyZRsC());
            unsortedMapRotationRepetitionAccuracyX.put(processRequirement.getTvName(),processRequirement.getRotationRepetitionAccuracyXRsC());
            unsortedMapRotationRepetitionAccuracyY.put(processRequirement.getTvName(),processRequirement.getRotationRepetitionAccuracyYRsC());
            unsortedMapRotationRepetitionAccuracyZ.put(processRequirement.getTvName(),processRequirement.getRotationRepetitionAccuracyZRsC());


        });
        List<Map.Entry<String, Double>> sortedListForceX = new ArrayList<>(unsortedMapForceX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceX);

        List<Map.Entry<String, Double>> sortedListForceY = new ArrayList<>(unsortedMapForceY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceY);

        List<Map.Entry<String, Double>> sortedListForceZ = new ArrayList<>(unsortedMapForceZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListForceZ);

        List<Map.Entry<String, Double>> sortedListTorqueX = new ArrayList<>(unsortedMapTorqueX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueX);

        List<Map.Entry<String, Double>> sortedListTorqueY = new ArrayList<>(unsortedMapTorqueY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueY);

        List<Map.Entry<String, Double>> sortedListTorqueZ = new ArrayList<>(unsortedMapTorqueZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListTorqueZ);

        List<Map.Entry<String, Double>> sortedListPositionRepetitionAccuracyX = new ArrayList<>(unsortedMapPositionRepetitionAccuracyX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyX);

        List<Map.Entry<String, Double>> sortedListPositionRepetitionAccuracyY = new ArrayList<>(unsortedMapPositionRepetitionAccuracyY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyY);

        List<Map.Entry<String, Double>> sortedListPositionRepetitionAccuracyZ = new ArrayList<>(unsortedMapPositionRepetitionAccuracyZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListPositionRepetitionAccuracyZ);

        List<Map.Entry<String, Double>> sortedListRotationRepetitionAccuracyX = new ArrayList<>(unsortedMapRotationRepetitionAccuracyX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyX);

        List<Map.Entry<String, Double>> sortedListRotationRepetitionAccuracyY = new ArrayList<>(unsortedMapRotationRepetitionAccuracyY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyY);

        List<Map.Entry<String, Double>> sortedListRotationRepetitionAccuracyZ = new ArrayList<>(unsortedMapRotationRepetitionAccuracyZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());
        Collections.reverse(sortedListRotationRepetitionAccuracyZ);


        for (int i = 0; i < sortedListForceX.size(); i++) {
            RessourceHolder newRessource = RessourceHolder.builder()
                    .forceX(new AbstractMap.SimpleEntry<>(sortedListForceX.get(i).getKey(),sortedListForceX.get(i).getValue()))
                    .forceY(new AbstractMap.SimpleEntry<>(sortedListForceY.get(i).getKey(),sortedListForceY.get(i).getValue()))
                    .forceZ(new AbstractMap.SimpleEntry<>(sortedListForceZ.get(i).getKey(),sortedListForceZ.get(i).getValue()))
                    .torqueX(new AbstractMap.SimpleEntry<>(sortedListTorqueX.get(i).getKey(),sortedListTorqueX.get(i).getValue()))
                    .torqueY(new AbstractMap.SimpleEntry<>(sortedListTorqueY.get(i).getKey(),sortedListTorqueY.get(i).getValue()))
                    .torqueZ(new AbstractMap.SimpleEntry<>(sortedListTorqueZ.get(i).getKey(),sortedListTorqueZ.get(i).getValue()))
                    .positionRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyX.get(i).getKey(),sortedListPositionRepetitionAccuracyX.get(i).getValue()))
                    .positionRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyY.get(i).getKey(),sortedListPositionRepetitionAccuracyY.get(i).getValue()))
                    .positionRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(sortedListPositionRepetitionAccuracyZ.get(i).getKey(),sortedListPositionRepetitionAccuracyZ.get(i).getValue()))
                    .rotationRepetitionAccuracyX(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyX.get(i).getKey(),sortedListRotationRepetitionAccuracyX.get(i).getValue()))
                    .rotationRepetitionAccuracyY(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyY.get(i).getKey(),sortedListRotationRepetitionAccuracyY.get(i).getValue()))
                    .rotationRepetitionAccuracyZ(new AbstractMap.SimpleEntry<>(sortedListRotationRepetitionAccuracyZ.get(i).getKey(),sortedListRotationRepetitionAccuracyZ.get(i).getValue()))
                    .build();

            newRessourceHolderList.add(newRessource);
        }




        return newRessourceHolderList;
    }

    public List<Constraints> fillAndSortRequirementList(List<ProcessRequirement> processRequirementListIn,String value) {

        List<Double> listOfForceX = new ArrayList<>();
        List<Double> listOfForceY = new ArrayList<>();
        List<Double> listOfForceZ = new ArrayList<>();
        List<Double> listOfTorqueX = new ArrayList<>();
        List<Double> listOfTorqueY = new ArrayList<>();
        List<Double> listOfTorqueZ = new ArrayList<>();
        List<Double> listOfPositionRepetitionAccuracyX = new ArrayList<>();
        List<Double> listOfPositionRepetitionAccuracyY = new ArrayList<>();
        List<Double> listOfPositionRepetitionAccuracyZ = new ArrayList<>();
        List<Double> listOfRotationRepetitionAccuracyX = new ArrayList<>();
        List<Double> listOfRotationRepetitionAccuracyY = new ArrayList<>();
        List<Double> listOfRotationRepetitionAccuracyZ = new ArrayList<>();
        List<Constraints> constraintsList = new ArrayList<>();


        processRequirementListIn.forEach(processRequirement -> {
            listOfForceX.add(processRequirement.getForceXRsC());
            listOfForceY.add(processRequirement.getForceYRsC());
            listOfForceZ.add(processRequirement.getForceZRsC());

            listOfTorqueX.add(processRequirement.getTorqueXRsC());
            listOfTorqueY.add(processRequirement.getTorqueYRsC());
            listOfTorqueZ.add(processRequirement.getTorqueZRsC());

            listOfPositionRepetitionAccuracyX.add(processRequirement.getPositionRepetitionAccuracyXRsC());
            listOfPositionRepetitionAccuracyY.add(processRequirement.getPositionRepetitionAccuracyYRsC());
            listOfPositionRepetitionAccuracyZ.add(processRequirement.getPositionRepetitionAccuracyZRsC());

            listOfRotationRepetitionAccuracyX.add(processRequirement.getRotationRepetitionAccuracyXRsC());
            listOfRotationRepetitionAccuracyY.add(processRequirement.getRotationRepetitionAccuracyYRsC());
            listOfRotationRepetitionAccuracyZ.add(processRequirement.getRotationRepetitionAccuracyZRsC());

            listOfForceX.sort(Collections.reverseOrder());
            listOfForceY.sort(Collections.reverseOrder());
            listOfForceZ.sort(Collections.reverseOrder());
            listOfTorqueX.sort(Collections.reverseOrder());
            listOfTorqueY.sort(Collections.reverseOrder());
            listOfTorqueZ.sort(Collections.reverseOrder());
            listOfPositionRepetitionAccuracyX.sort(Collections.reverseOrder());
            listOfPositionRepetitionAccuracyY.sort(Collections.reverseOrder());
            listOfPositionRepetitionAccuracyZ.sort(Collections.reverseOrder());
            listOfRotationRepetitionAccuracyX.sort(Collections.reverseOrder());
            listOfRotationRepetitionAccuracyY.sort(Collections.reverseOrder());
            listOfRotationRepetitionAccuracyZ.sort(Collections.reverseOrder());

        });
        // Tabellenstruktur
        // ForceX | ForceY | ForceZ | TorqueX | TorqueY | TorqueZ | PositionRepetitionAccuracyX | PositionRepetitionAccuracyY | PositionRepetitionAccuracyZ  | RotationRepetitionAccuracyX | RotationRepetitionAccuracyY | RotationRepetitionAccuracyZ
        for (int i = 0; i < listOfForceX.size(); i++) {
            Constraints constraint = Constraints.builder()
                    .idShort(String.valueOf(i))
                     .forceX(listOfForceX.get(i))
                    .forceY(listOfForceY.get(i))
                    .forceZ(listOfForceZ.get(i))
                    .torqueX(listOfTorqueX.get(i))
                    .torqueY(listOfTorqueY.get(i))
                    .torqueZ(listOfTorqueZ.get(i))
                    .positionRepetitionAccuracyX(listOfPositionRepetitionAccuracyX.get(i))
                    .positionRepetitionAccuracyY(listOfPositionRepetitionAccuracyY.get(i))
                    .positionRepetitionAccuracyZ(listOfPositionRepetitionAccuracyZ.get(i))
                    .rotationRepetitionAccuracyX(listOfRotationRepetitionAccuracyX.get(i))
                    .rotationRepetitionAccuracyY(listOfRotationRepetitionAccuracyY.get(i))
                    .rotationRepetitionAccuracyZ(listOfRotationRepetitionAccuracyZ.get(i))
                    .build();

            constraintsList.add(constraint);
        }

        return constraintsList;
    }

    public void printRessourceHolderList(List<RessourceHolder> ressourceHolderList) {
        for (RessourceHolder ressourceHolder : ressourceHolderList) {
            System.out.println("ID: " + ressourceHolder.getIdShort());
            System.out.println("REST API: " + ressourceHolder.getRestApi());
            System.out.println("Mass: " + ressourceHolder.getMass());

            printEntry("Force X", ressourceHolder.getForceX());
            printEntry("Force Y", ressourceHolder.getForceY());
            printEntry("Force Z", ressourceHolder.getForceZ());
            printEntry("Torque X", ressourceHolder.getTorqueX());
            printEntry("Torque Y", ressourceHolder.getTorqueY());
            printEntry("Torque Z", ressourceHolder.getTorqueZ());
            printEntry("Position Repetition Accuracy X", ressourceHolder.getPositionRepetitionAccuracyX());
            printEntry("Position Repetition Accuracy Y", ressourceHolder.getPositionRepetitionAccuracyY());
            printEntry("Position Repetition Accuracy Z", ressourceHolder.getPositionRepetitionAccuracyZ());
            printEntry("Rotation Repetition Accuracy X", ressourceHolder.getRotationRepetitionAccuracyX());
            printEntry("Rotation Repetition Accuracy Y", ressourceHolder.getRotationRepetitionAccuracyY());
            printEntry("Rotation Repetition Accuracy Z", ressourceHolder.getRotationRepetitionAccuracyZ());
            System.out.println(); // Leerzeile zur Trennung
        }
    }
    private static void printEntry(String description, AbstractMap.SimpleEntry<String, Double> entry) {
        System.out.println(description + ": " + entry.getKey() + " - " + entry.getValue());
    }


}


