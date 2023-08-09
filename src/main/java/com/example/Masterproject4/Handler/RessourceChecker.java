package com.example.Masterproject4.Handler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Component
@Data
@Builder
public class RessourceChecker {

    public static void findMatchingAssurance(Constraints requirement, List<Constraints> assurances, List<Constraints> matchedAssurance) {
        for (Constraints assurance : assurances) {
            if (requirement.matches(assurance)) {
                matchedAssurance.add(assurance);
                System.out.println("Gefundenes Requirement");
                System.out.println(requirement);
                System.out.println("Gefundener Assurance");
                System.out.println(assurance);
            }
        }
    }

    public List<Constraints> compareRequirementWithAssurance(List<Constraints> productRequirements, List<Constraints> assurances) {
        List<Constraints> matchedAssurances = new ArrayList<>();

        System.out.println("Liste der Anforderungen");
        productRequirements.forEach(System.out::println);
        ;
        System.out.println("Liste der Assurances");
        assurances.forEach(System.out::println);
        ;

        for (Constraints requirement : productRequirements) {
            findMatchingAssurance(requirement, assurances, matchedAssurances);
            if (!(matchedAssurances.isEmpty())) {
                break;
            }
        }

        return matchedAssurances;
    }

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
        List<Constraints> listOfMatchedConstraints = new ArrayList<Constraints>();
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
        System.out.println("Passende Ressource");
        System.out.println(matchedRessourceholder);
        System.out.println("Passende Assurnace");
        System.out.println(listOfMatchedConstraints);
    }

    public void compareRessourceWithGripper(RessourceHolder ressourceHolderIn, List<Constraints> assuranceListIn, List<Constraints> matchedConstraints) {
        for (Constraints constraint : assuranceListIn) {
            if (constraint.getConnectionType().equals("AutomaticallyRemoveable")) {
                /*
                System.out.println("Vergleich von");
                System.out.println(ressourceHolderIn);
                System.out.println("mit");
                System.out.println(constraint);

                 */
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
}
