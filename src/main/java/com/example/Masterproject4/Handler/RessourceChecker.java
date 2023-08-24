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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


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
        int matchCounter = 0;
        Class<RessourceHolder> ressourceHolderClass = RessourceHolder.class;
        Field[] fields = ressourceHolderClass.getDeclaredFields();
        int minMatchAttributes = (int) Math.ceil(fields.length / 2.0);

        // Schleife über alle Sequenzen des Satzes
        ressourceHolderIn.forEach(sequenz -> {
            System.out.println("Gelesene Sequenz:");
            System.out.println(sequenz.toString());
            assuranceListIn.forEach(assurance -> {
                checkMatchingAssurance(sequenz, assurance);

            });




            // Ziel -> Constraints müssen immer erfüllt sein
            //      -> Min. 1 RequiredStateChange muss erfüllt sein

        });




    }

    public void checkMatchingAssurance(RessourceHolder ressourceIn, AssuranceFullObject assuranceIn) {
        List<KinematicChain> kinematicChainList  =ressourceIn.getKinematicChainList();
        KinematicChain kinematicChain = new KinematicChain();
        List<Boolean> sequences = new ArrayList<>();
        int booleanCounter = 0;
        double price = assuranceIn.getPrice();

        AttributeComparer ressourceAttribute = AttributeComparer.builder()
                .positionX(ressourceIn.getPositionX().getValue())
                .positionY(ressourceIn.getPositionY().getValue())
                .positionZ(ressourceIn.getPositionZ().getValue())
                .forceX(ressourceIn.getForceX().getValue())
                .forceY(ressourceIn.getForceY().getValue())
                .forceZ(ressourceIn.getForceZ().getValue())
                .build();

        AttributeComparer assuranceInAttribute = AttributeComparer.builder()
                .positionX(ressourceIn.getPositionX().getValue())
                .positionY(ressourceIn.getPositionY().getValue())
                .positionZ(ressourceIn.getPositionZ().getValue())
                .forceX(ressourceIn.getForceX().getValue())
                .forceY(ressourceIn.getForceY().getValue())
                .forceZ(ressourceIn.getForceZ().getValue())
                .build();

        Field[] fieldsOfAttributeComparer =  ressourceAttribute.getClass().getDeclaredFields();
        Field[] fieldsOfkinematicChain =  kinematicChain.getClass().getDeclaredFields();

        for (Field field : fieldsOfAttributeComparer) {
            field.setAccessible(true);
            try {
                Object value1 = field.get(ressourceAttribute);
                Object value2 = field.get(assuranceInAttribute);
                if (value1 instanceof Double && value2 instanceof Double) {
                    double doubleValue1 = (double) value1;
                    double doubleValue2 = (double) value2;
                    Field booleanField = fieldsOfkinematicChain.getClass().getDeclaredField(field.getName());
                    booleanField.setAccessible(true);
                    if(doubleValue1 <= doubleValue2) {
                        booleanField.set(fieldsOfkinematicChain, true);
                        sequences.add(true);
                        booleanCounter ++;
                    } else {
                        booleanField.set(fieldsOfkinematicChain, false);
                        sequences.add(false);
                    }

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        if(booleanCounter > 3) {
            kinematicChain.setSequences(sequences);
            kinematicChain.setAssetId(assuranceIn.getAssetId());
        }



    }


}
