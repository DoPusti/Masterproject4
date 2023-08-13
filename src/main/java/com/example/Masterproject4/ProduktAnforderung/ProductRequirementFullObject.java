package com.example.Masterproject4.ProduktAnforderung;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Component
public class ProductRequirementFullObject {

    String assetId;
    List<ProductProperty> productProperty;
    List<ProcessRequirement> processRequirement;

}
