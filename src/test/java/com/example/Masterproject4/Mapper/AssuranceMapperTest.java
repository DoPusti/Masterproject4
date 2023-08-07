package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.Repository.AssuranceRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

//@DataJpaTest
@SpringBootTest
class AssuranceMapperTest {

    AssuranceFullObject assuranceFullObject = new AssuranceFullObject();
    @Autowired
    private AssuranceRepository assuranceRepository;

    @Test
    void fillAssurances() throws JAXBException {


        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\BoschRexroth_CKK110NN1_Axis01_angepasst.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        // Loop über alle Submodelle des AASX-Files
        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            List<SubModelElement> listOfAllSubmodelsElements = subModelObject.getSubmodelElements().getSubmodelElement();
            System.out.println("Idshort vom SubmodelElement " + subModelObject.getIdShort());


            switch (subModelObject.getIdShort()) {
                case "Identification" ->
                        fillSubModelIdentification(listOfAllSubmodelsElements, subModelObject.getIdShort());

                case "Assurances" -> fillSubModelAssurances(listOfAllSubmodelsElements);
                case "MediaSupply" -> fillSubModelMediaSupply(listOfAllSubmodelsElements, subModelObject.getIdShort());
                case "EnvironmentalConditions" ->
                        fillSubModelEnvironmentConditions(listOfAllSubmodelsElements, subModelObject.getIdShort());

                case "EconomicFactors" ->
                        fillSubModelEconomicFactors(listOfAllSubmodelsElements, subModelObject.getIdShort());


            }


        });
        System.out.println("------------------");
        System.out.println("Informationen zum Objekt");
        System.out.println(assuranceFullObject);

        assuranceRepository.save(assuranceFullObject);


    }


    // Submodel Identification
    private void fillSubModelIdentification(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            fillValueInList(propertyIdentification.getIdShort(), propertyIdentification.getValue(), "", SECIdShort);
        });
        System.out.println("Liste für Identification gefüllt");

    }

    // Submodel Assurance
    private void fillSubModelAssurances(List<SubModelElement> listOfAllSubmodelsElements) {
        listOfAllSubmodelsElements.forEach(subModelElementsInAssurance -> {
            System.out.println("SubModelElemente in Assurance");
            // Alle direkten Properties von SMC prüfen
            String idShortOfSMC = subModelElementsInAssurance.getSubmodelElementCollection().getIdShort();
            System.out.println("Idshort von jedem SubmodelElement in Assurance " + idShortOfSMC);

            List<SubModelElement> subModelElementsDeep1 = subModelElementsInAssurance.getSubmodelElementCollection().getValue().getSubmodelElement();

            // Schleife über alle SMC im SM Assurance
            // Erstmal Property testen
            subModelElementsDeep1.forEach(subModelElementObject1 -> {
                Property property1 = subModelElementObject1.getProperty();
                if (property1 != null) {
                    fillValueInList(property1.getIdShort(), property1.getValue(), "", idShortOfSMC);
                } else {
                    // Neue Ebene der SMC
                    String idShortOfSMCInSMC = subModelElementObject1.getSubmodelElementCollection().getIdShort();
                    List<SubModelElement> subModelElementsDeep2 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                    subModelElementsDeep2.forEach(subModelElementObject2 -> {
                        Property property2 = subModelElementObject2.getProperty();
                        if (property2 != null) {
                            fillValueInList(property2.getIdShort(), property2.getValue(), "", idShortOfSMCInSMC);
                        }
                        // Kein Property, sondern Range
                        else {
                            Range range = subModelElementObject2.getRange();
                            fillValueInList(range.getIdShort(), range.getMin(), range.getMax(), idShortOfSMCInSMC);
                        }
                    });
                }

            });

    });
        System.out.println("Liste für Assurances gefüllt");
}

    // Submodel MediaSupply
    private void fillSubModelMediaSupply(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(), property1.getValue(), "", SECIdShort);
        });
        System.out.println("Liste für MediaSupply gefüllt");
    }

    // Submodel EnvironmentConditions
    private void fillSubModelEnvironmentConditions(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(), property1.getValue(), "", SECIdShort);
        });
        System.out.println("Liste für EnvironmentConditions gefüllt");
    }

    // Submodel EconomicFactors
    private void fillSubModelEconomicFactors(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            if (property1 != null) {
                fillValueInList(property1.getIdShort(), property1.getValue(), "", SECIdShort);
            } else {

                String SMCIdSHort = subModelElementObject1.getSubmodelElementCollection().getIdShort();
                List<SubModelElement> subModelElementDeep1 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementDeep1.forEach(subModelElementObject2 -> {
                    Property property2 = subModelElementObject2.getProperty();
                    System.out.println("Aufruf mit " + property2.getIdShort() + " " + property2.getValue() + " " + SMCIdSHort);
                    fillValueInList(property2.getIdShort(), property2.getValue(), "", SMCIdSHort);


                });

            }
        });
        System.out.println("Liste für EconomicFactors gefüllt");

    }

    private void fillValueInList(String idShort, String value1, String value2, String SECIdshort) {
        if (value1.equals("NaN")) {
            value1 = "0";
        }
        if (value2.equals("NaN")) {
            value2 = "0";
        }
        switch (SECIdshort) {
            case "InternalPropertyRelations": assuranceFullObject.setRestAPIAdress(value1); break;
            case "Properties":
                switch (idShort) {
                    case "Mass" -> assuranceFullObject.setMass(Double.parseDouble(value1));
                    case "ConnectionType" -> assuranceFullObject.setConnectionType(value1);
                }
                // Identification
            case "Identification":
                switch (idShort) {
                    case "AssetId" -> assuranceFullObject.setAssetId(value1);
                    case "SerialNumber" -> assuranceFullObject.setSerialNumber(value1);
                    case "URL" -> assuranceFullObject.setUrl(value1);
                }
                // Assurances
            case "Dimensions":
                switch (idShort) {
                    case "Length" -> assuranceFullObject.setLength(Double.parseDouble(value1));
                    case "Width" -> assuranceFullObject.setWidth(Double.parseDouble(value1));
                    case "Height" -> assuranceFullObject.setHeight(Double.parseDouble(value1));
                }
            case "CenterOfMass":
                switch (idShort) {
                    case "X" -> assuranceFullObject.setXCoM(Double.parseDouble(value1));
                    case "Y" -> assuranceFullObject.setYCoM(Double.parseDouble(value1));
                    case "Z" -> assuranceFullObject.setZCoM(Double.parseDouble(value1));
                }
            case "OperatingPrinciple":
                if (value1 != null) {
                    assuranceFullObject.setOperatingPrinciple(value1);
                    break;
                }
            case "Constraints":
                switch (idShort) {
                    case "ForceX" -> assuranceFullObject.setForceX(Double.parseDouble(value1));
                    case "ForceY" -> assuranceFullObject.setForceY(Double.parseDouble(value1));
                    case "ForceZ" -> assuranceFullObject.setForceZ(Double.parseDouble(value1));

                    case "TorqueX" -> assuranceFullObject.setTorqueX(Double.parseDouble(value1));
                    case "TorqueY" -> assuranceFullObject.setTorqueY(Double.parseDouble(value1));
                    case "TorqueZ" -> assuranceFullObject.setTorqueZ(Double.parseDouble(value1));

                    case "PositionRepetitionAccuracyX" ->
                            assuranceFullObject.setPositionRepetitionAccuracyX(Double.parseDouble(value1));
                    case "PositionRepetitionAccuracyY" ->
                            assuranceFullObject.setPositionRepetitionAccuracyY(Double.parseDouble(value1));
                    case "PositionRepetitionAccuracyZ" ->
                            assuranceFullObject.setPositionRepetitionAccuracyZ(Double.parseDouble(value1));

                    case "RotationRepetitionAccuracyX" ->
                            assuranceFullObject.setRotationRepetitionAccuracyX(Double.parseDouble(value1));
                    case "RotationRepetitionAccuracyY" ->
                            assuranceFullObject.setRotationRepetitionAccuracyY(Double.parseDouble(value1));
                    case "RotationRepetitionAccuracyZ" ->
                            assuranceFullObject.setRotationRepetitionAccuracyZ(Double.parseDouble(value1));
                }
                // MediaSupply
            case "MediaSupply":
                switch (idShort) {
                    case "OperatingCurrent" -> assuranceFullObject.setOperatingCurrent(Double.parseDouble(value1));
                    case "OperatingVoltage" -> assuranceFullObject.setOperatingVoltage(Double.parseDouble(value1));
                    case "CompressedAirPressure" ->
                            assuranceFullObject.setCompressedAirPressure(Double.parseDouble(value1));
                    case "AirFlow" -> assuranceFullObject.setAirFlow(Double.parseDouble(value1));
                }
                // Environmental Conditions
            case "EnvironmentalConditions":
                switch (idShort) {
                    case "Temperature" -> assuranceFullObject.setTemperature(Double.parseDouble(value1));
                    case "Pressure" -> assuranceFullObject.setPressure(Double.parseDouble(value1));
                    case "Humidity" -> assuranceFullObject.setHumidity(Double.parseDouble(value1));
                    case "Purity" -> assuranceFullObject.setPurity(Boolean.parseBoolean(value1));
                    case "FoodGrade" -> assuranceFullObject.setFoodGrade(Integer.parseInt(value1));
                    case "Explosiveness" -> assuranceFullObject.setExplosiveness(Integer.parseInt(value1));
                }
                // EconomicFactors
            case "EconomicFactors":
                switch (idShort) {
                    case "Price" -> assuranceFullObject.setPrice(Double.parseDouble(value1));
                    case "Lengthofusage" -> assuranceFullObject.setLengthOfUsage(Double.parseDouble(value1));
                    case "MaintenanceInterval" ->
                            assuranceFullObject.setMaintenanceInterval(Double.parseDouble(value1));
                    case "MaintanceDuration" -> assuranceFullObject.setMaintanceDuration(Double.parseDouble(value1));
                    case "DeliveryTime" -> assuranceFullObject.setDeliveryTime(Double.parseDouble(value1));
                    case "OneTimeLicenceCost" -> assuranceFullObject.setOneTimeLicenceCost(Double.parseDouble(value1));
                    case "MonthlyLicenceCost" -> assuranceFullObject.setMonthlyLicenceCost(Double.parseDouble(value1));
                }
            case "SpaceRequirement":
                switch (idShort) {
                    case "Length":
                        assuranceFullObject.setLengthSR(Double.parseDouble(value1));
                    case "Width":
                        assuranceFullObject.setWidthSR(Double.parseDouble(value1));
                    case "Height":
                        assuranceFullObject.setHeightSR(Double.parseDouble(value1));
                }


        }
    }

;

}