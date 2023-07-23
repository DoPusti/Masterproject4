package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.Repository.AssuranceRepository;
import com.example.Masterproject4.Zusicherung.AssuranceFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class AssuranceMapper {

    AssuranceFullObject assuranceFullObject = new AssuranceFullObject();


    public AssuranceFullObject saveXMLToDatabase(File file) throws JAXBException {
        // Testfile
        File file1 = new File("src\\main\\resources\\ProductRequirementsForTest\\ABBScaraIRB920T_Axis07.xml");


        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            List<SubModelElement> listOfAllSubmodelsElements = subModelObject.getSubmodelElements().getSubmodelElement();
            switch (subModelObject.getIdShort()) {
                case "Identification" ->
                        fillSubModelIdentification(listOfAllSubmodelsElements, subModelObject.getIdShort());
                case "Assurances" ->
                        fillSubModelAssurances(listOfAllSubmodelsElements);
                case "MediaSupply" ->
                        fillSubModelMediaSupply(listOfAllSubmodelsElements,subModelObject.getIdShort());
                case "EnvironmentalConditions" ->
                        fillSubModelEnvironmentConditions(listOfAllSubmodelsElements,subModelObject.getIdShort());

                case "EconomicFactors" ->
                        fillSubModelEconomicFactors(listOfAllSubmodelsElements,subModelObject.getIdShort());

            }
        });
        System.out.println("------------------");
        System.out.println("Informationen zum Objekt");
        System.out.println(assuranceFullObject);
        return assuranceFullObject;
        //assuranceRepository.save(assuranceFullObject);

    }
    // Submodel Identification
    private void fillSubModelIdentification(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            fillValueInList(propertyIdentification.getIdShort(), propertyIdentification.getValue(), "", SECIdShort);
        });

    }

    // Submodel Assurance
    private void fillSubModelAssurances(List<SubModelElement> listOfAllSubmodelsElements) {
        listOfAllSubmodelsElements.forEach(subModelElementsInAssurance -> {
            // Alle direkten Properties von SMC prüfen
            String idShortOfSMC = subModelElementsInAssurance.getSubmodelElementCollection().getIdShort();
            List<SubModelElement> subModelElementsDeep1 = subModelElementsInAssurance.getSubmodelElementCollection().getValue().getSubmodelElement();
            if(!Objects.equals(idShortOfSMC, "InternalPropertyRelations")) {
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
            }


        });
    }

    // Submodel MediaSupply
    private void fillSubModelMediaSupply(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(),property1.getValue(),"",SECIdShort);
        });
    }

    // Submodel EnvironmentConditions
    private void fillSubModelEnvironmentConditions(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(),property1.getValue(),"",SECIdShort);
        });
    }

    // Submodel EconomicFactors
    private void fillSubModelEconomicFactors(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            if(property1 != null) {
                fillValueInList(property1.getIdShort(),property1.getValue(),"",SECIdShort);
            } else {

                String SMCIdSHort = subModelElementObject1.getSubmodelElementCollection().getIdShort();
                List<SubModelElement> subModelElementDeep1 = subModelElementObject1.getSubmodelElementCollection().getValue().getSubmodelElement();
                subModelElementDeep1.forEach(subModelElementObject2 -> {
                    Property property2 = subModelElementObject2.getProperty();
                    System.out.println("Aufruf mit " + property2.getIdShort() + " " + property2.getValue() + " " + SMCIdSHort);
                    fillValueInList(property2.getIdShort(),property2.getValue(),"",SMCIdSHort);


                }) ;

            }
        });

    }
    private void fillValueInList(String idShort, String value1, String value2, String SECIdshort) {
        if(value1.equals("NaN")) {
            value1 = "0";
        }
        if(value2.equals("NaN")) {
            value2 = "0";
        }
        switch (SECIdshort) {
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
            case "X":
                switch (idShort) {
                    case "Position" -> assuranceFullObject.setXPosition(Double.parseDouble(value1));
                    case "PositionRepetitionAccuracy" ->
                            assuranceFullObject.setXPositionRepetitionAccuracy(Double.parseDouble(value1));
                    case "Rotation" -> assuranceFullObject.setXRotation(Double.parseDouble(value1));
                    case "RotationRepetitionAccuracy" ->
                            assuranceFullObject.setXRotationRepetitionAccuracy(Double.parseDouble(value1));
                }
            case "Y":
                switch (idShort) {
                    case "Position" -> assuranceFullObject.setYPosition(Double.parseDouble(value1));
                    case "PositionRepetitionAccuracy" ->
                            assuranceFullObject.setYPositionRepetitionAccuracy(Double.parseDouble(value1));
                    case "Rotation" -> assuranceFullObject.setYRotation(Double.parseDouble(value1));
                    case "RotationRepetitionAccuracy" ->
                            assuranceFullObject.setYRotationRepetitionAccuracy(Double.parseDouble(value1));
                }
            case "Z":
                switch (idShort) {
                    case "Position" -> assuranceFullObject.setZPosition(Double.parseDouble(value1));
                    case "PositionRepetitionAccuracy" ->
                            assuranceFullObject.setZPositionRepetitionAccuracy(Double.parseDouble(value1));
                    case "Rotation" -> assuranceFullObject.setZRotation(Double.parseDouble(value1));
                    case "RotationRepetitionAccuracy" ->
                            assuranceFullObject.setZRotationRepetitionAccuracy(Double.parseDouble(value1));
                }
            case "Force":
                switch (idShort) {
                    case "X":
                        assuranceFullObject.setXForceMin(Double.parseDouble(value1));
                        assuranceFullObject.setXForceMax(Double.parseDouble(value2));
                    case "Y":
                        assuranceFullObject.setYForceMin(Double.parseDouble(value1));
                        assuranceFullObject.setYForceMax(Double.parseDouble(value2));
                    case "Z":
                        assuranceFullObject.setZForceMin(Double.parseDouble(value1));
                        assuranceFullObject.setZForceMax(Double.parseDouble(value2));

                }
            case "Torque":
                switch (idShort) {
                    case "X" -> assuranceFullObject.setXTorque(Double.parseDouble(value1));
                    case "Y" -> assuranceFullObject.setYTorque(Double.parseDouble(value1));
                    case "Z" -> assuranceFullObject.setZTorque(Double.parseDouble(value1));

                }

                // MediaSupply
            case "MediaSupply":
                switch (idShort) {
                    case "OperatingCurrent" -> assuranceFullObject.setOperatingCurrent(Double.parseDouble(value1));
                    case "OperatingVoltage" -> assuranceFullObject.setOperatingVoltage(Double.parseDouble(value1));
                    case "CompressedAirPressure" ->
                            assuranceFullObject.setCompressedAirPressure(Double.parseDouble(value1));
                    case "AirFlow" ->assuranceFullObject.setAirFlow(Double.parseDouble(value1));
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
                    case "MaintenanceInterval" -> assuranceFullObject.setMaintenanceInterval(Double.parseDouble(value1));
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
