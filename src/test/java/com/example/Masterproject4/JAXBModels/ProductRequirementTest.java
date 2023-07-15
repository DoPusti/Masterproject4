package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.File;



class ProductRequirementTest {

    @Test
    void xmlFormatter() throws JAXBException {
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ProductRequirement.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(ProductRequirement.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ProductRequirement productRequirement = (ProductRequirement) unmarshaller.unmarshal(file);

       // System.out.println(productRequirement.getAssetAdministrationShells());
       //  System.out.println(productRequirement.getAssets());
        System.out.println(productRequirement.getSubmodels());

    }
}