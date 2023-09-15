package com.example.Masterproject4.CombinedRessources;
import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@Builder
public class KinematicChainTree {
    Map<Integer, Map<String, PropertyInformation>> requirementSet;
}
