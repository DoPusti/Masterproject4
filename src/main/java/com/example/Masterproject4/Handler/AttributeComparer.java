package com.example.Masterproject4.Handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttributeComparer {

    double forceX;
    double forceY;
    double forceZ;
    double positionX;
    double positionY;
    double positionZ;
}
