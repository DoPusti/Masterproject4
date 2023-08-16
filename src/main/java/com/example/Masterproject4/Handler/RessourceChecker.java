package com.example.Masterproject4.Handler;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.ProduktAnforderung.RessourceHolder;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
            constraints = compareRessourceWithGripper(ressourceHolder, assuranceListIn);
            if (!(constraints.getAssetId() == null)) {
                matchedRessourceholder = ressourceHolder;
                ressourceHolder.setGripper(constraints);
                break;
            }
        }
        Log.info(matchedRessourceholder.toString());
        Log.info(constraints.toString());
    }

    public AssuranceFullObject compareRessourceWithGripper(RessourceHolder ressourceHolderIn, List<AssuranceFullObject> assuranceListIn) {
        AssuranceFullObject constraintsOut = new AssuranceFullObject();
        for (AssuranceFullObject constraint : assuranceListIn) {
            if (constraint.getConnectionType().equals("AutomaticallyRemoveable")) {
                if (constraint.getForceX() >= ressourceHolderIn.getForceX().getValue() && constraint.getForceY() >= ressourceHolderIn.getForceY().getValue() && constraint.getForceZ() >= ressourceHolderIn.getForceZ().getValue() && constraint.getTorqueX() >= ressourceHolderIn.getTorqueX().getValue() && constraint.getTorqueY() >= ressourceHolderIn.getTorqueY().getValue() && constraint.getTorqueZ() >= ressourceHolderIn.getTorqueZ().getValue() && constraint.getPositionRepetitionAccuracyX() >= ressourceHolderIn.getPositionRepetitionAccuracyX().getValue() && constraint.getPositionRepetitionAccuracyY() >= ressourceHolderIn.getPositionRepetitionAccuracyY().getValue() && constraint.getPositionRepetitionAccuracyZ() >= ressourceHolderIn.getPositionRepetitionAccuracyZ().getValue() && constraint.getRotationRepetitionAccuracyX() >= ressourceHolderIn.getRotationRepetitionAccuracyX().getValue() && constraint.getRotationRepetitionAccuracyY() >= ressourceHolderIn.getRotationRepetitionAccuracyY().getValue() && constraint.getRotationRepetitionAccuracyZ() >= ressourceHolderIn.getRotationRepetitionAccuracyZ().getValue()) {
                    if (constraintsOut.getAssetId() == null || constraint.getPrice() < constraintsOut.getPrice()) {
                        //System.out.println("constraintsOut " + constraintsOut);
                        //System.out.println("constraint " + constraint);
                        constraintsOut = constraint;
                        //System.out.println("ConstraintsOut nach Belegung  " + constraintsOut);

                    }
                }
            }
        }
        return constraintsOut;
    }



}
