package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
import com.example.Masterproject4.ProduktAnforderung.ProductProperty;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            List<SubModelElement> listOfAllSubmodelsElements = subModelObject.getSubmodelElements().getSubmodelElement();
            listOfAllSubmodelsElements.forEach(subModelElement -> {
                switch (idShortOfSM) {
                    case "Identification" -> fillSubModelIdentification(listOfAllSubmodelsElements);
                    case "ProductProperty" -> fillSubModelProductProperty(listOfAllSubmodelsElements);
                    case "ProcessRequirement" -> fillSubModelProcessRequirement(listOfAllSubmodelsElements);
                }
            });
        });
        fullObjectProductRequirement.setProductProperty(productProperties);
        fullObjectProductRequirement.setProcessRequirement(processRequirements);
        return fullObjectProductRequirement;
    }


    private void fillSubModelIdentification(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            switch (propertyIdentification.getIdShort()) {
                case "AssetId" -> fullObjectProductRequirement.setAssetId(propertyIdentification.getValue());
            }
        });

    }

    private void fillSubModelProductProperty(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInProductProperty -> {
            // IDShort z.B. Combined Parts
            String idShortOfSubModelElement = subModelElementsInProductProperty.getSubmodelElementCollection().getIdShort();
            List<SubModelElement> subModelElementsInProductPropertyDeep1 = subModelElementsInProductProperty.getSubmodelElementCollection().getValue().getSubmodelElement();
            subModelElementsInProductPropertyDeep1.forEach(subModelElementObject -> {
                String idShortOfPart = subModelElementObject.getSubmodelElementCollection().getIdShort();
                ProductProperty productProperty = new ProductProperty();
                productProperty.setIdShort(idShortOfPart);
                productProperty.setTyp(idShortOfSubModelElement);
                List<SubModelElement> subModelElementsInProductPropertyDeep2 = subModelElementObject.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementsInProductPropertyDeep2.forEach(subModelElementObject1 -> {
                    // Property ausprobieren
                    //productProperties
                    Property property1 = subModelElementObject1.getProperty();
                    if (property1 != null) {
                        switch (property1.getIdShort()) {
                            case "Mass" -> productProperty.setMass(Double.parseDouble(property1.getValue()));
                            case "MeanRoughness" ->
                                    productProperty.setMeanRoughness(Double.parseDouble(property1.getValue()));
                            case "FerroMagnetic" ->
                                    productProperty.setFerroMagnetic(Boolean.parseBoolean(property1.getValue()));
                        }
                    } else {

                        List<SubModelElement> subModelElementsInProductPropertyDeep3 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsInProductPropertyDeep3.forEach(subModelElementObject2 -> {
                            Property property2 = subModelElementObject2.getProperty();
                            switch (property2.getIdShort()) {
                                case "Length" -> productProperty.setLength(Double.parseDouble(property2.getValue()));
                                case "Width" -> productProperty.setWidth(Double.parseDouble(property2.getValue()));
                                case "Height" -> productProperty.setHeight(Double.parseDouble(property2.getValue()));
                            }
                        });
                    }

                });
                productProperties.add(productProperty);

            });
        });

    }

    private void fillSubModelProcessRequirement(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInProcessRequirement -> {
            String idShortOfProcessRequirement = subModelElementsInProcessRequirement.getSubmodelElementCollection().getIdShort();
            List<SubModelElement> subModelElementsInElement = subModelElementsInProcessRequirement.getSubmodelElementCollection().getValue().getSubmodelElement();
            ProcessRequirement processRequirement = new ProcessRequirement();
            processRequirement.setTvName(idShortOfProcessRequirement);
            subModelElementsInElement.forEach(subModelElementDeep1 -> {
                Property property = subModelElementDeep1.getProperty();
                if (property != null) {
                    switch (property.getIdShort()) {
                        case "ReferenceParts" -> processRequirement.setReferenceParts(property.getValue());
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
                                        processRequirement.setPositionX(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "PositionY":
                                        processRequirement.setPositionY(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "PositionZ":
                                        processRequirement.setPositionZ(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "RotationX":
                                        processRequirement.setRotationX(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "RotationY":
                                        processRequirement.setRotationY(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "RotationZ":
                                        processRequirement.setRotationZ(Double.parseDouble(property2.getValue()));
                                        break;
                                    case "MaxSpeedX":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxSpeedXRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxSpeedXSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "MaxSpeedY":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxSpeedYRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxSpeedYSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "MaxSpeedZ":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxSpeedZRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxSpeedZSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "MaxAccelerationX":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxAccelerationXRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxAccelerationXSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "MaxAccelerationY":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxAccelerationYRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxAccelerationYSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "MaxAccelerationZ":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setMaxAccelerationZRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setMaxAccelerationZSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "ForceX":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setForceXRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setForceXSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "ForceY":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setForceYRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setForceYSsC(Double.parseDouble(property2.getValue()));
                                        }
                                    case "ForceZ":
                                        if (idShortInnerhalbSCM.equals("RequiredStateChange")) {
                                            processRequirement.setForceZRsC(Double.parseDouble(property2.getValue()));
                                        } else {
                                            processRequirement.setForceZSsC(Double.parseDouble(property2.getValue()));
                                        }
                                }
                            });
                        }
                    });
                }
                processRequirements.add(processRequirement);
            });
        });

    }

    ;
    }


