package com.example.Masterproject4.ProduktAnforderung;

import lombok.*;

import static java.lang.Math.max;

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
    double length, width, height;
    double centerOfMassX, centerOfMassY, centerOfMassZ;
    double meanRoughness;
    @Override
    public String toString() {
        return "idShort: " + idShort +
                ", mass: " + mass +
                ", length: " + length +
                ", width: " + width +
                ", height: " + height +
                ", centerOfMassX: " + centerOfMassX+
                ", centerOfMassY: " + centerOfMassY +
                ", centerOfMassZ: " + centerOfMassZ ;


    }
}
