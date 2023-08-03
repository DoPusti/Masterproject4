package com.example.Masterproject4.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class AssuranceFullObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

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
    double xForce;
    double yForce;
    double zForce;
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
    double lengthOfUsage;
    double maintenanceInterval;
    double maintanceDuration;
    double deliveryTime;
    double oneTimeLicenceCost;
    double monthlyLicenceCost;



}
