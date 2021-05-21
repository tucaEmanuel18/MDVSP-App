package com.graph;

import com.core.Route;
import com.graphPainter.GraphPainter;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching;

import java.io.IOException;
import java.util.Set;

public class MinimalBipartitePerfectMatchingKuhnMunkres {

    public static Set<WeightEdge> resolve(Graph<RouteNode, WeightEdge> bipartiteGraph, Set<RouteNode> S, Set<RouteNode> T){
        MatchingAlgorithm.Matching<RouteNode, WeightEdge> matching =
             new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(bipartiteGraph, S, T).getMatching();

        System.out.println("\nEdges from matching: ");
       for(WeightEdge edge : matching.getEdges()){
           System.out.println(edge);
       }

        return matching.getEdges();
    }
}
