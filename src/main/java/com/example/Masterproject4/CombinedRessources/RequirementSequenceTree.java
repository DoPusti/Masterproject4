package com.example.Masterproject4.CombinedRessources;

import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@ToString
public class RequirementSequenceTree implements RequirementTree{

    Map<String, Map<String, PropertyInformation>> propertyParameters;
    List<RequirementSequenceTree> listOfChildNodes;

    @Override
    public Boolean hasChild() {
        return listOfChildNodes.isEmpty();
    }

    @Override
    public List<RequirementSequenceTree> getChilds() {
        return listOfChildNodes;
    }

    @Override
    public void addChild(RequirementSequenceTree child) {
        listOfChildNodes.add(child);
    }
}
