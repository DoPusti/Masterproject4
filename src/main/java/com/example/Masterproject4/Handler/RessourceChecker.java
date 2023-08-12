package com.example.Masterproject4.Handler;

import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
import com.example.Masterproject4.ProduktAnforderung.ProductProperty;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    public void checkConstraintsOfRequirement(List<RessourceHolder> ressourceHolderIn, List<Constraints> assuranceListIn, Boolean gripper) {
        List<Constraints> listOfMatchedConstraints = new ArrayList<>();
        RessourceHolder matchedRessourceholder = new RessourceHolder();
        for (RessourceHolder ressourceHolder : ressourceHolderIn) {
            if (gripper) {
                compareRessourceWithGripper(ressourceHolder, assuranceListIn, listOfMatchedConstraints);
            }
            if (!(listOfMatchedConstraints.isEmpty())) {
                matchedRessourceholder = ressourceHolder;
                break;
            }
        }
        Log.info(matchedRessourceholder.toString());
        Log.info(listOfMatchedConstraints.toString());
    }

    public void compareRessourceWithGripper(RessourceHolder ressourceHolderIn, List<Constraints> assuranceListIn, List<Constraints> matchedConstraints) {
        for (Constraints constraint : assuranceListIn) {
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
                    matchedConstraints.add(constraint);
                }
            }
        }
    }

    public List<RessourceHolder> fillPartAttributs(List<RessourceHolder> ressourceHolderListIn, ProductRequirementFullObject fullObjectProductRequirementIn) {
        // fullObjectProductRequirementIn als Referencliste
        // ressourceHolderListIn als Sortierte Liste von Ressourcen
        List<ProductProperty> productPropertyList = fullObjectProductRequirementIn.getProductProperty();
        List<ProcessRequirement> processRequirementList= fullObjectProductRequirementIn.getProcessRequirement();

        ressourceHolderListIn.forEach(ressourceHolder -> {
            productPropertyList.forEach(productProperty -> {
                if(ressourceHolder.getTvList().contains(productProperty.getIdShort())) {
                    processRequirementList.forEach(processRequirement -> {
                        if(processRequirement.get)
                    });
                }
            });
        });
        return ressourceHolderListIn;
    }
}
