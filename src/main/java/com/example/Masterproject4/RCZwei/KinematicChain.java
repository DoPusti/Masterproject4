package com.example.Masterproject4.RCZwei;

import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Component
public class KinematicChain {
    Integer uuid;
    // Noch zu betrachtende Anforderungen
    PropertyInformation[][] tableOfRemainingRequirement;
    // Kindknoten
    ArrayList<KinematicChain> childs = new ArrayList<>();
    // Teilvorgänge, die noch betrachtet werden müssen
    Set<String> remainingSequence;
    // Zusicherungen selbst
    AssuranceMapper gripperOrAxis;
    // Die gerade zu betrachtenden Schlange
    Map<String,PropertyInformation> propertiesOfCurrentKinematicChain;
    // Referenz für Greifer oder Achse
    String nameOfAssurance;
    // Methode, um einen KinematicChain zu den childs hinzuzufügen
    public void addChild(KinematicChain child) {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        childs.add(child);
    }


   // Methode zum Sammeln der Baumstruktur als Zeichenkette
    public String getTreeStructure() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("UUID: ").append(uuid).append("\n");
        if (gripperOrAxis != null) {
            result.append("Gripper/Axis ID: ").append(gripperOrAxis.getId()).append("\n");
        }

        if (childs != null) {
            for (KinematicChain child : childs) {
                result.append("   |--> ").append(child.getTreeStructure().replaceAll("\n", "\n   ")).append("\n");
            }
        }

        return result.toString();
    }


    // Methode zum Sammeln der Baumstruktur als HTML-String
    public String getTreeStructureAsHTML() {
        StringBuilder result = new StringBuilder();
        result.append("<ul>");
        result.append("<li>UUID: ").append(uuid).append("</li>");
        if (childs != null) {
            result.append("<ul>");
            for (KinematicChain child : childs) {
                result.append("<li>").append(child.getTreeStructureAsHTML()).append("</li>");
            }
            result.append("</ul>");
        }
        result.append("</ul>");
        return result.toString();
    }
    
    
    
    
}
