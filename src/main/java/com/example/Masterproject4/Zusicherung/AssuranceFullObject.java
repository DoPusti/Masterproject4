package com.example.Masterproject4.Zusicherung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssuranceFullObject {
    // Identification
    String assetId;
    String serialNumber;
    String url;

    // Assurances
    double mass;
    double length;
    double width;
    double height;
    double xCoM;
    double yCoM;
    double zCoM;
    String connectionType;
    String operatingPrinciple;
    double xPosition;
    double yPosition;
    double zPosition;
    double xPositionRepetitionAccuracy;
    double yPositionRepetitionAccuracy;
    double zPositionRepetitionAccuracy;
    double xRotation;
    double yRotation;
    double zRotation;
    double xRotationRepetitionAccuracy;
    double yRotationRepetitionAccuracy;
    double zRotationRepetitionAccuracy;
    double xForceMin;
    double xForceMax;
    double yForceMin;
    double yForceMax;
    double zForceMin;
    double zForceMax;
    double xTorque;
    double yTorque;
    double zTorque;

    // MediaSupply
    double operatingCurrent;
    double operatingVoltage;
    double compressedAirPressure;
    double airFlow;

    // Environmental Conditions
    double temperature;
    double pressure;
    double humidity;
    boolean purity;
    int foodGrade;
    int explosiveness;

    // EconomicFactors
    double price;
    double lengthSR;
    double widthSR;
    double heightSR;
    int lengthOfUsage;
    int maintenanceInterval;
    int maintanceDuration;
    int deliveryTime;
    double oneTimeLicenceCost;
    double monthlyLicenceCost;



}
