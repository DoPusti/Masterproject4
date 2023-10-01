package com.example.Masterproject4.XMLAttributeHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyInformation {
    String subProcessId;
    double valueOfParameter;
    boolean stabilityGiven;
    String partReference;
    boolean requirementFullFilled;
    String dataSpecification;
    int valency;
    int subProcessIdOrderValue;
    String attributeName;
    int numberOfSequence;
    AssuranceMapper matchingAssurance;

    @Override
    public String toString() {
        return "ID: " + subProcessId + "/Attribut: "  + attributeName + "/Value: " + valueOfParameter;
    }
    // Konstruktor für tiefe Kopie
    public PropertyInformation(PropertyInformation original) {
        this.attributeName = original.attributeName;
        this.valueOfParameter = original.valueOfParameter;
        this.dataSpecification = original.dataSpecification;
        this.subProcessId = original.subProcessId;
    }

}
