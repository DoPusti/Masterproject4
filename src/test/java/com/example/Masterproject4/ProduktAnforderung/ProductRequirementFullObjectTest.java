package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.JAXBModels.ProductRequirement;
import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductRequirementFullObjectTest {

    @Test
    public void fillProductRequirement() throws JAXBException {
        List<Part> parts = new ArrayList<>();


        List<Teilvorgang> teilVorgang = new ArrayList<>();

        Teilvorgang teilvorgangOfProductRequirement = new Teilvorgang();

        ProductRequirementFullObject productRequirementFullObject = new ProductRequirementFullObject();

        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ProductRequirementWithMultipleTVS.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(com.example.Masterproject4.JAXBModels.ProductRequirement.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ProductRequirement productRequirement = (ProductRequirement) unmarshaller.unmarshal(file);

        // Loop über alle Submodelle des AASX-Files
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
                            System.out.println("Wert vor dem Speichern: " + partOfProductRequirement.getTyp());
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
                                                /*
                                                System.out.println("IdShort: " + property2.getIdShort());
                                                System.out.println("Value: " + property2.getValue());

                                                 */
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
                                            /*
                                            System.out.println("IdShort: " + subModelElementInSMCInSMC.getProperty().getIdShort());
                                            System.out.println("Value: " + subModelElementInSMCInSMC.getProperty().getValue());

                                             */

                                        }
                                            System.out.println("Nach Belegen eines Wertes " + partOfProductRequirement);
                                    }



                            );
                            System.out.println("Vor dem Hinzufügen " + partOfProductRequirement);
                            System.out.println("Vor dem Hinzufügen " + parts);
                            parts.add(partOfProductRequirement);
                            System.out.println("Nach dem Hinzufügen " + partOfProductRequirement);
                            System.out.println("Nach dem Hinzufügen " + parts);
                        });
                    });
                    break;
                case "ProcessRequirement":
                    break;
            }
        });
        System.out.println(parts);
        productRequirementFullObject.setPart(parts);
        System.out.println("---------------------------------------------");
        System.out.println("Infos zum Objekt ProductRequirementFullObject");
        System.out.println(productRequirementFullObject.getAssetId());
        System.out.println(productRequirementFullObject.getPart());
        System.out.println(productRequirementFullObject.getPart().size());


    }

}