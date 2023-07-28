package com.example.Masterproject4.ProduktAnforderung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductProperty {
    String typ;
    String idShort;
    double mass;
    double staticFrictionCoefficient;
    boolean ferroMagnetic;
    double length;
    double width;
    double height;
    double x;
    double y;
    double z;
    double meanRoughness;
}
