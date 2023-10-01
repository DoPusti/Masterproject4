package com.example.Masterproject4.Handler;

import com.example.Masterproject4.RCZwei.KinematicChain;

import java.util.ArrayList;
import java.util.List;



/*
In diesem Beispiel speichern wir nun ganze TreeNode-Objekte in den Pfaden.
Der dfs-Algorithmus traversiert den Baum und berechnet die Summe der gripperOrAxis.valueOfParameter()-Werte für jeden Pfad.
topPaths speichert die besten Pfade, sortiert nach der Summe der Werte, und gibt die 5 besten Pfade aus.
Du kannst k entsprechend deinen Anforderungen ändern. Beachte, dass AssuranceMapper eine fiktive Klasse ist
 und du sie durch deine tatsächliche Implementierung ersetzen solltest.
 */
public class PathFinder {
    private static List<List<KinematicChain>> topPaths = new ArrayList<>();

    public static List<List<KinematicChain>> findTopPaths(KinematicChain root, int k) {
        dfs(root, new ArrayList<>(), k);
        topPaths.sort((a, b) -> Double.compare(sum(b), sum(a)));
        return topPaths.subList(0, Math.min(k, topPaths.size()));
    }

    private static void dfs(KinematicChain node, List<KinematicChain> currentPath, int k) {
        if (node == null) {
            return;
        }

        currentPath.add(node);

        if (node.getChilds().isEmpty()) {
            if (topPaths.size() < k || sum(currentPath) > sum(topPaths.get(topPaths.size() - 1))) {
                topPaths.add(new ArrayList<>(currentPath));
                topPaths.sort((a, b) -> Double.compare(sum(b), sum(a)));
                if (topPaths.size() > k) {
                    topPaths.remove(topPaths.size() - 1);
                }
            }
        } else {
            for (KinematicChain child : node.getChilds()) {
                dfs(child, currentPath, k);
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }

    public static double sum(List<KinematicChain> path) {
        return path.stream().mapToDouble(node -> node.getGripperOrAxis().getPrice()).sum();
    }
}