package com.example.Masterproject4.Handler;

import com.example.Masterproject4.RCZwei.KinematicChain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PathFinder2 {
    private static final PriorityQueue<List<KinematicChain>> minHeapPaths = new PriorityQueue<>(Comparator.comparingDouble(PathFinder2::sum));

    public static List<List<KinematicChain>> findTopPaths(KinematicChain root) {
        dfs(root, new ArrayList<>());
        List<List<KinematicChain>> topPaths = new ArrayList<>();
        while (!minHeapPaths.isEmpty()) {
            topPaths.add(minHeapPaths.poll());
            if (topPaths.size() == 2) {
                break;
            }
        }
        return topPaths;
    }

    private static void dfs(KinematicChain node, List<KinematicChain> currentPath) {
        if (node == null) {
            return;
        }

        currentPath.add(node);

        if (node.getChilds().isEmpty()) {
            minHeapPaths.offer(new ArrayList<>(currentPath));
        } else {
            for (KinematicChain child : node.getChilds()) {
                dfs(child, currentPath);
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }

    public static double sum(List<KinematicChain> path) {
        return path.stream().mapToDouble(node -> node.getGripperOrAxis().getPrice()).sum();
    }

}
