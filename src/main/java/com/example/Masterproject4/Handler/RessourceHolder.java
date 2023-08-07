package com.example.Masterproject4.Handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceHolder {

    String idShort;
    String restApi;
    double mass;

    AbstractMap.SimpleEntry<String, Double> forceX;
    AbstractMap.SimpleEntry<String, Double> forceY;
    AbstractMap.SimpleEntry<String, Double> forceZ;
    AbstractMap.SimpleEntry<String, Double> torqueX;
    AbstractMap.SimpleEntry<String, Double> torqueY;
    AbstractMap.SimpleEntry<String, Double> torqueZ;
    AbstractMap.SimpleEntry<String, Double> positionRepetitionAccuracyX;
    AbstractMap.SimpleEntry<String, Double> positionRepetitionAccuracyY;
    AbstractMap.SimpleEntry<String, Double> positionRepetitionAccuracyZ;
    AbstractMap.SimpleEntry<String, Double> rotationRepetitionAccuracyX;
    AbstractMap.SimpleEntry<String, Double> rotationRepetitionAccuracyY;
    AbstractMap.SimpleEntry<String, Double> rotationRepetitionAccuracyZ;


}
