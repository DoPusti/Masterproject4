package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.JAXBModels.*;
import com.example.Masterproject4.Repository.AssuranceRepository;
import com.example.Masterproject4.Repository.ProductPropertyRepository;
import com.example.Masterproject4.Zusicherung.AssuranceFullObject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest
class XMLStructureFullObjectTest {

    AssuranceFullObject assuranceFullObject = new AssuranceFullObject();
    @Autowired
    private AssuranceRepository assuranceRepository;

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
                case "Identification" : {
                    Property propertyForIdentification = subModelObject.getSubmodelElements().getSubmodelElement().get(0).getProperty();
                    System.out.println("IdShort : " + propertyForIdentification.getIdShort());
                    System.out.println("Value   : " + propertyForIdentification.getValue());
                    productRequirementFullObject.setAssetId(propertyForIdentification.getValue());
                    break;
                }
                case "ProductPropertyOverall" : {
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
                    }); break;
                }
                case "ProcessRequirement" : {
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

                    }); break;
                }
                // Zur Liste hinzufügen

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


        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\ABBScaraIRB920T_Axis07_2.xml");

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

                case "Assurances" ->
                    fillSubModelAssurances(listOfAllSubmodelsElements);
                /*
                case "MediaSupply" ->
                    fillSubModelMediaSupply(listOfAllSubmodelsElements,subModelObject.getIdShort());
                case "EnvironmentalConditions" ->
                    fillSubModelEnvironmentConditions(listOfAllSubmodelsElements,subModelObject.getIdShort());

                case "EconomicFactors" ->
                        fillSubModelEconomicFactors(listOfAllSubmodelsElements,subModelObject.getIdShort());

                 */

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
            //TODO Mit Patrick die Befüllung besprechen
            if(!Objects.equals(idShortOfSMC, "InternalPropertyRelations")) {
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

            }


        });
        System.out.println("Liste für Assurances gefüllt");
    }

    // Submodel MediaSupply
    private void fillSubModelMediaSupply(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(),property1.getValue(),"",SECIdShort);
        });
        System.out.println("Liste für MediaSupply gefüllt");
    }

    // Submodel EnvironmentConditions
    private void fillSubModelEnvironmentConditions(List<SubModelElement> subModelElements, String SECIdShort) {
        subModelElements.forEach(subModelElementObject1 -> {
            Property property1 = subModelElementObject1.getProperty();
            fillValueInList(property1.getIdShort(),property1.getValue(),"",SECIdShort);
        });
        System.out.println("Liste für EnvironmentConditions gefüllt");
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
        System.out.println("Liste für EconomicFactors gefüllt");

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