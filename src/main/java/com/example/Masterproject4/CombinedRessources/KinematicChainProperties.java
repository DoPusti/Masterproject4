package com.example.Masterproject4.CombinedRessources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChainProperties {
    int ranking;
    boolean valueIsHigherOrEqualToRessource;
    double valueOfAttribute;
    int subprocess;
    String kindOfAttribute;

}
