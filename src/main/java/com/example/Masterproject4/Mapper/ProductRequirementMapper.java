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

    public ProductRequirementFullObject mapXMLToClass(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);


        ProductRequirementFullObject productRequirementFullObject = new ProductRequirementFullObject();

        List<ProductProperty> productProperties = new ArrayList<>();
        List<ProcessRequirement> teilVorgang = new ArrayList<>();
        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();

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
                            ProductProperty productPropertyOfProductRequirement = new ProductProperty();
                            productPropertyOfProductRequirement.setTyp(subModelElementParts.getSubmodelElementCollection().getIdShort());
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
                                                            productPropertyOfProductRequirement.setLength(Double.parseDouble(property2.getValue()));
                                                    case "Width" ->
                                                            productPropertyOfProductRequirement.setWidth(Double.parseDouble(property2.getValue()));
                                                    case "Height" ->
                                                            productPropertyOfProductRequirement.setHeight(Double.parseDouble(property2.getValue()));
                                                    case "X" ->
                                                            productPropertyOfProductRequirement.setX(Double.parseDouble(property2.getValue()));
                                                    case "Y" ->
                                                            productPropertyOfProductRequirement.setY(Double.parseDouble(property2.getValue()));
                                                    case "Z" ->
                                                            productPropertyOfProductRequirement.setZ(Double.parseDouble(property2.getValue()));
                                                }
                                            });
                                        }
                                        // Property ist direkt da
                                        else {
                                            switch (property.getIdShort()) {
                                                case "Mass" ->
                                                        productPropertyOfProductRequirement.setMass(Double.parseDouble(property.getValue()));
                                                case "StaticFrictionCoefficient" ->
                                                        productPropertyOfProductRequirement.setStaticFrictionCoefficient(Double.parseDouble(property.getValue()));
                                                case "FerroMagnetic" ->
                                                        productPropertyOfProductRequirement.setFerroMagnetic(Boolean.parseBoolean(property.getValue()));
                                            }
                                        }
                                    }


                            );
                            productProperties.add(productPropertyOfProductRequirement);
                        });
                    });
                    break;
                case "ProcessRequirement":
                    List<SubModelElement> subModelElementsInProcessRequirement = subModelObject.getSubmodelElements().getSubmodelElement();
                    subModelElementsInProcessRequirement.forEach(object4 -> {
                        //Von Jedem Element die SubModelElementCollection filtern
                        ProcessRequirement teilVorGangParts = new ProcessRequirement();
                        teilVorGangParts.setTvName(object4.getSubmodelElementCollection().getIdShort());
                        System.out.println("Id vom SMC " + teilVorGangParts.getTvName());
                        //2 Elemente mit SMC und eins mit Property
                        List<SubModelElement> subModelElementsInSMC = object4.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsInSMC.forEach(object5 -> {
                            Property property5 = object5.getProperty();
                            // SMC Rest

                        });
                        teilVorgang.add(teilVorGangParts);

                    });
                    // Zur Liste hinzufügen

                    break;
            }
        });
        productRequirementFullObject.setProductProperty(productProperties);
        //productRequirementFullObject.setTeilVorgang(teilVorgang);

        return productRequirementFullObject;
    }

}
