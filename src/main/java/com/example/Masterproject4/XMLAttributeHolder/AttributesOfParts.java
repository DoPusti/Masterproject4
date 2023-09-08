package com.example.Masterproject4.XMLAttributeHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttributesOfParts {
    double mass;
    double staticFrictionCoefficient;
    boolean ferroMagnetic;
    double length, width, height;
    double centerOfMassX, centerOfMassY, centerOfMassZ;
    double meanRoughness;
}
