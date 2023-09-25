package com.example.Masterproject4.CombinedRessources;

import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CombinedRessource {
    List<AssuranceMapper> kinematicChainOfAllRessources;
}
