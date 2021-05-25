package com.MDVSP.repair;

import com.MDVSP.core.Problem;
import com.MDVSP.core.Route;
import com.MDVSP.graph.WeightEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.*;

public class BipartiteGraph {
    private Problem problem;
    private boolean changeSource;
    private Set<RouteNode> simpleNodesPartition;
    private Set<RouteNode> primeNodesPartition;
    private Graph<RouteNode, WeightEdge> bipartiteGraph;
    private Map<Route, List<RouteFixation>> routesFixations;
    private long counter;


    public BipartiteGraph(List<Route> infeasibleRoutes, Problem problem, boolean changeSource) {
        this.problem = problem;
        this.changeSource = changeSource;
        simpleNodesPartition = new HashSet<>();
        primeNodesPartition = new HashSet<>();
        bipartiteGraph = new DefaultDirectedWeightedGraph<>(WeightEdge.class);
        routesFixations = new HashMap<>();
        counter = 0;
        createBipartiteGraph(infeasibleRoutes);
    }

    private void createBipartiteGraph(List<Route> infeasibleRoutes){
        for(Route route : infeasibleRoutes){
            RouteFixation selfRouteFixation = SelfRouteFixation.create(route, problem, changeSource);

            if(!selfRouteFixation.isFixable()){
                System.err.println("createBipartiteGraph: In list of infeasible routes I receive a feasible route: " + route);
                continue;
            }

            // For each route P , add two nodes P and P' and edge between them
            RouteNode actualNode = new RouteNode("P" + counter, route, false);
            RouteNode actualPrimeNode = new RouteNode("P" + counter++, route, true);
            bipartiteGraph.addVertex(actualNode);
            bipartiteGraph.addVertex(actualPrimeNode);

            // Save the selfRouteFixation
            routesFixations.put(route, new ArrayList<>());
            routesFixations.get(route).add(selfRouteFixation);

            // Add edge between P and P'
            bipartiteGraph.addEdge(actualNode, actualPrimeNode);
            bipartiteGraph.setEdgeWeight(actualNode, actualPrimeNode, selfRouteFixation.getPenaltyCost());


            // add edge between actual nodes, and inserted nodes
            for(RouteNode anotherPrimeNode : primeNodesPartition){
                RouteNode anotherSimpleNode = anotherPrimeNode.getCorespondentNode();

                RouteFixation pairRouteFixation = PairRouteFixation.create(route, anotherPrimeNode.getRoute(), problem);
                if(pairRouteFixation.isFixable()){
                    // add edge from actualNode to anotherPrimeNode
                    bipartiteGraph.addEdge(actualNode, anotherPrimeNode);
                    bipartiteGraph.setEdgeWeight(actualNode, anotherPrimeNode, pairRouteFixation.getPenaltyCost());

                    routesFixations.get(route).add(pairRouteFixation);

                    // add edge from anotherSimpleNode to actualPrimeNode
                    RouteFixation reversePairRouteFixation = ((PairRouteFixation) pairRouteFixation).getReversePairFixation();

                    bipartiteGraph.addEdge(anotherSimpleNode, actualPrimeNode);
                    bipartiteGraph.setEdgeWeight(anotherSimpleNode, actualPrimeNode, reversePairRouteFixation.getPenaltyCost());

                    routesFixations.get(anotherSimpleNode.getRoute()).add(reversePairRouteFixation);
                }else{
                    // The graph must be complete so if this pair of route is not Fixable, we add edge with Infinite cost between them
                    bipartiteGraph.addEdge(actualNode, anotherPrimeNode);
                    bipartiteGraph.setEdgeWeight(actualNode, anotherPrimeNode, Long.MAX_VALUE);

                    bipartiteGraph.addEdge(anotherSimpleNode, actualPrimeNode);
                    bipartiteGraph.setEdgeWeight(anotherSimpleNode, actualPrimeNode, Long.MAX_VALUE);
                }
            }
            // add actual nodes to graph partitions
            simpleNodesPartition.add(actualNode);
            primeNodesPartition.add(actualPrimeNode);
        }
    }

    public Set<RouteNode> getSimpleNodesPartition() {
        return simpleNodesPartition;
    }

    public Set<RouteNode> getPrimeNodesPartition() {
        return primeNodesPartition;
    }

    public Graph<RouteNode, WeightEdge> getGraph() {
        return bipartiteGraph;
    }

    public Map<Route, List<RouteFixation>> getRoutesFixations() {
        return routesFixations;
    }
}
