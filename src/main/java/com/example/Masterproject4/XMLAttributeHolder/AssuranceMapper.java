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
    Map<String,PropertyInformation> propertyParameters;
}
