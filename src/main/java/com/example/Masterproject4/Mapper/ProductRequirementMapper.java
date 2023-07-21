package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.ProductRequirement;
import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import com.example.Masterproject4.ProduktAnforderung.Part;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import com.example.Masterproject4.ProduktAnforderung.Teilvorgang;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductRequirementMapper {

    public ProductRequirement unmarschallXML(File file) throws JAXBException {
        File file1 = new File("src\\main\\resources\\ProductRequirementsForTest\\ProductRequirementWithMultipleTVS.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(com.example.Masterproject4.JAXBModels.ProductRequirement.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ProductRequirement productRequirement = (ProductRequirement) unmarshaller.unmarshal(file);
        return productRequirement;
    }

    public ProductRequirementFullObject mapXMLToClass(ProductRequirement productRequirement) {
        ProductRequirementFullObject productRequirementFullObject = new ProductRequirementFullObject();
        List<Part> parts = new ArrayList<>();
        List<Teilvorgang> teilVorgang = new ArrayList<>();
        List<SubModel> listOfAllSubmodels = productRequirement.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            switch (subModelObject.getIdShort()) {
                case "Identification":
                    Property propertyForIdentification = subModelObject.getSubmodelElements().getSubmodelElement().get(0).getProperty();
                    System.out.println("IdShort : " + propertyForIdentification.getIdShort());
                    System.out.println("Value   : " + propertyForIdentification.getValue());
                    productRequirementFullObject.setAssetId(propertyForIdentification.getValue());
                    break;
                case "ProductPropertyOverall":
                    //submodel -> submodelElements -> submodelElement -> submodelElementCollection
                    List<SubModelElement> subModelElementsInProductProperty = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElementsInProductProperty.forEach(subModelElementInProductProperty -> {
                        System.out.println("IdShort von SMC: " + subModelElementInProductProperty.getSubmodelElementCollection().getIdShort());
                        List<SubModelElement> subModelElementsInSMC = subModelElementInProductProperty.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsInSMC.forEach(subModelElementParts -> {
                            System.out.println("IdShort von Member in SMC: " + subModelElementParts.getSubmodelElementCollection().getIdShort());
                            Part partOfProductRequirement = new Part();
                            partOfProductRequirement.setTyp(subModelElementParts.getSubmodelElementCollection().getIdShort());
                            List<SubModelElement> subModelElementsInSMCInSMC = subModelElementParts.getSubmodelElementCollection().getValue().getSubmodelElement();
                            subModelElementsInSMCInSMC.forEach(subModelElementInSMCInSMC -> {

                                        Property property = subModelElementInSMCInSMC.getProperty();
                                        // SCM und kein Property
                                        if (property == null) {
                                            List<SubModelElement> subModelElementsInProperty = subModelElementInSMCInSMC.getSubmodelElementCollection().getValue().getSubmodelElement();
                                            subModelElementsInProperty.stream().forEach(subModelElementProperty -> {
                                                Property property2 = subModelElementProperty.getProperty();
                                                switch (property2.getIdShort()) {
                                                    case "Length" ->
                                                            partOfProductRequirement.setLength(Double.parseDouble(property2.getValue()));
                                                    case "Width" ->
                                                            partOfProductRequirement.setWidth(Double.parseDouble(property2.getValue()));
                                                    case "Height" ->
                                                            partOfProductRequirement.setHeight(Double.parseDouble(property2.getValue()));
                                                    case "X" ->
                                                            partOfProductRequirement.setX(Double.parseDouble(property2.getValue()));
                                                    case "Y" ->
                                                            partOfProductRequirement.setY(Double.parseDouble(property2.getValue()));
                                                    case "Z" ->
                                                            partOfProductRequirement.setZ(Double.parseDouble(property2.getValue()));
                                                }
                                                //System.out.println("IdShort: " + property2.getIdShort());
                                                //System.out.println("Value: " + property2.getValue());

                                            });
                                        }
                                        // Property ist direkt da
                                        else {
                                            switch (property.getIdShort()) {
                                                case "Mass" ->
                                                        partOfProductRequirement.setMass(Double.parseDouble(property.getValue()));
                                                case "StaticFrictionCoefficient" ->
                                                        partOfProductRequirement.setStaticFrictionCoefficient(Double.parseDouble(property.getValue()));
                                                case "FerroMagnetic" ->
                                                        partOfProductRequirement.setFerroMagnetic(Boolean.parseBoolean(property.getValue()));
                                            }

                                            //System.out.println("IdShort: " + subModelElementInSMCInSMC.getProperty().getIdShort());
                                            //System.out.println("Value: " + subModelElementInSMCInSMC.getProperty().getValue());
                                        }
                                        //System.out.println("Nach Belegen eines Wertes " + partOfProductRequirement);
                                    }


                            );
                            parts.add(partOfProductRequirement);
                        });
                    });
                    break;
                case "ProcessRequirement":
                    List<SubModelElement> subModelElementsInProcessRequirement = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElementsInProcessRequirement.forEach(object4 -> {
                        //Von Jedem Element die SubModelElementCollection filtern
                        //System.out.println(object4);
                        Teilvorgang teilVorGangParts = new Teilvorgang();
                        teilVorGangParts.setTvName(object4.getSubmodelElementCollection().getIdShort());
                        System.out.println("Id vom SMC " + teilVorGangParts.getTvName());
                        //2 Elemente mit SMC und eins mit Property
                        List<SubModelElement> subModelElementsInSMC = object4.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsInSMC.forEach(object5 -> {
                            Property property5 = object5.getProperty();
                            // SMC Rest
                            if (property5 == null) {
                                List<SubModelElement> subModelElementsRest = object5.getSubmodelElementCollection().getValue().getSubmodelElement();
                                subModelElementsRest.forEach(subModelElement -> {
                                    Property property6 = subModelElement.getProperty();
                                    switch (property6.getIdShort()) {
                                        case "PositionX" ->
                                                teilVorGangParts.setPositionX(Double.parseDouble(property6.getValue()));
                                        case "PositionY" ->
                                                teilVorGangParts.setPositionY(Double.parseDouble(property6.getValue()));
                                        case "PositionZ" ->
                                                teilVorGangParts.setPositionZ(Double.parseDouble(property6.getValue()));
                                        case "RotationX" ->
                                                teilVorGangParts.setRotationX(Double.parseDouble(property6.getValue()));
                                        case "RotationY" ->
                                                teilVorGangParts.setRotationY(Double.parseDouble(property6.getValue()));
                                        case "RotationZ" ->
                                                teilVorGangParts.setRotationZ(Double.parseDouble(property6.getValue()));
                                        case "ForceX" ->
                                                teilVorGangParts.setForceX(Double.parseDouble(property6.getValue()));
                                        case "ForceY" ->
                                                teilVorGangParts.setForceY(Double.parseDouble(property6.getValue()));
                                        case "ForceZ" ->
                                                teilVorGangParts.setForceZ(Double.parseDouble(property6.getValue()));
                                        case "MomentumX" ->
                                                teilVorGangParts.setMomentumX(Double.parseDouble(property6.getValue()));
                                        case "MomentumY" ->
                                                teilVorGangParts.setMomentumY(Double.parseDouble(property6.getValue()));
                                        case "MomentumZ" ->
                                                teilVorGangParts.setMomentumZ(Double.parseDouble(property6.getValue()));
                                    }
                                });


                            }
                            // Property Reference Parts
                            else {
                                teilVorGangParts.setReferenceParts(property5.getValue());
                                System.out.println("ReferenceParts gesetzt mit : " + teilVorGangParts.getReferenceParts());

                            }
                        });
                        teilVorgang.add(teilVorGangParts);

                    });
                    // Zur Liste hinzufügen

                    break;
            }
        });
        System.out.println(parts);
        productRequirementFullObject.setPart(parts);
        productRequirementFullObject.setTeilVorgang(teilVorgang);

        return productRequirementFullObject;
    }

}
