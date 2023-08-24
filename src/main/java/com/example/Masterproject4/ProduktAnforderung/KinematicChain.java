package com.example.Masterproject4.ProduktAnforderung;

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
    boolean positionX;
    boolean positionY;
    boolean positionZ;
    List<Boolean> sequences;
    double price;
    String assetId;
}
