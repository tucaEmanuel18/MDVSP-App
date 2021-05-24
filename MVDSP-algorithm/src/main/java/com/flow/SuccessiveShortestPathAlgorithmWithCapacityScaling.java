package com.flow;

import com.graph.BoundedWeightEdge;
import com.graph.Node;
import com.graph.WeightEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.function.Function;

public class SuccessiveShortestPathAlgorithmWithCapacityScaling {

    public static Graph<Node, WeightEdge> resolve(Graph<Node, WeightEdge> graph) {


        MinimumCostFlowAlgorithm.MinimumCostFlow<WeightEdge> minimumCostFlow = getMinimumFlow(graph);

        Graph<Node, WeightEdge> flowGraph = createFlowGraph(graph, minimumCostFlow);
       /* try {
            GraphPainter.paint(flowGraph, "flow");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
        return flowGraph;
    }

    private static MinimumCostFlowAlgorithm.MinimumCostFlow<WeightEdge> getMinimumFlow(Graph<Node, WeightEdge> graph) {

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
        return capacityScalingMinimumCostFlow.getMinimumCostFlow(minimumCostFlowProblem);
    }

    public static Graph<Node, WeightEdge> createFlowGraph(Graph<Node, WeightEdge> graph, MinimumCostFlowAlgorithm.MinimumCostFlow<WeightEdge> minimumCostFlow) {
        Graph<Node, WeightEdge> flowGraph = new DefaultDirectedWeightedGraph<>(WeightEdge.class);
        for (Node node : graph.vertexSet()) {
            flowGraph.addVertex(node);
        }
        for (WeightEdge weightEdge : minimumCostFlow.getFlowMap().keySet()) {
            if (minimumCostFlow.getFlow(weightEdge) > 0) {
                //System.out.println("I need to add edge from " + weightEdge.getSourceNode() + " to " + weightEdge.getTargetNode() + " with flow "+ minimumCostFlow.getFlow(weightEdge));
                flowGraph.addEdge((Node) weightEdge.getSourceNode(),
                        (Node) weightEdge.getTargetNode());
                flowGraph.setEdgeWeight((Node) weightEdge.getSourceNode(),
                        (Node) weightEdge.getTargetNode(), minimumCostFlow.getFlow(weightEdge));
            }
        }
        return flowGraph;
    }


}
