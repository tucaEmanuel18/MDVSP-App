package com.repair;
import com.core.Problem;

import com.core.Route;
import com.graph.GraphUtils;
import com.graph.Node;
import com.graph.WeightEdge;
import org.jgrapht.Graph;

import java.util.*;

public class RepairSolution {
    private Graph<Node, WeightEdge> flowGraph;
    private Problem problem;
    List<Route> repairedSolution;



    public RepairSolution(Graph<Node, WeightEdge> flowGraph, Problem problem){
        if(flowGraph == null || problem == null){
            throw new NullPointerException();
        }
        this.flowGraph = flowGraph;
        this.problem = problem;

        repairedSolution = repairSolution();
    }

    public List<Route> getRepairedSolution() {
        return repairedSolution;
    }


    /**
     * Given a solution of a minimum-cost circulation problem, this method repair the solution to find a solution for our problem
     * @return repaired (if necessary ) list of routes
     */
    public List<Route> repairSolution(){
        List<Route> feasibleRoutes = new ArrayList<>();
        List<Route> infeasibleRoutes = new ArrayList<>();

        //Get list of routes from flow graph and separates these routes into feasible/infeasible routes
        Route.separateRoutes(feasibleRoutes, infeasibleRoutes, GraphUtils.getRoutes(flowGraph));

        System.out.println("Feasible Routes");
        for(Route route : feasibleRoutes){
            System.out.println(route + "->" + route.getCostWithoutTripsCost(problem).toMinutes());
        }

        System.out.println("InFeasible Routes");
        for(Route route : infeasibleRoutes){
            System.out.println(route + "->" + route.getCostWithoutTripsCost(problem).toMinutes());
        }

        // repair the infeasible routes
        if(infeasibleRoutes.size() > 0) {
            // map the list of infeasible routes to a bipartite digraph
            BipartiteGraph bipartiteGraph1 = new BipartiteGraph(infeasibleRoutes, problem, true);
            BipartiteGraph bipartiteGraph2 = new BipartiteGraph(infeasibleRoutes, problem, false);

            // Compute the minimal weight bipartite perfect matching with Kuhn Munkres algorithm
            // to find out for each edge the pair with which it will make the repair
            // (the pair can also be the edge itself)
            MinimalBipartitePerfectMatching minimalPerfectMatching1 = new MinimalBipartitePerfectMatching(bipartiteGraph1);
            MinimalBipartitePerfectMatching minimalPerfectMatching2 = new MinimalBipartitePerfectMatching(bipartiteGraph2);

            // Select the matching that have a less weight
            Set<WeightEdge> edgesFromMatching;
            Map<Route, List<RouteFixation>> routesFixations;
            if(minimalPerfectMatching1.getWeight() < minimalPerfectMatching2.getWeight()){
                edgesFromMatching = minimalPerfectMatching1.getEdges();
                routesFixations = bipartiteGraph1.getRoutesFixations();
            }else{
                edgesFromMatching = minimalPerfectMatching2.getEdges();
                routesFixations = bipartiteGraph2.getRoutesFixations();
            }

            // Apply the reparation process for all edges
            for (WeightEdge edge : edgesFromMatching) {
                feasibleRoutes.add(repairRoute(
                        (RouteNode) edge.getSourceNode(),
                        (RouteNode) edge.getTargetNode(),
                        routesFixations)
                );
            }
        }
        return feasibleRoutes;
    }



    /**
     * Given a infeasible route (sourceRoute) and it's infeasible route correspondent,
     * this method apply the process of repairing
     * @param sourceRoute an infeasible route on which correction is applied
     * @param sinkRoute the correspondent route with which the correction is made
     * @param routesFixations the map with all possible corrections
     * @return the repaired route
     */
    private static Route repairRoute(RouteNode sourceRoute, RouteNode sinkRoute,Map<Route, List<RouteFixation>> routesFixations){

        if(sourceRoute.getName().equals(sinkRoute.getName())){
            // self Fixation
            RouteFixation routeFixation = routesFixations.get(sourceRoute.getRoute()).get(0);
            return routeFixation.getFixedRoute();
        }else{
            //pair Fixation
            RouteFixation routeFixation = getSpecificRouteFixationFromList(
                    sinkRoute.getRoute(), routesFixations.get(sinkRoute.getRoute()));
            if(routeFixation == null){
                System.err.println("Don't find routFixation between " + sourceRoute.getRoute() + " and " + sinkRoute);
                return null;
            }else{
                return routeFixation.getFixedRoute();
            }
        }
    }

    public static RouteFixation getSpecificRouteFixationFromList(Route sinkRoute, List<RouteFixation> routeFixations){
        for(RouteFixation routeFixation : routeFixations){
            if(routeFixation instanceof SelfRouteFixation){
                continue;
            }
            if(((PairRouteFixation)routeFixation).getPairRoute().equals(sinkRoute)){
                return routeFixation;
            }
        }
        return null;
    }

}