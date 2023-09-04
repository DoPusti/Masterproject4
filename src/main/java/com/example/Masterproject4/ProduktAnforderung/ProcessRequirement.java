package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Mapper.AttributeToValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessRequirement {
    String tvName;
    String referenceParts;
    int id;
    boolean stability;
    Map<String, AttributeToValue> attributeDefinitions;

    @Override
    public String toString() {
        String returnString = "";
        for (Map.Entry<String, AttributeToValue> entry : attributeDefinitions.entrySet()) {
            returnString = returnString.concat("Attributname: " + entry.getKey() +
                    ",Wert : " + entry.getValue().getValueOfParameter() +
                    ",ProcessId " +entry.getValue().getSubProcessId() +
                    ",Stability: " + entry.getValue().isStabilityGiven() + "\n");
        }
        return returnString;
    }


}
