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
    double positionRepetitionAccuracyX;
    double positionRepetitionAccuracyY;
    double positionRepetitionAccuracyZ;
    double rotationRepetitionAccuracyX;
    double rotationRepetitionAccuracyY;
    double rotationRepetitionAccuracyZ;
    String restApi;
    String connectionType;

    public boolean matches(Constraints other) {
        return this.forceX <= other.forceX &&
                this.forceY <= other.forceY &&
                this.forceZ <= other.forceZ &&
                this.torqueX <= other.torqueX &&
                this.torqueY <= other.torqueY &&
                this.torqueZ <= other.torqueZ &&
                this.positionRepetitionAccuracyX <= other.positionRepetitionAccuracyX &&
                this.positionRepetitionAccuracyY <= other.positionRepetitionAccuracyY &&
                this.positionRepetitionAccuracyZ <= other.positionRepetitionAccuracyZ &&
                this.rotationRepetitionAccuracyX <= other.rotationRepetitionAccuracyX &&
                this.rotationRepetitionAccuracyY <= other.rotationRepetitionAccuracyY &&
                this.rotationRepetitionAccuracyZ <= other.rotationRepetitionAccuracyZ;
    }

    @Override
    public String toString() {
        return "Constraints{" +
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
                "\nrestApi='" + restApi + '\'' +
                "\nconnectionType='" + connectionType + '\'' +
                '}';
    }


}
