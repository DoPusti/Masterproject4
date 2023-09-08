package com.example.Masterproject4.XMLAttributeHolder;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Component
public class ProductRequirementFullObject {

    String assetId;
    // n - Names with m Properties and Information about the Properties
    Map<String, Map<String, Double>> productPropertiesOfParts;
    List<ProcessRequirement> processRequirement;


}
