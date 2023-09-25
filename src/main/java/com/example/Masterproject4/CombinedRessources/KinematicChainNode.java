package com.example.Masterproject4.CombinedRessources;

import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChainNode {

    PropertyInformation[][] tableOfRemainingRequirement;
    ArrayList<PropertyInformation> requirementSequence;
    Map<ArrayList<PropertyInformation>,List<CombinedRessource>> listOfCombinedRessources;
    AssuranceMapper gripperOrAxis;
    List<AssuranceMapper> nodeAssurances;
    List<KinematicChainNode> listOfLeaves;


}
