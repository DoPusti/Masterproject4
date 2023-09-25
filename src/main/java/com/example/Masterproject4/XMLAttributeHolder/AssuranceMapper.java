package com.example.Masterproject4.XMLAttributeHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class AssuranceMapper {
    String assetId;
    Long id;
    String connectionType;
    double price;
    Map<String,PropertyInformation> propertyParameters;

    public StringBuilder toStringCustom() {
        StringBuilder outPutString = new StringBuilder();
        outPutString.append("Asset ID: ").append(assetId).append("\n");
        outPutString.append("ID      : ").append(id).append("\n");
        for (Map.Entry<String, PropertyInformation> entry : propertyParameters.entrySet()) {
            String propertyName = entry.getKey();
            PropertyInformation propertyInformation = entry.getValue();
            String valueOfParameter = String.valueOf(propertyInformation.getValueOfParameter());
            outPutString.append("Property Name: ").append(propertyName).append("\n");
            outPutString.append("Value of Parameter: ").append(valueOfParameter).append("\n");
        }
        outPutString.append("\n");
        return outPutString;
    }
}
