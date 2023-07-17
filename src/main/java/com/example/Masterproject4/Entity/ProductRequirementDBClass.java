package com.example.Masterproject4.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_productRequirement")
public class ProductRequirementDBClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "AssetId")
    private String assetId;

    @Column(name = "Mass")
    private double mass;

    @Column(name = "Temperature")
    private double temperature;

    @Column(name = "DimensionLength")
    private double dimensionLength;

    @Column(name = "DimensionLength")
    private double dimensionWidth;

    @Column(name = "DimensionLength")
    private double dimensionHeight;

    @Column(name = "CenterOfMassX")
    private double centerOfMassX;

    @Column(name = "CenterOfMassY")
    private double centerOfMassY;

    @Column(name = "CenterOfMassZ")
    private double centerOfMassZ;

    @Column(name = "StaticFrictionCoefficient")
    private double staticFrictionCoefficient;

    @Column(name = "FerroMagnetic")
    private Boolean ferroMagnetic;


}
