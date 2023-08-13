package com.example.Masterproject4.ProduktAnforderung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcessReference {
    String partName;
    String tvName;
    double mass;
    double meanRoughness;
    boolean ferroMagnetic;
    double length;
    double width;
    double height;
    double centerOfMassX;
    double centerOfMassY;
    double centerOfMassZ;

    @Override
    public String toString() {
        return "ProductProcessReference{" +
                "partName='" + partName +
                " | tvName=" + tvName +
                " | mass=" + mass +
                " | meanRoughness=" + meanRoughness +
                " | ferroMagnetic=" + ferroMagnetic +
                " | length=" + length +
                " | width=" + width +
                " | height=" + height +
                " | centerOfMassX=" + centerOfMassX +
                " | centerOfMassY=" + centerOfMassY +
                " | centerOfMassZ=" + centerOfMassZ +
                '}' + "\n";
    }

}
