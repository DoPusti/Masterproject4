package com.example.Masterproject4.ProduktAnforderung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teilvorgang {
    String tvName;
    double PositionX;
    double PositionY;
    double PositionZ;
    double RotationX;
    double RotationY;
    double RotationZ;
    double ForceX;
    double ForceY;
    double ForceZ;
    double MomentumX;
    double MomentumY;
    double MomentumZ;
    String ReferenceParts;
}
