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
    // Noch zu betrachtende Anforderungen
    PropertyInformation[][] tableOfRemainingRequirement;
    // Kindknoten
    List<KinematicChain> childs;
    // Teilvorgänge, die noch betrachtet werden müssen
    Set<String> remainingSequence;
    // Anforderungsset im Bezug auf Achsen, die noch betrachtet werden müssen
    Set<String> remainingRequiredStateChanges;
    // Zusicherungen selbst
    AssuranceMapper gripperOrAxis;
    // Die gerade zu betrachtenden Schlange
    ArrayList<PropertyInformation> sequenceOfAllProperties;
    // Referenz für Greifer oder Achse
    String nameOfAssurance;

    // Methode, um einen KinematicChain zu den childs hinzuzufügen
    public void addChild(KinematicChain child) {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        childs.add(child);
    }
}
