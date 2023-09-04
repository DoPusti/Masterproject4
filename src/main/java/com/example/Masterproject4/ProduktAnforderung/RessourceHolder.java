package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Mapper.AttributeToValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceHolder {

    int idShort;
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
    AbstractMap.SimpleEntry<Integer, Double> positionX;
    AbstractMap.SimpleEntry<Integer, Double> positionY;
    AbstractMap.SimpleEntry<Integer, Double> positionZ;
    AbstractMap.SimpleEntry<Integer, Double> rotationX;
    AbstractMap.SimpleEntry<Integer, Double> rotationY;
    AbstractMap.SimpleEntry<Integer, Double> rotationZ;
    AbstractMap.SimpleEntry<Integer, Double> forceX;
    AbstractMap.SimpleEntry<Integer, Double> forceY;
    AbstractMap.SimpleEntry<Integer, Double> forceZ;
    AbstractMap.SimpleEntry<Integer, Double> torqueX;
    AbstractMap.SimpleEntry<Integer, Double> torqueY;
    AbstractMap.SimpleEntry<Integer, Double> torqueZ;
    AbstractMap.SimpleEntry<Integer, Double> positionRepetitionAccuracyX;
    AbstractMap.SimpleEntry<Integer, Double> positionRepetitionAccuracyY;
    AbstractMap.SimpleEntry<Integer, Double> positionRepetitionAccuracyZ;
    AbstractMap.SimpleEntry<Integer, Double> rotationRepetitionAccuracyX;
    AbstractMap.SimpleEntry<Integer, Double> rotationRepetitionAccuracyY;
    AbstractMap.SimpleEntry<Integer, Double> rotationRepetitionAccuracyZ;




    AssuranceFullObject gripper;

    // Sequence von einer Zusicherung, die passt
    List<KinematicChain> kinematicChainList;


    public String InformationForKinematicChainList() {
        StringBuilder returnInformation = new StringBuilder("Liste der Kinematischen Kitte von :" + idShort);
        for(KinematicChain kinematicChain : kinematicChainList) {
            returnInformation.append("ID = ").append(kinematicChain.getId());
            returnInformation.append("-> AttributsInformationen :").append("\n");
            for(Map.Entry<String,KinematicChainProperties> entry : kinematicChain.getPropertiesOfAttributes().entrySet()) {
                returnInformation.append("Attributsname= ").append(entry.getKey());
                returnInformation.append(",Attributswert= ").append(entry.getValue().getValueOfAttribute());
                returnInformation.append(",Attributstyp= ").append(entry.getValue().getKindOfAttribute());
                returnInformation.append(",Wert ist erreicht= ").append(entry.getValue().isValueIsHigherOrEqualToRessource());
                returnInformation.append(",TeilvorgangsId= ").append(entry.getValue().getSubprocess()).append("\n");
            }
        }
        return returnInformation.toString();
    }

    public String getStringSequence() {
        return "idShort: " + idShort +
                ", positionX: " + positionX +
                ", positionY: " + positionY +
                ", positionZ: " + positionZ +
                ", forceX: " + forceX +
                ", forceY: " + forceY+
                ", forceZ: " + forceZ +
                ", gripper: " + gripper ;

    }



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
                " | restApi='" + restApi +
                " | gripper='" + gripper +
                '}' + "\n";
    }

}
