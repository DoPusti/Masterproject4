package com.example.Masterproject4.XMLAttributeHolder;

import lombok.*;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ProcessRequirement {
    String tvName;
    String referenceParts;
    int id;
    boolean stability;
    Map<String, PropertyInformation> attributeDefinitions;


}
