package com.example.Masterproject4.CombinedRessources;

import java.util.List;

public interface RequirementTree {
    Boolean hasChild();
    List<KinematicChainTree> getChilds();
    void addChild(KinematicChainTree child);
}
