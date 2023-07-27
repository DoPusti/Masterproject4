package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
import com.example.Masterproject4.ProduktAnforderung.ProductProperty;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class ProductRequirementMapperTest {

    ProductRequirementFullObject fullObjectProductRequirment = new ProductRequirementFullObject();
    List<ProductProperty> productProperties = new ArrayList<>();
    List<ProcessRequirement> teilVorgang = new ArrayList<>();

    @Test
    public void fillProductRequirement() throws Exception {
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi3.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            String idShortOfSM = subModelObject.getIdShort();
            System.out.println(idShortOfSM);
            List<SubModelElement> listOfAllSubmodelsElements = subModelObject.getSubmodelElements().getSubmodelElement();
            listOfAllSubmodelsElements.forEach(subModelElement -> {
                switch (idShortOfSM) {
                    case "Identification" -> fillSubModelIdentification(listOfAllSubmodelsElements);
                    case "ProductProperty" -> fillSubModelProductProperty(listOfAllSubmodelsElements);
                }
            });
        });
        /*
        System.out.println("Volles Object");
        System.out.println(fullObjectProductRequirment);
        System.out.println("Liste Teilvorgänge");
        System.out.println(teilVorgang);
        System.out.println("Liste Teile");
        System.out.println(productProperties);

         */
    }


    private void fillSubModelIdentification(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            switch (propertyIdentification.getIdShort()) {
                case "AssetId" -> fullObjectProductRequirment.setAssetId(propertyIdentification.getValue());
            }
        });

    }

    private void fillSubModelProductProperty(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInProductProperty -> {
            // IDShort z.B. Combined Parts
            String idShortOfSubModelElement = subModelElementsInProductProperty.getSubmodelElementCollection().getIdShort();
            System.out.println(idShortOfSubModelElement);
            List<SubModelElement> subModelElementsInProductPropertyDeep1 = subModelElementsInProductProperty.getSubmodelElementCollection().getValue().getSubmodelElement();
            subModelElementsInProductPropertyDeep1.forEach(subModelElementObject -> {
                String idShortOfPart = subModelElementObject.getSubmodelElementCollection().getIdShort();
                System.out.println(idShortOfPart);
                ProductProperty productProperty = new ProductProperty();
                productProperty.setTyp(idShortOfPart);
                List<SubModelElement> subModelElementsInProductPropertyDeep2 = subModelElementObject.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementsInProductPropertyDeep2.forEach(subModelElementObject1 -> {
                    // Property ausprobieren
                    //productProperties
                    Property property1 = subModelElementObject1.getProperty();
                    if (property1!=null) {
                        switch(property1.getIdShort()) {
                            case "Mass" -> productProperty.setMass(Double.parseDouble(property1.getValue()));
                            case "MeanRoughness" -> productProperty.setMeanRoughness(Double.parseDouble(property1.getValue()));
                            case "FerroMagnetic" -> productProperty.setFerroMagnetic(Boolean.parseBoolean(property1.getValue()));
                        }
                    } else {

                       List<SubModelElement>  subModelElementsInProductPropertyDeep3 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                        subModelElementsInProductPropertyDeep3.forEach(subModelElementObject2 -> {
                            Property property2 = subModelElementObject2.getProperty();
                            switch(property2.getIdShort()) {
                                case "Length" -> productProperty.setLength(Double.parseDouble(property2.getValue()));
                                case "Width" -> productProperty.setWidth(Double.parseDouble(property2.getValue()));
                                case "Height" -> productProperty.setHeight(Double.parseDouble(property2.getValue()));
                            }
                        });
                    }

                });
                System.out.println("Fertiges Product " + productProperty);

            });
        });

    }


}