package com.example.Masterproject4.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_productRequirement")
public class ProductRequirementDBClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "prID", nullable = false)
    private Long id;

    @Column(name = "AssetId")
    private String assetId;

    @OneToMany(mappedBy="productRequirement",fetch = FetchType.EAGER)
    private Set<ProductPropertyDBClass> productPoperties;

    @OneToMany(mappedBy="productRequirement",fetch = FetchType.EAGER)
    private Set<ProcessRequirementDBClass> processRequirements;

}
