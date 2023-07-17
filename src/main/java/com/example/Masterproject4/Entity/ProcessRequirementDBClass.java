package com.example.Masterproject4.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_processRequirement")
public class ProcessRequirementDBClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "Id", nullable = false)
    private Long iD;

    @Column(name = "PositionX")
    private double positionX;

    @Column(name = "PositionY")
    private double positionY;

    @Column(name = "PositionZ")
    private double positionZ;

    @Column(name = "RotationX")
    private double rotationX;

    @Column(name = "RotationY")
    private double rotationY;

    @Column(name = "RotationZ")
    private double rotationZ;

    @Column(name = "ForceX")
    private double forceX;

    @Column(name = "ForceY")
    private double forceY;

    @Column(name = "ForceZ")
    private double forceZ;

    @Column(name = "MomentumX")
    private double momentumX;

    @Column(name = "MomentumY")
    private double momentumY;

    @Column(name = "MomentumZ")
    private double momentumZ;


}
