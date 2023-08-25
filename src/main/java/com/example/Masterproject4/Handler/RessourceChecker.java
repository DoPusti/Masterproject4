package com.example.Masterproject4.Handler;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.ProduktAnforderung.KinematicChain;
import com.example.Masterproject4.ProduktAnforderung.RessourceHolder;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
                System.out.println("RestCall " + restCall);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> call = restTemplate.getForEntity(restCall, String.class);
                System.out.println(call.getBody());
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
                            //System.out.println("constraintsOut " + constraintsOut);
                            //System.out.println("constraint " + constraint);
                            constraintsOut = constraint;
                            //System.out.println("ConstraintsOut nach Belegung  " + constraintsOut);

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
                            && constraint.getXCoM() >= ressourceHolderIn.getCenterOfMassX()
                            && constraint.getYCoM() >= ressourceHolderIn.getCenterOfMassY()
                            && constraint.getZCoM() >= ressourceHolderIn.getCenterOfMassZ()
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
                            //System.out.println("constraintsOut " + constraintsOut);
                            //System.out.println("constraint " + constraint);
                            constraintsOut = constraint;
                            //System.out.println("ConstraintsOut nach Belegung  " + constraintsOut);

                        }
                    }
                }

            }

        }
        return constraintsOut;
    }

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


    public void searchForKinematicChain(List<RessourceHolder> ressourceHolderIn, List<AssuranceFullObject> assuranceListIn) {



        // Schleife über alle Sequenzen des Satzes
        ressourceHolderIn.forEach(sequenz -> {
            System.out.println("Gelesene Sequenz:");
            System.out.println(sequenz.getStringSequence());
            assuranceListIn.forEach(assurance -> {
                if (assurance.getConnectionType().equals("NotAutomaticallyRemovable")) {
                    System.out.println("Gelesene Zusicherung:");
                    System.out.println(assurance.getStringSequence());
                    checkMatchingAssurance(sequenz, assurance);
                }
            });
            // Mindestens ein Eintrag wurde gefunden, der passen könnte
            if(!(sequenz.getKinematicChainList() == null)) {
                List<KinematicChain> kinematicChainList = sequenz.getKinematicChainList();
                kinematicChainList.forEach(kinematicChain -> {
                    if(!kinematicChain.isForceX()) {
                        kinematicChain.setRankingForceX(checkRankingOfConstraint(kinematicChain.getAssurance(),ressourceHolderIn,"ForceX"));
                    }
                    if(!kinematicChain.isForceY()) {
                        kinematicChain.setRankingForceY(checkRankingOfConstraint(kinematicChain.getAssurance(),ressourceHolderIn,"ForceY"));
                    }
                    if(!kinematicChain.isForceZ()) {
                        kinematicChain.setRankingForceZ(checkRankingOfConstraint(kinematicChain.getAssurance(),ressourceHolderIn,"ForceZ"));
                    }
                });
            }


        });


    }

    public void checkMatchingAssurance(RessourceHolder ressourceIn, AssuranceFullObject assuranceIn) {
        List<KinematicChain> kinematicChainList;
        if(ressourceIn.getKinematicChainList() == null) {
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


        if ((requiredStateChangeCounter + constraintCounter >= 3) && requiredStateChangeCounter > 1) {
            kinematicChain.setSequences(sequences);
            kinematicChain.setAssurance(assuranceIn);
            kinematicChain.setId(assuranceIn.getId());
            kinematicChain.setPrice(price);
            kinematicChainList.add(kinematicChain);
            ressourceIn.setKinematicChainList(kinematicChainList);
            System.out.println("Gefundene Zusicherung");
            System.out.println(kinematicChain.getSequences());
            System.out.println(kinematicChain.getId() + " | " + assuranceIn.getForceX() + " | " + assuranceIn.getForceY() + " | " + assuranceIn.getPositionX() + " | " + assuranceIn.getPositionY() + " | " + assuranceIn.getPositionZ());

        }


    }

    public int checkRankingOfConstraint(AssuranceFullObject assuranceIn, List<RessourceHolder> ressourceHolderIn,String attributeName) {

        int rankingCounter = 0;

        for(RessourceHolder ressourceHolder : ressourceHolderIn) {
            rankingCounter ++;
            switch (attributeName) {
                case "ForceX":
                    if(assuranceIn.getForceX() >= ressourceHolder.getForceX().getValue()) {
                        break;
                    }
                case "ForceY":
                    if(assuranceIn.getForceY() >= ressourceHolder.getForceX().getValue()) {
                        break;
                    }
                case "ForceZ":
                    if(assuranceIn.getForceZ() >= ressourceHolder.getForceX().getValue()) {
                        break;
                    }
                default:
            }
        }

    }

}
