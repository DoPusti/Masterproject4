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
    double PositionX;
    double PositionY;
    double PositionZ;
    double PositionRepetitionAccuracyX;
    double PositionRepetitionAccuracyY;
    double PositionRepetitionAccuracyZ;
    double RotationX;
    double RotationY;
    double RotationZ;
    double RotationRepetitionAccuracyX;
    double RotationRepetitionAccuracyY;
    double RotationRepetitionAccuracyZ;
    double ForceX;
    double ForceY;
    double ForceZ;
    double TorqueX;
    double TorqueY;
    double TorqueZ;

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

    String RestAPIAdress;



}
