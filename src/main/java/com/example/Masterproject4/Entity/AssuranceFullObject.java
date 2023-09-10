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
    public double mass;
    public double length;
    public double width;
    public double height;
    public double centerOfMassX;
    public double centerOfMassY;
    public double centerOfMassZ;
    String connectionType;
    String operatingPrinciple;
    public double positionX;
    public double positionY;
    public double positionZ;
    public double positionRepetitionAccuracyX;
    public double positionRepetitionAccuracyY;
    public double positionRepetitionAccuracyZ;
    public double rotationX;
    public double rotationY;
    public double rotationZ;
    public double rotationRepetitionAccuracyX;
    public double rotationRepetitionAccuracyY;
    public double rotationRepetitionAccuracyZ;
    public double forceX;
    public double forceY;
    public double forceZ;
    public double torqueX;
    public double torqueY;
    public double torqueZ;

    // MediaSupply
    public double operatingCurrent;
    public double operatingVoltage;
    public double compressedAirPressure;
    public double airFlow;

    // Environmental Conditions
    public double temperature;
    public double pressure;
    public double humidity;
    boolean purity;
    int foodGrade;
    int explosiveness;

    // EconomicFactors
    public double price;
    public double lengthSR;
    public double widthSR;
    public double heightSR;
    public double lengthOfUsage;
    public double maintenanceInterval;
    public double maintanceDuration;
    public double deliveryTime;
    public double oneTimeLicenceCost;
    public double monthlyLicenceCost;

    String RestAPIAdress;


}
