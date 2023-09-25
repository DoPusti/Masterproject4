package com.example.Masterproject4.CombinedRessources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KinematicChainRoot {
    List<KinematicChainNode> leaveNodes; // Liste aller Kinematischen Ketten

}


