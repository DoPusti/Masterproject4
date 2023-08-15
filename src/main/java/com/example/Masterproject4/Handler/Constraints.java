package com.example.Masterproject4.Handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Constraints {
    String idShort;
    double forceX;
    double forceY;
    double forceZ;
    double torqueX;
    double torqueY;
    double torqueZ;
    double positionX;
    double positionY;
    double positionZ;
    double rotationX;
    double rotationY;
    double rotationZ;
    double positionRepetitionAccuracyX;
    double positionRepetitionAccuracyY;
    double positionRepetitionAccuracyZ;
    double rotationRepetitionAccuracyX;
    double rotationRepetitionAccuracyY;
    double rotationRepetitionAccuracyZ;
    String restApi;
    String connectionType;
    double price;
    double centerOfMassX;
    double centerOfMassY;
    double centerOfMassZ;
    double length;
    double width;
    double height;
    boolean ferroMagnetic;



    @Override
    public String toString() {
        return "Constraints{" +
                "idShort='" + idShort +
                " | forceX=" + forceX +
                " |  forceY=" + forceY +
                " | forceZ=" + forceZ +
                " | torqueX=" + torqueX +
                " | torqueY=" + torqueY +
                " | torqueZ=" + torqueZ +
                " | positionX=" + positionX +
                " | positionY=" + positionY +
                " | positionZ=" + positionZ +
                " | rotationX=" + rotationX +
                " | rotationY=" + rotationY +
                " | rotationZ=" + rotationZ +
                " | length=" + length +
                " | width=" + width +
                " | height=" + height +
                " | centerOfMassX=" + centerOfMassX +
                " | centerOfMassY=" + centerOfMassY +
                " | centerOfMassZ=" + centerOfMassZ +
                " | ferroMagnetic=" + ferroMagnetic +
                " | positionRepetitionAccuracyX=" + positionRepetitionAccuracyX +
                " | positionRepetitionAccuracyY=" + positionRepetitionAccuracyY +
                " | positionRepetitionAccuracyZ=" + positionRepetitionAccuracyZ +
                " | rotationRepetitionAccuracyX=" + rotationRepetitionAccuracyX +
                " | rotationRepetitionAccuracyY=" + rotationRepetitionAccuracyY +
                " | rotationRepetitionAccuracyZ=" + rotationRepetitionAccuracyZ +
                " | restApi=" + restApi +
                " | price=" + price +
                " | connectionType=" + connectionType + '\n' +
                '}';
    }


}
