package com.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;

import java.util.function.Function;

public class SuccessiveShortestPathAlgorithmWithCapacityScaling {

    public static void resolve(Graph<Node, Edge> graph){

        System.out.println("Flow MAP::");
        MinimumCostFlowAlgorithm.MinimumCostFlow<Edge> minimumCostFlow = getMinimumFlow(graph);
        for(Edge edge : minimumCostFlow.getFlowMap().keySet()){
            System.out.println(edge.toString() + " -> " + minimumCostFlow.getFlow(edge));
        }


    }

    private static MinimumCostFlowAlgorithm.MinimumCostFlow<Edge> getMinimumFlow(Graph<Node, Edge> graph){

        Function<Node, Integer> supplyMap = n -> n.getCapacity();
        Function<Edge, Integer> arcCapacityLowerBounds = e -> e.getLowerBound();
        Function<Edge, Integer> arcCapacityUpperBounds = e -> e.getUpperBound();



        MinimumCostFlowProblem<Node, Edge> minimumCostFlowProblem =
                new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<Node, Edge>(
                        graph,
                        supplyMap,
                        arcCapacityUpperBounds,
                        arcCapacityLowerBounds
                );

        CapacityScalingMinimumCostFlow<Node,Edge> capacityScalingMinimumCostFlow = new CapacityScalingMinimumCostFlow<>();

        return capacityScalingMinimumCostFlow.getMinimumCostFlow( minimumCostFlowProblem);
    }


}
