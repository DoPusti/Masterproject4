package com.example.Masterproject4.Handler;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.CombinedRessources.KinematicChain;
import com.example.Masterproject4.CombinedRessources.KinematicChainProperties;
import com.example.Masterproject4.XMLAttributeHolder.RessourceHolder;
import com.example.Masterproject4.CombinedRessources.StateOfStability;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;


@Component
@Data
@Builder
public class RessourceChecker {

    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker.class);

    public void callRestService(List<Constraints> matchedAssurances) {
        matchedAssurances.forEach(assurance -> {
            if (!(assurance.getRestApi() == null)) {
                String restCall;
                restCall = assurance.getRestApi() + "/getPayload?AssetId=" + assurance.getIdShort();
                Log.info("RestCall " + restCall);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> call = restTemplate.getForEntity(restCall, String.class);
                Log.info(call.getBody());
            }
        });

    }

    public void searchForGripper(List<RessourceHolder> ressourceHolderIn, List<AssuranceFullObject> assuranceListIn) {
        RessourceHolder matchedRessourceholder = new RessourceHolder();
        AssuranceFullObject constraints = new AssuranceFullObject();
        for (RessourceHolder ressourceHolder : ressourceHolderIn) {
            constraints = compareRessourceAssurance(ressourceHolder, assuranceListIn, true);
            if (!(constraints.getAssetId() == null)) {
                matchedRessourceholder = ressourceHolder;
                ressourceHolder.setGripper(constraints);
                break;
            }
        }
        Log.info(matchedRessourceholder.toString());
        Log.info(constraints.toString());
    }

    public AssuranceFullObject compareRessourceAssurance(RessourceHolder ressourceHolderIn, List<AssuranceFullObject> assuranceListIn, Boolean gripper) {
        AssuranceFullObject constraintsOut = new AssuranceFullObject();
        for (AssuranceFullObject constraint : assuranceListIn) {
            if (gripper) {
                if (constraint.getConnectionType().equals("AutomaticallyRemoveable")) {
                    if (constraint.getForceX() >= ressourceHolderIn.getForceX().getValue()
                            && constraint.getForceY() >= ressourceHolderIn.getForceY().getValue()
                            && constraint.getForceZ() >= ressourceHolderIn.getForceZ().getValue()
                            && constraint.getTorqueX() >= ressourceHolderIn.getTorqueX().getValue()
                            && constraint.getTorqueY() >= ressourceHolderIn.getTorqueY().getValue()
                            && constraint.getTorqueZ() >= ressourceHolderIn.getTorqueZ().getValue()
                            && constraint.getPositionRepetitionAccuracyX() >= ressourceHolderIn.getPositionRepetitionAccuracyX().getValue()
                            && constraint.getPositionRepetitionAccuracyY() >= ressourceHolderIn.getPositionRepetitionAccuracyY().getValue()
                            && constraint.getPositionRepetitionAccuracyZ() >= ressourceHolderIn.getPositionRepetitionAccuracyZ().getValue()
                            && constraint.getRotationRepetitionAccuracyX() >= ressourceHolderIn.getRotationRepetitionAccuracyX().getValue()
                            && constraint.getRotationRepetitionAccuracyY() >= ressourceHolderIn.getRotationRepetitionAccuracyY().getValue()
                            && constraint.getRotationRepetitionAccuracyZ() >= ressourceHolderIn.getRotationRepetitionAccuracyZ().getValue()) {
                        if (constraintsOut.getAssetId() == null || constraint.getPrice() < constraintsOut.getPrice()) {
                            // Log.info("constraintsOut " + constraintsOut);
                            // Log.info("constraint " + constraint);
                            constraintsOut = constraint;
                            // Log.info("ConstraintsOut nach Belegung  " + constraintsOut);

                        }
                    }
                }
            } else {
                if (constraint.getConnectionType().equals("NotAutomaticallyRemoveable")) {
                    if (constraint.getForceX() >= ressourceHolderIn.getForceX().getValue()
                            && constraint.getForceY() >= ressourceHolderIn.getForceY().getValue()
                            && constraint.getForceZ() >= ressourceHolderIn.getForceZ().getValue()
                            && constraint.getTorqueX() >= ressourceHolderIn.getTorqueX().getValue()
                            && constraint.getTorqueY() >= ressourceHolderIn.getTorqueY().getValue()
                            && constraint.getTorqueZ() >= ressourceHolderIn.getTorqueZ().getValue()
                            && constraint.getPositionRepetitionAccuracyX() >= ressourceHolderIn.getPositionRepetitionAccuracyX().getValue()
                            && constraint.getPositionRepetitionAccuracyY() >= ressourceHolderIn.getPositionRepetitionAccuracyY().getValue()
                            && constraint.getPositionRepetitionAccuracyZ() >= ressourceHolderIn.getPositionRepetitionAccuracyZ().getValue()
                            && constraint.getRotationRepetitionAccuracyX() >= ressourceHolderIn.getRotationRepetitionAccuracyX().getValue()
                            && constraint.getRotationRepetitionAccuracyY() >= ressourceHolderIn.getRotationRepetitionAccuracyY().getValue()
                            && constraint.getRotationRepetitionAccuracyZ() >= ressourceHolderIn.getRotationRepetitionAccuracyZ().getValue()
                            && constraint.getMass() >= ressourceHolderIn.getMass()
                            && constraint.getCenterOfMassX() >= ressourceHolderIn.getCenterOfMassX()
                            && constraint.getCenterOfMassY() >= ressourceHolderIn.getCenterOfMassY()
                            && constraint.getCenterOfMassZ() >= ressourceHolderIn.getCenterOfMassZ()
                            && constraint.getHeight() >= ressourceHolderIn.getHeight()
                            && constraint.getWidth() >= ressourceHolderIn.getWidth()
                            && constraint.getLength() >= ressourceHolderIn.getLength()
                            && constraint.getPositionX() >= ressourceHolderIn.getPositionX().getValue()
                            && constraint.getPositionY() >= ressourceHolderIn.getPositionY().getValue()
                            && constraint.getPositionZ() >= ressourceHolderIn.getPositionZ().getValue()
                            && constraint.getRotationX() >= ressourceHolderIn.getRotationX().getValue()
                            && constraint.getRotationY() >= ressourceHolderIn.getRotationY().getValue()
                            && constraint.getRotationZ() >= ressourceHolderIn.getRotationZ().getValue()

                    ) {
                        if (constraintsOut.getAssetId() == null || constraint.getPrice() < constraintsOut.getPrice()) {
                            // Log.info("constraintsOut " + constraintsOut);
                            // Log.info("constraint " + constraint);
                            constraintsOut = constraint;
                            // Log.info("ConstraintsOut nach Belegung  " + constraintsOut);

                        }
                    }
                }

            }

        }
        return constraintsOut;
    }

     public void checkKinematicChain(List<RessourceHolder> ressourceIn,
                                    List<AssuranceFullObject> assuranceIn,
                                    List<StateOfStability> stateOfStabilityIn,
                                    HashMap<String, String> listOfMatchingAttributesIn
    ) throws IllegalAccessException, NoSuchFieldException {

        System.out.println("Liste der relevanten zu prüfenden Zeilen");

        for (RessourceHolder ressource : ressourceIn) {
            System.out.println(ressource.getStringSequence());
            if (findMatchinAssuranceTestOne(ressource, assuranceIn, listOfMatchingAttributesIn)) {
                System.out.println("Minimum von einer Zusicherung erreicht");
                checkRemainingMatchingAttributes(ressource,assuranceIn,stateOfStabilityIn);
                break;
            }
        }
    }

    public Boolean findMatchinAssuranceTestOne(RessourceHolder ressource,
                                               List<AssuranceFullObject> assuranceIn,
                                               HashMap<String, String> listOfMatchingAttributesIn) throws IllegalAccessException, NoSuchFieldException {

        Boolean minimumOfOneMatchingAssuranceFound = false;


        List<KinematicChain> kinematicChainList = new ArrayList<>();

       //Jede Zusicherung wird geprüft
        for (AssuranceFullObject assurance : assuranceIn) {
            int counterOfMatchingAttributes = listOfMatchingAttributesIn.size();
            int counterOfPersistentStateChange = 0;
            int counterOfConstraints = 0;
            if (assurance.getConnectionType().equals("NotAutomaticallyRemovable")) {


                KinematicChain kinematicChain = new KinematicChain();
                Map<String, KinematicChainProperties> propertyOfAttribute = new HashMap<>();
                kinematicChain.setAssurance(assurance);
                kinematicChain.setPrice(assurance.getPrice());
                kinematicChain.setId(assurance.getId());

                // Alle Attribute der Zusicherung entnehmen und prüfen, ob diese relevant sind
                System.out.println("Prüfung der Zusicherung " + assurance.getId());

                Class<?> clazz = assurance.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();

                    KinematicChainProperties kinematicChainProperty = new KinematicChainProperties();

                    // Attribut der Zusicherung ist relevant für die Prüfung
                    if (listOfMatchingAttributesIn.containsKey(fieldName)) {
                        String kindOfAttribute = listOfMatchingAttributesIn.get(fieldName);
                        //System.out.println("Attributname: " + fieldName + " ist relevant und ist ein Attribut von " + kindOfAttribute);
                        field.setAccessible(true);
                        double valueOfAssuranceAttribute = (double) field.get(assurance);

                        // Wert des Attributs der Ressource holen und dann mit Zusicherung vergleichen
                        Field nameOfRessourceAttribute = ressource.getClass().getDeclaredField(fieldName);
                        nameOfRessourceAttribute.setAccessible(true);
                        AbstractMap.SimpleEntry<Integer, Double> valueOfRessourceAttribute = (AbstractMap.SimpleEntry<Integer, Double>) nameOfRessourceAttribute.get(ressource);

                        kinematicChainProperty.setRanking(0);
                        kinematicChainProperty.setSubprocess(valueOfRessourceAttribute.getKey());
                        kinematicChainProperty.setValueOfAttribute(valueOfAssuranceAttribute);
                        kinematicChainProperty.setKindOfAttribute(kindOfAttribute);

                        if (valueOfRessourceAttribute.getValue() <= valueOfAssuranceAttribute) {
                            /*
                            System.out.println("Attribut gefunden, das passt");
                            System.out.println("Name des Attributs " + fieldName);
                            System.out.println("Wert Zusicherung " + valueOfAssuranceAttribute);
                            System.out.println("Wert Ressource " + valueOfRessourceAttribute);
                             */
                            kinematicChainProperty.setValueIsHigherOrEqualToRessource(true);
                            if (kindOfAttribute.equals("PersistentStateChange")) {
                                counterOfPersistentStateChange++;
                            } else {
                                counterOfConstraints++;
                            }
                        } else {
                            kinematicChainProperty.setValueIsHigherOrEqualToRessource(false);

                        }
                        propertyOfAttribute.put(fieldName, kinematicChainProperty);


                    }

                }
                if ((counterOfConstraints + counterOfPersistentStateChange >= counterOfMatchingAttributes / 2) && counterOfPersistentStateChange > 1) {
                    System.out.println("Relevante Zusicherung gefunden");
                    System.out.println("ID = " + assurance.getId());
                    System.out.println("CounterConstraints = " + counterOfConstraints);
                    System.out.println("CounterPersistentStateChange = " + counterOfPersistentStateChange);
                    kinematicChain.setPropertiesOfAttributes(propertyOfAttribute);
                    kinematicChainList.add(kinematicChain);
                    //
                  }

            }

        }
        if(kinematicChainList.size()>0) {
            minimumOfOneMatchingAssuranceFound = true;
        }
        ressource.setKinematicChainList(kinematicChainList);
        System.out.println(ressource.InformationForKinematicChainList());

        return minimumOfOneMatchingAssuranceFound;
    }

    public void checkRemainingMatchingAttributes(RessourceHolder ressourceIn, List<AssuranceFullObject> assuranceIn, List<StateOfStability> stateOfStabilityIn) {
        //Prüfung der vorhandenen combinierten Ressource und Suche nach der nächsten Ressource




    }

}
