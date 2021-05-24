package com.flow;

import com.graph.Node;
import com.graph.WeightEdge;
import org.jgrapht.Graph;

public class FlowGraph {
    Graph<Node, WeightEdge> flowGraph;

    public FlowGraph(Graph<Node, WeightEdge> graph){
       flowGraph = SuccessiveShortestPathAlgorithmWithCapacityScaling.resolve(graph);
    }

    public Graph<Node, WeightEdge> getFlowGraph() {
        return flowGraph;
    }
}
