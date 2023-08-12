package com.example.Masterproject4.Handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceHolder {

    String idShort;
    String restApi;
    double mass;
    double meanRoughness;
    List<String> tvList;

    AbstractMap.SimpleEntry<String, Double> positionX;
    AbstractMap.SimpleEntry<String, Double> positionY;
    AbstractMap.SimpleEntry<String, Double> positionZ;
    AbstractMap.SimpleEntry<String, Double> rotationX;
    AbstractMap.SimpleEntry<String, Double> rotationY;
    AbstractMap.SimpleEntry<String, Double> rotationZ;
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


    @Override
    public String toString() {
        return "RessourceHolder{" +
                "idShort='" + idShort + '\'' +
                "\nforceX=" + forceX +
                "\nforceY=" + forceY +
                "\nforceZ=" + forceZ +
                "\ntorqueX=" + torqueX +
                "\ntorqueY=" + torqueY +
                "\ntorqueZ=" + torqueZ +
                "\npositionRepetitionAccuracyX=" + positionRepetitionAccuracyX +
                "\npositionRepetitionAccuracyY=" + positionRepetitionAccuracyY +
                "\npositionRepetitionAccuracyZ=" + positionRepetitionAccuracyZ +
                "\nrotationRepetitionAccuracyX=" + rotationRepetitionAccuracyX +
                "\nrotationRepetitionAccuracyY=" + rotationRepetitionAccuracyY +
                "\nrotationRepetitionAccuracyZ=" + rotationRepetitionAccuracyZ +
                "\ntvList=" + tvList +
                "\nrestApi='" + restApi + '\'' +
                '}';
    }

}
