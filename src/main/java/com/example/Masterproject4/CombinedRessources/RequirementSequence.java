package com.example.Masterproject4.CombinedRessources;

import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@ToString
public class RequirementSequence {

    Map<String, Map<String, PropertyInformation>> propertyParameters;

}
