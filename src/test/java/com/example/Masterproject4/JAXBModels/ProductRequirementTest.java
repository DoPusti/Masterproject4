package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;


class ProductRequirementTest {

    @Test
    void xmlFormatterGetSubmodels() throws JAXBException {
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ProductRequirement.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(ProductRequirement.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ProductRequirement productRequirement = (ProductRequirement) unmarshaller.unmarshal(file);

        // System.out.println(productRequirement.getAssetAdministrationShells());
        //  System.out.println(productRequirement.getAssets());
        System.out.println(productRequirement.getSubmodels());

    }

    @Test
    void xmlFormatterGetAllValues() throws JAXBException {
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ProductRequirementTVXML.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(ProductRequirement.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ProductRequirement productRequirement = (ProductRequirement) unmarshaller.unmarshal(file);

        // Alle Submodels des Assets
        List<SubModel> listeOfSubmodels = productRequirement.getSubmodels().getSubmodel();
        listeOfSubmodels.stream().forEach(objekt -> {
            System.out.println("Submodel " + objekt.getIdShort());

            // 1 bis n SubModelElemente
            List<SubModelElement> submodelElement = objekt.getSubmodelElements().getSubmodelElement();

            submodelElement.stream().forEach(objekt1 -> {
                // Property nicht direkt im Baum vorhanden, ggf. danach noch SCM
                Property property = objekt1.getProperty();
                if (property != null) {
                    System.out.println("IDShort: " + property.getIdShort());
                    System.out.println("Value: " + property.getValue());

                } else {
                    System.out.println(objekt1.getSubmodelElementCollection().getIdShort());
                    List<SubModelElement> submodelElementsInCollection = objekt1.getSubmodelElementCollection().getValue().getSubmodelElement();
                    // Einzelne Teilvorgänge
                    if (objekt1.getSubmodelElementCollection().getIdShort().equals("TV1")) {
                        submodelElementsInCollection.stream().forEach(objekt3 -> {
                            //System.out.println(objekt3);
                            System.out.println(objekt3.getSubmodelElementCollection().getIdShort());
                            List<SubModelElement> submodelsInTVCollectioN = objekt3.getSubmodelElementCollection().getValue().getSubmodelElement();
                            submodelsInTVCollectioN.stream().forEach(objekt5 -> {
                                Property property5 = objekt5.getProperty();
                                System.out.println(property5.getIdShort());
                                System.out.println(property5.getValue());
                            });
                        });
                    } else {
                        submodelElementsInCollection.stream().forEach(objekt4 -> {
                            Property property1 = objekt4.getProperty();
                            if (property1 != null) {
                                System.out.println("IDShort: " + property1.getIdShort());
                                System.out.println("Value: " + property1.getValue());
                            } else {
                                System.out.println("Fehlerhafte Property");
                            }


                        });
                    }


                }

            });

        });


    }
}