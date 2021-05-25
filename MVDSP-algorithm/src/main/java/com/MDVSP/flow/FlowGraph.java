package com.MDVSP.flow;

import com.MDVSP.graph.Node;
import com.MDVSP.graph.WeightEdge;
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
