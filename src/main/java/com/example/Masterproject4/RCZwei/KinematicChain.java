package com.example.Masterproject4.RCZwei;

import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChain {
    PropertyInformation[][] tableOfRemainingRequirement;
    List<KinematicChain> childs;
    Set<String> remainingSequence;
    Set<String> remainingRequiredStateChanges;
    AssuranceMapper gripperOrAxis;
    String nameOfAssurance;
}
