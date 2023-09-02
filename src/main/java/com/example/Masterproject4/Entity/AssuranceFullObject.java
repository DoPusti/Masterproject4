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
    double centerOfMassX;
    double centerOfMassY;
    double centerOfMassZ;
    String connectionType;
    String operatingPrinciple;
    double positionX;
    double positionY;
    double positionZ;
    double positionRepetitionAccuracyX;
    double positionRepetitionAccuracyY;
    double positionRepetitionAccuracyZ;
    double rotationX;
    double rotationY;
    double rotationZ;
    double rotationRepetitionAccuracyX;
    double rotationRepetitionAccuracyY;
    double rotationRepetitionAccuracyZ;
    double forceX;
    double forceY;
    double forceZ;
    double torqueX;
    double torqueY;
    double torqueZ;

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

    public String getStringSequence() {
        return "id: " + id +
                ", positionX: " + positionX +
                ", positionY: " + positionY +
                ", positionZ: " + positionZ +
                ", forceX: " + forceX +
                ", forceY: " + forceY +
                ", forceZ: " + forceZ;

    }



}
