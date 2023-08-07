package com.example.Masterproject4.Handler;

import com.example.Masterproject4.Handler.Constraints;
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
}
