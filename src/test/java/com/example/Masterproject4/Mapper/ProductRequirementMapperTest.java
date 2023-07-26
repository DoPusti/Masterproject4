package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.ProduktAnforderung.Part;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import com.example.Masterproject4.ProduktAnforderung.Teilvorgang;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRequirementMapperTest {


    ProductRequirementFullObject fullObjectProductRequirment = new ProductRequirementFullObject();
    List<Part> parts = new ArrayList<>();
    List<Teilvorgang> teilVorgang = new ArrayList<>();

    @Test
    public void fillProductRequirement() throws Exception {
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi2.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            System.out.println(subModelObject.getIdShort());
            List<SubModelElement> subModelElements = subModelObject.getSubmodelElements().getSubmodelElement();
            subModelElements.forEach(subModelElement -> {
                String idShortOfSM = subModelObject.getIdShort();
                System.out.println(subModelElement);
                switch (subModelObject.getIdShort()) {
                    //Teilvorgang einzelnerVorgang = null;
                   // Part einzelnesTeil = null;
                    case "Identification" :
                        Property property1 = subModelElement.getProperty();
                        /*
                        fillValueInList(property1.getIdShort(),property1.getValue(),idShortOfSM,einzelnesTeil,einzelnerVorgang);

                         */
                }
            });
        });
        System.out.println("Volles Object");
        System.out.println(fullObjectProductRequirment);
        System.out.println("Liste Teilvorgänge");
        System.out.println(teilVorgang);
        System.out.println("Liste Teile");
        System.out.println(parts);
    }


    private void fillValueInList(String idShort, String value1, String idShortOfSM,
                                 Part teileVonRequirement, Teilvorgang vorgaengeVonRequirement) throws Exception {
        if(value1.equals("NaN")) {
            value1 = "0";
        }
        switch(idShort) {
            case "ReferenceParts" -> vorgaengeVonRequirement.setReferenceParts(value1);
            case "Mass" -> teileVonRequirement.setMass(Double.parseDouble(value1));
            case "MeanRoughness" -> teileVonRequirement.setMeanRoughness(Double.parseDouble(value1));
            case "FerroMagnetic" -> teileVonRequirement.setFerroMagnetic(Boolean.parseBoolean(value1));
        }
        switch (idShortOfSM) {
            // Identification
            case "Identification":
                switch (idShort) {
                    case "AssetId" -> fullObjectProductRequirment.setAssetId(value1);
                }
            case "RequiredPropertyChanges":
                switch (idShort) {
                    case "PositionX" -> vorgaengeVonRequirement.setPositionX(Double.parseDouble(value1));
                    case "PositionY" -> vorgaengeVonRequirement.setPositionY(Double.parseDouble(value1));
                    case "PositionZ" -> vorgaengeVonRequirement.setPositionZ(Double.parseDouble(value1));
                    case "RotationX" -> vorgaengeVonRequirement.setRotationX(Double.parseDouble(value1));
                    case "RotationY" -> vorgaengeVonRequirement.setRotationY(Double.parseDouble(value1));
                    case "RotationZ" -> vorgaengeVonRequirement.setRotationZ(Double.parseDouble(value1));

                }
            case "RequiredFurtherContraints":
                switch (idShort) {
                    case "ForceX" -> vorgaengeVonRequirement.setForceX(Double.parseDouble(value1));
                    case "ForceY" -> vorgaengeVonRequirement.setForceY(Double.parseDouble(value1));
                    case "ForceZ" -> vorgaengeVonRequirement.setForceZ(Double.parseDouble(value1));
                    case "MomentumX" -> vorgaengeVonRequirement.setMomentumX(Double.parseDouble(value1));
                    case "MomentumY" -> vorgaengeVonRequirement.setMomentumY(Double.parseDouble(value1));
                    case "MomentumZ" -> vorgaengeVonRequirement.setMomentumZ(Double.parseDouble(value1));

                }
            case "CenterOfMass":
                switch (idShort) {
                    case "X" -> teileVonRequirement.setX(Double.parseDouble(value1));
                    case "Y" -> teileVonRequirement.setZ(Double.parseDouble(value1));
                    case "Z" -> teileVonRequirement.setZ(Double.parseDouble(value1));
                }
            case "Dimensions":
                switch (idShort) {
                    case "Length" -> teileVonRequirement.setLength(Double.parseDouble(value1));
                    case "Width" ->  teileVonRequirement.setWidth(Double.parseDouble(value1));
                    case "Height" -> teileVonRequirement.setHeight(Double.parseDouble(value1));
                }



        }
    }

}