package com.example.Masterproject4.ProduktAnforderung;

import lombok.*;

import static java.lang.Math.max;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessRequirement {
    String tvName;
    String referenceParts;
    int id;
    boolean stability;

    public double forceXRsC,forceYRsC,forceZRsC ;
    public double torqueXRsC, torqueYRsC, torqueZRsC;
    public double positionRepetitionAccuracyXRsC,positionRepetitionAccuracyYRsC,positionRepetitionAccuracyZRsC;
    public double rotationRepetitionAccuracyXRsC,rotationRepetitionAccuracyYRsC,rotationRepetitionAccuracyZRsC;
    public double maxSpeedXRsC,maxSpeedYRsC,maxSpeedZRsC;
    public double maxAccelerationXRsC,maxAccelerationYRsC,maxAccelerationZRsC;
    public double forceXSsC,forceYSsC,forceZSsC;
    public double torqueXSsC,torqueYSsC,torqueZSsC;
    public double positionRepetitionAccuracyXSsC,positionRepetitionAccuracyYSsC,positionRepetitionAccuracyZSsC;
    public double rotationRepetitionAccuracyXSsC,rotationRepetitionAccuracyYSsC,rotationRepetitionAccuracyZSsC;
    public double maxSpeedXSsC,maxSpeedYSsC,maxSpeedZSsC;
    public double maxAccelerationXSsC,maxAccelerationYSsC,maxAccelerationZSsC;
    public double positionXRsC,positionYRsC,positionZRsC;
    public double rotationXRsC,rotationYRsC,rotationZRsC;
    public double positionXSsC,positionYSsC,positionZSsC;
    public double rotationXSsC,rotationYSsC,rotationZSsC;


    @Override
    public String toString() {
        return "id: " + id +
                ", tvname: " + tvName +
                ", stability: " + stability +
                ", positionX: " + max(positionXRsC,positionXSsC) +
                ", positionY: " + max(positionYRsC,positionYSsC) +
                ", positionZ: " + max(positionZRsC,positionZSsC)+
                ", forceX: " + max(forceXRsC,forceXSsC) +
                ", forceY: " + max(forceYRsC,forceYSsC) +
                ", forceZ: " + max(forceZRsC,forceZSsC) ;

    }


}
