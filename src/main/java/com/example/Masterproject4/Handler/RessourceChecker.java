package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.RequirementSequence;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.XMLAttributeHolder.RessourceHolder;
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
                Log.info("RestCall " + restCall);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> call = restTemplate.getForEntity(restCall, String.class);
                Log.info(call.getBody());
            }
        });

    }

    public RequirementSequence searchForGripper(RequirementSequence requirementSequenceIn,List<AssuranceFullObject> assuranceListIn) {
        RequirementSequence requirementSequenceOut = new RequirementSequence();
        return requirementSequenceOut;
    }

}
