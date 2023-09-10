package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.RequirementSequence;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
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
import java.util.Map;
import java.util.TreeMap;


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

    public RequirementSequence searchForGripper(RequirementSequence requirementSequenceIn, List<AssuranceFullObject> assuranceListIn) {
        RequirementSequence requirementSequenceOut = new RequirementSequence();
        return requirementSequenceOut;
    }

    /***
     * Parsen einer Liste von Zusicherungen auf eine Liste von Zusicherungen im Map-Format
     * @param assuranceListIn   : Liste von Zusicherungen, die aus der Datenbank direkt kommen
     * @return assuranceListOut : Liste von Zusicherungen auf eine Map angepasst
     */
    public List<AssuranceMapper> fillAssuranceMapper(List<AssuranceFullObject> assuranceListIn) {
        List<AssuranceMapper> assuranceListOut = new ArrayList<>();

        for (AssuranceFullObject fullObject : assuranceListIn) {
            AssuranceMapper mapper = new AssuranceMapper();
            mapper.setAssetId(fullObject.getAssetId());
            mapper.setId((fullObject.getId()));
            Map<String, PropertyInformation> propertyParameters = new TreeMap<>();
            // Verwende Reflection, um alle Double-Attribute zu extrahieren
            Field[] fields = fullObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == double.class) {
                    try {
                        double value = field.getDouble(fullObject);
                        PropertyInformation newPropertyInformationForAssuranceMapper = new PropertyInformation();
                        newPropertyInformationForAssuranceMapper.setValueOfParameter(value);
                        propertyParameters.put(field.getName(), newPropertyInformationForAssuranceMapper);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            mapper.setPropertyParameters(propertyParameters);
            assuranceListOut.add(mapper);
        }
        return assuranceListOut;
    }

}
