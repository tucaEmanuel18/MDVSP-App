package com.repair;

import com.graph.WeightEdge;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching;

import java.util.Set;

public class MinimalBipartitePerfectMatching {

    BipartiteGraph bipartiteGraph;
    MatchingAlgorithm.Matching<RouteNode, WeightEdge> matching;
    public MinimalBipartitePerfectMatching(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;

    }

    public Set<WeightEdge> getEdges(){
        if(matching == null){
           matching = new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(
                            bipartiteGraph.getGraph(),
                            bipartiteGraph.getSimpleNodesPartition(),
                            bipartiteGraph.getPrimeNodesPartition()
           ).getMatching();
        }
        return matching.getEdges();
    }

    public double getWeight(){
        if(matching == null){
            matching = new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(
                    bipartiteGraph.getGraph(),
                    bipartiteGraph.getSimpleNodesPartition(),
                    bipartiteGraph.getPrimeNodesPartition()
            ).getMatching();
        }
        return matching.getWeight();
    }
}
