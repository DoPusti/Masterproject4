package com.example.Masterproject4.Handler;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.ProduktAnforderung.RessourceHolder;
import com.example.Masterproject4.ProduktAnforderung.StateOfStability;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    /*
    public void searchForAxe(List<RessourceHolder> ressourceHolderIn, List<AssuranceFullObject> assuranceListIn) {
        RessourceHolder matchedRessourceholder = new RessourceHolder();
        AssuranceFullObject constraints = new AssuranceFullObject();
        for (RessourceHolder ressourceHolder : ressourceHolderIn) {
            constraints = compareRessourceAssurance(ressourceHolder, assuranceListIn, false);
            if (!(constraints.getAssetId() == null)) {
                matchedRessourceholder = ressourceHolder;
                ressourceHolder.setGripper(constraints);
                break;
            }
        }
        Log.info(matchedRessourceholder.toString());
        Log.info(constraints.toString());
    }

     */

/*
    public void searchForKinematicChain(List<RessourceHolder> ressourceHolderIn, List<AssuranceFullObject> assuranceListIn) {


        boolean foundMinimumOfOneAssurance = false;
        // Schleife über alle Sequenzen des Satzes
        for (RessourceHolder ressourceHolder : ressourceHolderIn) {
            if (!foundMinimumOfOneAssurance) {
                Log.info("Gelesene Sequenz:");
                Log.info(ressourceHolder.getStringSequence());
                for (AssuranceFullObject assurance : assuranceListIn) {
                    if (assurance.getConnectionType().equals("NotAutomaticallyRemovable")) {
                        if (checkMatchingAssurance(ressourceHolder, assurance)) {
                            foundMinimumOfOneAssurance = true;
                        }
                        ;
                    }
                }
                Log.info("Ausgabe der gesamten Ressourholer:");
                ressourceHolderIn.forEach(objekt -> {
                    String sequence = objekt.getStringSequence();
                    Log.info(sequence);
                });
                // Mindestens ein Eintrag wurde gefunden, der passen könnte
                if (!(ressourceHolder.getKinematicChainList() == null)) {
                    List<KinematicChain> kinematicChainList = ressourceHolder.getKinematicChainList();
                    kinematicChainList.forEach(kinematicChain -> {
                        Log.info("Prüfung der kinematischen Kette vor dem Ranking:");
                        Log.info(String.valueOf(kinematicChain));
                        if (!kinematicChain.isForceX()) {
                            kinematicChain.setRankingForceX(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "ForceX"));
                        }
                        if (!kinematicChain.isForceY()) {
                            kinematicChain.setRankingForceY(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "ForceY"));
                        }
                        if (!kinematicChain.isForceZ()) {
                            kinematicChain.setRankingForceZ(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "ForceZ"));
                        }
                        if (!kinematicChain.isPositionX()) {
                            kinematicChain.setRankingPositionX(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "PositionX"));
                        }
                        if (!kinematicChain.isPositionY()) {
                            kinematicChain.setRankingPositionY(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "PositionY"));
                        }
                        if (!kinematicChain.isPositionZ()) {
                            kinematicChain.setRankingPositionZ(checkRankingOfConstraint(kinematicChain.getAssurance(), ressourceHolderIn, "PositionZ"));
                        }
                        Log.info("Prüfung der kinematischen Kette nach dem Ranking:");
                        Log.info(String.valueOf(kinematicChain));
                    });
                }


            } else {
                break;
            }
        }


    }

 */

    /*
    public boolean checkMatchingAssurance(RessourceHolder ressourceIn, AssuranceFullObject assuranceIn) {
        boolean foundAssurance = false;
        List<KinematicChain> kinematicChainList;
        if (ressourceIn.getKinematicChainList() == null) {
            kinematicChainList = new ArrayList<>();
        } else {
            kinematicChainList = ressourceIn.getKinematicChainList();
        }
        KinematicChain kinematicChain = new KinematicChain();
        List<Boolean> sequences = new ArrayList<>();
        int constraintCounter = 0;
        int requiredStateChangeCounter = 0;
        double price = assuranceIn.getPrice();

        if (ressourceIn.getForceX().getValue() <= assuranceIn.getForceX()) {
            constraintCounter++;
            kinematicChain.setForceX(true);
            sequences.add(true);
        } else {
            kinematicChain.setForceX(false);
            sequences.add(false);
        }
        if (ressourceIn.getForceY().getValue() <= assuranceIn.getForceY()) {
            constraintCounter++;
            kinematicChain.setForceY(true);
            sequences.add(true);
        } else {
            kinematicChain.setForceY(false);
            sequences.add(false);
        }

        if (ressourceIn.getForceZ().getValue() <= assuranceIn.getForceZ()) {
            constraintCounter++;
            kinematicChain.setForceZ(true);
            sequences.add(true);
        } else {
            kinematicChain.setForceZ(false);
            sequences.add(false);
        }
        if (ressourceIn.getPositionX().getValue() <= assuranceIn.getPositionX()) {
            requiredStateChangeCounter++;
            kinematicChain.setPositionX(true);
            sequences.add(true);
        } else {
            kinematicChain.setPositionX(false);
            sequences.add(false);
        }
        if (ressourceIn.getPositionY().getValue() <= assuranceIn.getPositionY()) {
            requiredStateChangeCounter++;
            kinematicChain.setPositionY(true);
            sequences.add(true);
        } else {
            kinematicChain.setPositionY(false);
            sequences.add(false);
        }
        if (ressourceIn.getPositionZ().getValue() <= assuranceIn.getPositionZ()) {
            requiredStateChangeCounter++;
            kinematicChain.setPositionZ(true);
            sequences.add(true);
        } else {
            kinematicChain.setPositionZ(false);
            sequences.add(false);
        }


        if ((requiredStateChangeCounter + constraintCounter >= 3) && requiredStateChangeCounter >= 1) {
            kinematicChain.setSequences(sequences);
            kinematicChain.setAssurance(assuranceIn);
            kinematicChain.setId(assuranceIn.getId());
            kinematicChain.setPrice(price);
            kinematicChainList.add(kinematicChain);
            ressourceIn.setKinematicChainList(kinematicChainList);
            Log.info("Gefundene Zusicherung");
            foundAssurance = true;
            List<Boolean> booleanList = kinematicChain.getSequences();
            Log.info("Id=" + kinematicChain.getId()
                    + " | " + "forceX=" + assuranceIn.getForceX() + "/" + booleanList.get(0)
                    + " | " + "forceY=" + assuranceIn.getForceY() + "/" + booleanList.get(1)
                    + " | " + "forceZ=" + assuranceIn.getForceZ() + "/" + booleanList.get(2)
                    + " | " + "positionX=" + assuranceIn.getPositionX() + "/" + booleanList.get(3)
                    + " | " + "positionY=" + assuranceIn.getPositionY() + "/" + booleanList.get(4)
                    + " | " + "positionZ=" + assuranceIn.getPositionZ() + "/" + booleanList.get(5));

        }
        return foundAssurance;
    }

    public int checkRankingOfConstraint(AssuranceFullObject assuranceIn, List<RessourceHolder> ressourceHolderIn, String attributeName) {
        Log.info("Suche Ranking für  " + attributeName);
        int rankingCounter = 0;
        boolean foundRanking = false;
        for (RessourceHolder ressourceHolder : ressourceHolderIn) {
            if (!foundRanking) {
                rankingCounter++;
                switch (attributeName) {
                    case "ForceX":
                        Log.info("ForceX geprüft");
                        if (assuranceIn.getForceX() >= ressourceHolder.getForceX().getValue()) {
                            Log.info("Wert erreicht ForceX");
                            foundRanking = true;
                            break;
                        }
                        break;
                    case "ForceY":
                        Log.info("ForceY geprüft");
                        if (assuranceIn.getForceY() >= ressourceHolder.getForceX().getValue()) {
                            Log.info("Wert erreicht ForceY");
                            foundRanking = true;
                            break;
                        }
                        break;
                    case "ForceZ":
                        Log.info("ForceZ geprüft");
                        if (assuranceIn.getForceZ() >= ressourceHolder.getForceX().getValue()) {
                            Log.info("Wert erreicht ForceZ");
                            foundRanking = true;
                            break;
                        }
                        break;
                    case "PositionX":
                        if (assuranceIn.getPositionX() >= ressourceHolder.getPositionX().getValue()) {
                            Log.info("Wert erreicht PositionX");
                            foundRanking = true;
                            break;
                        }
                        break;
                    case "PositionY":
                        if (assuranceIn.getPositionY() >= ressourceHolder.getPositionY().getValue()) {
                            Log.info("Wert erreicht PositionY");
                            foundRanking = true;
                            break;
                        }
                        break;
                    case "PositionZ":
                        if (assuranceIn.getPositionZ() >= ressourceHolder.getPositionZ().getValue()) {
                            Log.info("Wert erreicht PositionZ");
                            foundRanking = true;
                            break;
                        }
                        break;
                    default:
                }
            }

        }

        return rankingCounter;
    }

     */

    public void checkKinematicChain(List<RessourceHolder> ressourceIn,
                                    List<AssuranceFullObject> assuranceIn,
                                    List<StateOfStability> stateOfStabilityIn,
                                    HashMap<String,String> listOfMatchingAttributesIn
    ) throws IllegalAccessException, NoSuchFieldException {

        System.out.println("Liste der relevanten zu prüfenden Zeilen");

        for (RessourceHolder ressource : ressourceIn) {
            System.out.println(ressource.getStringSequence());
            if (findMatchinAssuranceTestOne(ressource, assuranceIn, listOfMatchingAttributesIn)) {
                System.out.println("Minimum von einer Zusicherung erreicht");
                break;
            }
        }
    }

    public Boolean findMatchinAssuranceTestOne(RessourceHolder ressource,
                                               List<AssuranceFullObject> assuranceIn,
                                               HashMap<String,String> listOfMatchingAttributesIn) throws IllegalAccessException, NoSuchFieldException {


        // Jede Zusicherung wird geprüft
        for(AssuranceFullObject assurance : assuranceIn) {

            // Alle Attribute der Zusicherung entnehmen und prüfen, ob diese relevant sind
            System.out.println("Prüfung der Zusicherung " + assurance.getId());

            Class<?> clazz = assurance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                System.out.println("Attributname: " + fieldName);
            }
            /*
            Field nameOfAssuranceAttribute = assurance.getClass().getDeclaredField(nameOfMatchingAttribute);
            nameOfAssuranceAttribute.setAccessible(true);
            double valueOfAssuranceAttribute = (double) nameOfAssuranceAttribute.get(assurance);
            if(valueOfAssuranceAttribute>=valueOfRessourceAttribute.getValue()) {
                System.out.println("Passender Wert wurde gefunden");
                System.out.println("Name des Attributs " + nameOfMatchingAttribute);
                System.out.println("Wert der Ressource ist " + valueOfRessourceAttribute.getValue());
                System.out.println("Wert der Zusicherung ist " + valueOfAssuranceAttribute);
            } else {
                System.out.println("Wert passt leider nicht");
                System.out.println("Wert der Ressource ist " + valueOfRessourceAttribute.getValue());
                System.out.println("Wert der Zusicherung ist " + valueOfAssuranceAttribute);
            }

             */
        }

        /*
        for (Map.Entry<String, String> attribute : listOfMatchingAttributesIn) {
            System.out.println("Prüfung des Attributs " + attribute.getValue() + "|" + attribute.getKey());
            String nameOfMatchingAttribute = attribute.getKey();
            String valueOfMatchingAttribute = attribute.getValue();
            Field nameOfRessourceAttribute = ressource.getClass().getDeclaredField(nameOfMatchingAttribute);
            nameOfRessourceAttribute.setAccessible(true);

            AbstractMap.SimpleEntry<Integer, Double> valueOfRessourceAttribute = (AbstractMap.SimpleEntry<Integer, Double>) nameOfRessourceAttribute.get(ressource);


        }

         */



        return true;
    }

}
