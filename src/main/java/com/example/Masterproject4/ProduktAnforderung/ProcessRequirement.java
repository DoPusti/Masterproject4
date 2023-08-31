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

    double PositionXRsC;
    double PositionYRsC;
    double PositionZRsC;
    double RotationXRsC;
    double RotationYRsC;
    double RotationZRsC;

    double PositionXSsC;
    double PositionYSsC;
    double PositionZSsC;
    double RotationXSsC;
    double RotationYSsC;
    double RotationZSsC;

    // PersistentStateChange
    double ForceXRsC;
    double ForceYRsC;
    double ForceZRsC;
    double TorqueXRsC;
    double TorqueYRsC;
    double TorqueZRsC;
    double PositionRepetitionAccuracyXRsC;
    double PositionRepetitionAccuracyYRsC;
    double PositionRepetitionAccuracyZRsC;
    double RotationRepetitionAccuracyXRsC;
    double RotationRepetitionAccuracyYRsC;
    double RotationRepetitionAccuracyZRsC;
    double maxSpeedXRsC;
    double maxSpeedYRsC;
    double maxSpeedZRsC;
    double MaxAccelerationXRsC;
    double MaxAccelerationYRsC;
    double MaxAccelerationZRsC;

    // SecundaryStateChange
    double ForceXSsC;
    double ForceYSsC;
    double ForceZSsC;
    double TorqueXSsC;
    double TorqueYSsC;
    double TorqueZSsC;
    double PositionRepetitionAccuracyXSsC;
    double PositionRepetitionAccuracyYSsC;
    double PositionRepetitionAccuracyZSsC;
    double RotationRepetitionAccuracyXSsC;
    double RotationRepetitionAccuracyYSsC;
    double RotationRepetitionAccuracyZSsC;
    double maxSpeedXSsC;
    double maxSpeedYSsC;
    double maxSpeedZSsC;
    double MaxAccelerationXSsC;
    double MaxAccelerationYSsC;
    double MaxAccelerationZSsC;


    String ReferenceParts;
    boolean stability;



}
