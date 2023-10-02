package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.CombinedRessources;
import com.example.Masterproject4.CombinedRessources.KinematicChain;

import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {

    private static final PriorityQueue<List<CombinedRessources>> minHeapPaths = new PriorityQueue<>(Comparator.comparingDouble(PathFinder::sum));

    public static List<List<CombinedRessources>> findTopPaths(KinematicChain root, int kMin) {
        dfs(root, new ArrayList<>(), new HashSet<>());
        List<List<CombinedRessources>> topPaths = new ArrayList<>();
        Set<Set<Long>> visitedPaths = new HashSet<>();
        while (!minHeapPaths.isEmpty()) {
            List<CombinedRessources> currentPath = minHeapPaths.poll();
            Set<Long> pathSet = currentPath.stream().map(CombinedRessources::getId).collect(Collectors.toSet());
            if (!visitedPaths.contains(pathSet)) {
                visitedPaths.add(pathSet);
                topPaths.add(currentPath);
            }
            if (kMin != 0) {
                if (topPaths.size() == kMin) {
                    break;
                }
            }
        }
        return topPaths;
    }

    private static void dfs(KinematicChain node, List<CombinedRessources> currentPath, Set<Long> visitedNodes) {
        if (node == null || visitedNodes.contains(node.getGripperOrAxis().getId()) || !node.getPathIsRelevant()) {
            return;
        }

        CombinedRessources combinedRessources = new CombinedRessources(node.getGripperOrAxis().getId(), node.getNameOfAssurance(), node.getGripperOrAxis().getPrice());
        visitedNodes.add(node.getGripperOrAxis().getId());
        currentPath.add(combinedRessources);

        if (node.getChilds().isEmpty()) {
            minHeapPaths.offer(new ArrayList<>(currentPath));
        } else {
            for (KinematicChain child : node.getChilds()) {
                dfs(child, currentPath, visitedNodes);
            }
        }

        visitedNodes.remove(node.getGripperOrAxis().getId());
        currentPath.remove(currentPath.size() - 1);
    }

    public static double sum(List<CombinedRessources> path) {
        return path.stream().mapToDouble(CombinedRessources::getPrice).sum();
    }

}
