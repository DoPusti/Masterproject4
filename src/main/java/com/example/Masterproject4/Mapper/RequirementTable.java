package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Component
public class RequirementTable {
    Map<Integer, Map<String, PropertyInformation>> requirementAttributes;
}
