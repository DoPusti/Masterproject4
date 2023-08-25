package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChain {

    boolean forceX;
    boolean forceY;
    boolean forceZ;
    int rankingForceX;
    int rankingForceY;
    int rankingForceZ;
    boolean positionX;
    boolean positionY;
    boolean positionZ;
    AssuranceFullObject assurance;
    List<Boolean> sequences;
    double price;
    long id;

}
