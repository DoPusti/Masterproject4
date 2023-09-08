package com.example.Masterproject4.CombinedRessources;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChain {
    Map<String,KinematicChainProperties> propertiesOfAttributes;
    AssuranceFullObject assurance;
    double price;
    long id;


}


