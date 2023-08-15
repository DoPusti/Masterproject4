package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.Constraints;
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
    boolean ferroMagnetic;
    double length;
    double width;
    double height;
    double centerOfMassX;
    double centerOfMassY;
    double centerOfMassZ;

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

    AssuranceFullObject gripper;


    @Override
    public String toString() {
        return "RessourceHolder{" +
                "idShort='" + idShort +
                " | forceX=" + forceX +
                " | forceY=" + forceY +
                " | forceZ=" + forceZ +
                " | torqueX=" + torqueX +
                " | torqueY=" + torqueY +
                " | torqueZ=" + torqueZ +
                " | positionRepetitionAccuracyX=" + positionRepetitionAccuracyX +
                " | positionRepetitionAccuracyY=" + positionRepetitionAccuracyY +
                " | positionRepetitionAccuracyZ=" + positionRepetitionAccuracyZ +
                " | rotationRepetitionAccuracyX=" + rotationRepetitionAccuracyX +
                " | rotationRepetitionAccuracyY=" + rotationRepetitionAccuracyY +
                " | rotationRepetitionAccuracyZ=" + rotationRepetitionAccuracyZ +
                " | mass=" + mass +
                " | meanRoughness=" + meanRoughness +
                " | ferroMagnetic=" + ferroMagnetic +
                " | length=" + length +
                " | width=" + width +
                " | height=" + height +
                " | centerOfMassX=" + centerOfMassX +
                " | centerOfMassY=" + centerOfMassY +
                " | centerOfMassZ=" + centerOfMassZ +
                " | tvList=" + tvList +
                " | restApi='" + restApi +
                " | gripper='" + gripper +
                '}' + "\n";
    }

}
