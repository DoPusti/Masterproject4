package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.JAXBModels.Property;
import com.example.Masterproject4.JAXBModels.SubModel;
import com.example.Masterproject4.JAXBModels.SubModelElement;
import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.Zusicherung.AssuranceFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class XMLStructureFullObjectTest {

    AssuranceFullObject assuranceFullObject = new AssuranceFullObject();

    @Test
    public void fillProductRequirement() throws JAXBException {
        List<Part> parts = new ArrayList<>();


        List<Teilvorgang> teilVorgang = new ArrayList<>();

        ProductRequirementFullObject productRequirementFullObject = new ProductRequirementFullObject();

        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        // Loop über alle Submodelle des AASX-Files
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
        System.out.println("---------------------------------------------");
        System.out.println("Infos zum Objekt ProductRequirementFullObject");
        System.out.println(productRequirementFullObject.getAssetId());
        System.out.println(productRequirementFullObject.getPart());
        System.out.println(productRequirementFullObject.getPart().size());
        System.out.println(productRequirementFullObject.getTeilVorgang());
        System.out.println(productRequirementFullObject.getTeilVorgang().size());

    }

    @Test
    void fillAssurances() throws JAXBException {


        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ABBScaraIRB920T_Axis07.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLStructure.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        XMLStructure XMLStructure = (XMLStructure) unmarshaller.unmarshal(file);

        // Loop über alle Submodelle des AASX-Files
        List<SubModel> listOfAllSubmodels = XMLStructure.getSubmodels().getSubmodel();
        listOfAllSubmodels.forEach(subModelObject -> {
            List<SubModelElement> listOfAllSubmodelsElements = subModelObject.getSubmodelElements().getSubmodelElement();
            switch (subModelObject.getIdShort()) {
                case "Identification":
                    System.out.println("Unterprozedur Fill Identification wird aufgerufen");
                    fillSubModelIdentification(listOfAllSubmodelsElements);
                    break;
                case "Assurances":
                    System.out.println("Unterprozedur Fill Assurances wird aufgerufen");
                    fillSubModelAssurances(listOfAllSubmodelsElements);
                    listOfAllSubmodelsElements.forEach(subModelElementsAssurances -> {
                        List<SubModelElement> SMEsinSMEInAssurances = subModelElementsAssurances.getSubmodelElementCollection().getValue().getSubmodelElement();
                        SMEsinSMEInAssurances.forEach(SMEinSMEinAssurance -> {
                            //Manche haben direkt Property, andere zuerst wieder eine SEC
                            Property propertyInAssurance = SMEinSMEinAssurance.getProperty();
                            if (propertyInAssurance == null) {
                                List<SubModelElement> SMesinSMEsInSMEinAssurances = SMEinSMEinAssurance.getSubmodelElementCollection().getValue().getSubmodelElement();
                                SMesinSMEsInSMEinAssurances.forEach(SMesinSMEsInSMEinAssurancesObject -> {
                                    Property propertyInSMEInAssurance = SMesinSMEsInSMEinAssurancesObject.getProperty();
                                    if (propertyInSMEInAssurance == null) {
                                        // RangeObjekte (z.B. Force in Constraints)
                                        switch (SMesinSMEsInSMEinAssurancesObject.getRange().getIdShort()) {
                                            case "X":
                                                assuranceFullObject.setXForceMin(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMin()));
                                                assuranceFullObject.setXForceMax(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMax()));
                                                System.out.println("X mit MIN und MAX eingefügt");
                                            case "Y":
                                                assuranceFullObject.setYForceMin(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMin()));
                                                assuranceFullObject.setYForceMax(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMax()));
                                                System.out.println("Y mit MIN und MAX eingefügt");
                                            case "Z":
                                                assuranceFullObject.setZForceMin(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMin()));
                                                assuranceFullObject.setZForceMax(Double.parseDouble(SMesinSMEsInSMEinAssurancesObject.getRange().getMax()));
                                                System.out.println("Z mit MIN und MAX eingefügt");
                                        }

                                    } else {
                                        // Properties im ersten SCM
                                        switch (propertyInSMEInAssurance.getIdShort()) {
                                            case "Length":
                                                assuranceFullObject.setMass(Double.parseDouble(propertyInSMEInAssurance.getValue()));
                                                System.out.println("Mass eingefügt");
                                            case "Width":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");
                                            case "Height":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");
                                            case "X":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");
                                            case "Y":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");
                                            case "Z":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");
                                            case "Parallelgripper", "Magneticgripper", "Vaccumgripper":
                                                if (propertyInSMEInAssurance.getValue() != null) {
                                                    assuranceFullObject.setOperatingPrinciple(propertyInSMEInAssurance.getIdShort());
                                                    System.out.println("Operating Principle eingefügt");
                                                }
                                            case "Position":
                                                assuranceFullObject.setConnectionType(propertyInSMEInAssurance.getValue());
                                                System.out.println("ConnectionType eingefügt");

                                        }
                                    }


                                });
                            } else {
                                // Direkte Properties
                                switch (propertyInAssurance.getIdShort()) {
                                    case "Mass":
                                        assuranceFullObject.setMass(Double.parseDouble(propertyInAssurance.getValue()));
                                        System.out.println("Mass eingefügt");
                                    case "ConnectionType":
                                        assuranceFullObject.setConnectionType(propertyInAssurance.getValue());
                                        System.out.println("ConnectionType eingefügt");
                                }
                            }
                        });

                    });
                    break;
                /*
                case "MediaSupply":
                    listOfAllSubmodelsElements.forEach(subModelElements -> {
                        Property propertyMediaSupply = subModelElements.getProperty();
                        switch (propertyMediaSupply.getIdShort()) {
                            case "OperatingCurrent":
                                assuranceFullObject.setOperatingCurrent(Double.parseDouble(propertyMediaSupply.getValue()));
                            case "OperatingVoltage":
                                assuranceFullObject.setOperatingVoltage(Double.parseDouble(propertyMediaSupply.getValue()));
                            case "CompressedAirPressure":
                                assuranceFullObject.setCompressedAirPressure(Double.parseDouble(propertyMediaSupply.getValue()));
                            case "AirFlow":
                                assuranceFullObject.setAirFlow(Double.parseDouble(propertyMediaSupply.getValue()));
                        }
                    });
                    break;
                case "EnvironmentalConditions":
                    listOfAllSubmodelsElements.forEach(subModelElements -> {
                        Property propertyEnvironmentalConditions = subModelElements.getProperty();
                        switch (propertyEnvironmentalConditions.getIdShort()) {
                            case "Temperature":
                                assuranceFullObject.setTemperature(Double.parseDouble(propertyEnvironmentalConditions.getValue()));
                            case "Pressure":
                                assuranceFullObject.setPressure(Double.parseDouble(propertyEnvironmentalConditions.getValue()));
                            case "Humidity":
                                assuranceFullObject.setHumidity(Double.parseDouble(propertyEnvironmentalConditions.getValue()));
                            case "Purity":
                                assuranceFullObject.setPurity(Boolean.parseBoolean(propertyEnvironmentalConditions.getValue()));
                            case "FoodGrade":
                                assuranceFullObject.setFoodGrade(Integer.parseInt(propertyEnvironmentalConditions.getValue()));
                            case "Explosiveness":
                                assuranceFullObject.setExplosiveness(Integer.parseInt(propertyEnvironmentalConditions.getValue()));
                        }
                    });
                    break;
                case "EconomicFactors":
                    listOfAllSubmodelsElements.forEach(subModelElements -> {
                        Property propertyEconomicFactors = subModelElements.getProperty();
                        if (propertyEconomicFactors == null) {
                            List<SubModelElement> subModelElementsEF = subModelElements.getSubmodelElementCollection().getValue().getSubmodelElement();
                            subModelElementsEF.forEach(subModelSpaceRequirement -> {
                                // SM = Economic Factors -> SMC  = Space Requirment
                                Property  propertySpaceRequirments = subModelSpaceRequirement.getProperty();
                                switch (propertyEconomicFactors.getIdShort()) {
                                    case "Length":
                                        assuranceFullObject.setLengthSR(Double.parseDouble(propertyEconomicFactors.getValue()));
                                    case "Width":
                                        assuranceFullObject.setWidthSR(Integer.parseInt(propertyEconomicFactors.getValue()));
                                    case "Height":
                                        assuranceFullObject.setHeight(Integer.parseInt(propertyEconomicFactors.getValue()));
                                }
                            });
                        } else {
                            switch (propertyEconomicFactors.getIdShort()) {
                                case "Price":
                                    assuranceFullObject.setPrice(Double.parseDouble(propertyEconomicFactors.getValue()));
                                case "Lengthofusage":
                                    assuranceFullObject.setLengthOfUsage(Integer.parseInt(propertyEconomicFactors.getValue()));
                                case "MaintenanceInterval":
                                    assuranceFullObject.setMaintenanceInterval(Integer.parseInt(propertyEconomicFactors.getValue()));
                                case "MaintanceDuration":
                                    assuranceFullObject.setMaintanceDuration(Integer.parseInt(propertyEconomicFactors.getValue()));
                                case "DeliveryTime":
                                    assuranceFullObject.setDeliveryTime(Integer.parseInt(propertyEconomicFactors.getValue()));
                                case "OneTimeLicenceCost":
                                    assuranceFullObject.setOneTimeLicenceCost(Integer.parseInt(propertyEconomicFactors.getValue()));
                                case "MonthlyLicenceCost":
                                    assuranceFullObject.setMonthlyLicenceCost(Integer.parseInt(propertyEconomicFactors.getValue()));
                            }

                        }

                    });
                    break;

                     */
            }
        });
        System.out.println("------------------");
        System.out.println("Informationen zum Objekt");
        System.out.println(assuranceFullObject);


    }


    // Submodel Identification
    private void fillSubModelIdentification(List<SubModelElement> subModelElements) {
        subModelElements.forEach(subModelElementsInAssurance -> {
            Property propertyIdentification = subModelElementsInAssurance.getProperty();
            switch (propertyIdentification.getIdShort()) {
                case "AssetId":
                    assuranceFullObject.setAssetId(propertyIdentification.getValue());
                    System.out.println(propertyIdentification.getIdShort() + " eingefügt");
                    break;
                case "SerialNumber":
                    assuranceFullObject.setSerialNumber(propertyIdentification.getValue());
                    System.out.println(propertyIdentification.getIdShort() + " eingefügt");
                    break;
                case "URL":
                    assuranceFullObject.setUrl(propertyIdentification.getValue());
                    System.out.println(propertyIdentification.getIdShort() + " eingefügt");
                    break;
            }
        });

    }

    // Submodel Assurance
    private void fillSubModelAssurances(List<SubModelElement> listOfAllSubmodelsElements) {
        listOfAllSubmodelsElements.forEach(subModelElementsInAssurance -> {
            // Alle direkten Properties von SMC prüfen
            Property property1 = subModelElementsInAssurance.getProperty();
            if (property1 != null) {
                fillValueInList(property1.getIdShort(), property1.getValue(), "", subModelElementsInAssurance.getSubmodelElementCollection().getIdShort());
            } else {

            }
            System.out.println(subModelElementsInAssurance.getSubmodelElementCollection().getIdShort());
            System.out.println(subModelElementsInAssurance.getSubmodelElementCollection());
        });
    }

    private void fillValueInList(String idShort, String value1, String value2, String SECIdshort) {
        switch (SECIdshort) {
            // Identification
            case "Identification":
                switch (value1) {
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
                    case  "OperatingVoltage" -> assuranceFullObject.setOperatingVoltage(Double.parseDouble(value1));
                    case  "CompressedAirPressure" -> assuranceFullObject.setCompressedAirPressure(Double.parseDouble(value1));
                }
            // Environmental Conditions
            case "EnvironmentalConditions":
                switch (idShort) {
                    case "Temperature" -> assuranceFullObject.setTemperature(Double.parseDouble(value1));
                    case "Pressure" -> assuranceFullObject.setPressure(Double.parseDouble(value1));
                    case "Humidity" -> assuranceFullObject.setHumidity(Double.parseDouble(value1));
                    case "Purity" -> assuranceFullObject.setPurity(Boolean.parseBoolean(value1));
                    case  "FoodGrade" -> assuranceFullObject.setFoodGrade(Integer.parseInt(value1));
                    case  "Explosiveness" -> assuranceFullObject.setExplosiveness(Integer.parseInt(value1));
                }
            // EconomicFactors
            case "EconomicFactors":
                switch (idShort) {
                    case "Price" -> assuranceFullObject.setPrice(Double.parseDouble(value1));
                    case "Lengthofusage" -> assuranceFullObject.setLengthOfUsage(Integer.parseInt(value1));
                    case "MaintenanceInterval" -> assuranceFullObject.setMaintenanceInterval(Integer.parseInt(value1));
                    case "MaintanceDuration" -> assuranceFullObject.setMaintanceDuration(Integer.parseInt(value1));
                    case "DeliveryTime" -> assuranceFullObject.setDeliveryTime(Integer.parseInt(value1));
                    case "OneTimeLicenceCost" -> assuranceFullObject.setOneTimeLicenceCost(Double.parseDouble(value1));
                    case "MonthlyLicenceCost" -> assuranceFullObject.setMonthlyLicenceCost(Double.parseDouble(value1));
                }
            case "SpaceRequirement":
                switch (idShort) {
                    case "Length" : assuranceFullObject.setLengthSR(Double.parseDouble(value1));
                    case "Width" : assuranceFullObject.setWidthSR(Double.parseDouble(value1));
                    case "Height" : assuranceFullObject.setHeightSR(Double.parseDouble(value1));
                }


        }
    }

    ;

}