package com.example.Masterproject4.CombinedRessources;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcessReference {
    String partName;
    String tvName;
    double mass;
    double staticFrictionCoefficient;
    boolean ferroMagnetic;
    double length, width, height;
    double centerOfMassX, centerOfMassY, centerOfMassZ;
    double meanRoughness;

    @Override
    public String toString() {
        return "tvName: " + tvName +
                ", partName: " + partName +
                ", mass: " + mass +
                ", length: " + length +
                ", width: " + width +
                ", height: " + height +
                ", centerOfMassX: " + centerOfMassX+
                ", centerOfMassY: " + centerOfMassY +
                ", centerOfMassZ: " + centerOfMassZ ;


    }

}
