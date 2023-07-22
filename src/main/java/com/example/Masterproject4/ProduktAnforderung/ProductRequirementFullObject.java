package com.example.Masterproject4.ProduktAnforderung;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ProductRequirementFullObject {

    String assetId;
    List<Part> part;
    List<Teilvorgang> teilVorgang;

}