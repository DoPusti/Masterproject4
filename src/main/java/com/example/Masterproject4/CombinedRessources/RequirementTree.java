package com.example.Masterproject4.CombinedRessources;

import java.util.List;

public interface RequirementTree {
    Boolean hasChild();
    List<RequirementSequenceTree> getChilds();
    void addChild(RequirementSequenceTree child);
}
