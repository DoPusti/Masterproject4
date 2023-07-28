package com.example.Masterproject4.ProduktAnforderung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessRequirement {
    String tvName;
    double PositionX;
    double PositionY;
    double PositionZ;
    double RotationX;
    double RotationY;
    double RotationZ;
    double ForceXRsC;
    double ForceYRsC;
    double ForceZRsC;
    double ForceXSsC;
    double ForceYSsC;
    double ForceZSsC;
    double MomentumXRsC;
    double MomentumYRsC;
    double MomentumZRsC;
    double MomentumXSsC;
    double MomentumYSsC;
    double MomentumZSsC;
    String ReferenceParts;
    double maxSpeedXRsC;
    double maxSpeedYRsC;
    double maxSpeedZRsC;
    double maxSpeedXSsC;
    double maxSpeedYSsC;
    double maxSpeedZSsC;
    double MaxAccelerationXRsC;
    double MaxAccelerationYRsC;
    double MaxAccelerationZRsC;
    double MaxAccelerationXSsC;
    double MaxAccelerationYSsC;
    double MaxAccelerationZSsC;
}
