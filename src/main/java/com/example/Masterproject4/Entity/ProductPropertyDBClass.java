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
@Table(name="tbl_productProperty")
public class ProductPropertyDBClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Part")
    private String part;

    @Column(name = "Name")
    private String name;

    @Column(name = "Mass")
    private Double mass;

    @Column(name = "StaticFrictionCoefficient")
    private Double staticFrictionCoefficient;

    @Column(name = "FerroMagnetic")
    private Boolean ferroMagnetic;

    @Column(name = "Length")
    private Double length;

    @Column(name = "Width")
    private Double width;

    @Column(name = "Height")
    private Double height;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "Z")
    private Double z;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prID",nullable = false,referencedColumnName = "prID")
    private ProductRequirementDBClass productRequirement;


}

