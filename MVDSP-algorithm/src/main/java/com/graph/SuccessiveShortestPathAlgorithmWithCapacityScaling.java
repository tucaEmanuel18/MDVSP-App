package com.graph;

import com.graphPainter.GraphPainter;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;

import java.util.function.Function;

public class SuccessiveShortestPathAlgorithmWithCapacityScaling {

    public static Graph<Node, WeightEdge> resolve(Graph<Node, WeightEdge> graph){


        MinimumCostFlowAlgorithm.MinimumCostFlow<WeightEdge> minimumCostFlow = getMinimumFlow(graph);

        System.out.println("Flow MAP::");
        for(WeightEdge weightEdge : minimumCostFlow.getFlowMap().keySet()){
            System.out.println(weightEdge.toString() + " -> " + minimumCostFlow.getFlow(weightEdge));
        }

        Graph <Node, WeightEdge> flowGraph = Mapping.createFlowGraph(graph, minimumCostFlow);
        try{
            GraphPainter.paint(flowGraph, "flow");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return flowGraph;
    }

    private static MinimumCostFlowAlgorithm.MinimumCostFlow<WeightEdge> getMinimumFlow(Graph<Node, WeightEdge> graph){

        Function<Node, Integer> supplyMap = n -> n.getCapacity();
        Function<WeightEdge, Integer> arcCapacityLowerBounds = e -> ((BoundedWeightEdge) e).getLowerBound();
        Function<WeightEdge, Integer> arcCapacityUpperBounds = e -> ((BoundedWeightEdge) e).getUpperBound();



        MinimumCostFlowProblem<Node, WeightEdge> minimumCostFlowProblem =
                new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(
                        graph,
                        supplyMap,
                        arcCapacityUpperBounds,
                        arcCapacityLowerBounds
                );

        CapacityScalingMinimumCostFlow<Node, WeightEdge> capacityScalingMinimumCostFlow = new CapacityScalingMinimumCostFlow<>();

        return capacityScalingMinimumCostFlow.getMinimumCostFlow( minimumCostFlowProblem);
    }


}
