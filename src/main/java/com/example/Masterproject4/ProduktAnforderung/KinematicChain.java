package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChain {

    AbstractMap.SimpleEntry<Boolean, Integer> forceX;
    AbstractMap.SimpleEntry<Boolean, Integer> forceY;
    AbstractMap.SimpleEntry<Boolean, Integer> forceZ;

    AbstractMap.SimpleEntry<Boolean, Integer> positionX;
    AbstractMap.SimpleEntry<Boolean, Integer> positionY;
    AbstractMap.SimpleEntry<Boolean, Integer> positionZ;

    AssuranceFullObject assurance;
    double price;
    long id;


}


