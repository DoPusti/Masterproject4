package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.*;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class RequirementRow {
    Map<String, PropertyInformation> requirementAttributes;
}
