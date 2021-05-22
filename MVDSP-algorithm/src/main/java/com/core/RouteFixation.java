package com.core;

import com.graph.*;
import com.graphPainter.GraphPainter;
import org.jgrapht.Graph;

import java.io.IOException;
import java.util.*;

public abstract class RouteFixation {
    /**
     * true - means: i will fix this route
     * false - means: is fixed or it can't be fixed
     */
    private boolean isFixable;
    /**
     * The cost penalty of this fixation
     * if isFixable is false -> costPenalty must be 0
     */
    private long costPenalty;

    public RouteFixation(boolean isFixable, long costPenalty) {
        this.isFixable = isFixable;
        if(isFixable){
            this.costPenalty = costPenalty;
        }else{
            this.costPenalty = 0;
        }
    }

    public boolean isFixable() {
        return isFixable;
    }

    public long getCostPenalty() {
        return costPenalty;
    }


    /**
     * Given a solution of a minimum-cost circulation problem, this method repair the solution to find a solution for our problem
     * @param flowGraph a solution of the corresponding minimum cost circulation problem
     * @param problem our problem
     * @param problemGraph the mapping to minimum-cost circulation problem, graph
     * @return repaired (if necessary ) list of routes
     */
    public static List<Route> repairSolution(Graph<Node, WeightEdge> flowGraph, Problem problem, Graph<Node, WeightEdge> problemGraph){
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
            Map<Route, List<RouteFixation>> routesFixations = new HashMap<>();
            Set<RouteNode> simpleNodesPartition = new HashSet<>();
            Set<RouteNode> primeNodesPartition = new HashSet<>();

            // map the list of infeasible routes to a bipartite digraph
            Graph<RouteNode, WeightEdge> bipartiteGraph = Mapping.createRepairBipartiteGraph(
                    infeasibleRoutes, problem, problemGraph, routesFixations,
                    simpleNodesPartition, primeNodesPartition);
            try {
                GraphPainter.routePaint(bipartiteGraph, "bipartiteGraph");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // Compute the minimal weight bipartite perfect matching with Kuhn Munkres algorithm
            // to find out for each edge the pair with which it will make the repair
            // (the pair can also be the edge itself)
            Set<WeightEdge> edgesFromMatching =
                    MinimalBipartitePerfectMatchingKuhnMunkres.resolve(
                            bipartiteGraph, simpleNodesPartition, primeNodesPartition);

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
            return getSelfFixedRoute(sourceRoute.getRoute(), (SelfRouteFixation)routeFixation);
        }else{
            //pair Fixation
            RouteFixation routeFixation = getSpecificRouteFixationFromList(
                    sinkRoute.getRoute(), routesFixations.get(sinkRoute.getRoute()));
            if(routeFixation == null){
                System.err.println("Don't find routFixation between " + sourceRoute.getRoute() + " and " + sinkRoute);
                return null;
            }else{
                return getPairFixedRoute(sourceRoute.getRoute(), sinkRoute.getRoute(), (PairRouteFixation) routeFixation);
            }
        }
    }

    public static Route getPairFixedRoute(Route sourceRoute, Route sinkRoute, PairRouteFixation routeFixation){
        Route fixedRoute = new Route();
        // add locations from sourceRoute
        for(int index = 0; index <= routeFixation.getSourceChangePosition(); index++){
            fixedRoute.addLocation(sourceRoute.get(index));
        }
        //add locations from sinkRoute
        for(int index = routeFixation.getSinkChangePosition(); index <= sinkRoute.size() - 1; index++){
            fixedRoute.addLocation(sinkRoute.get(index));
        }
        return fixedRoute;
    }


    public static Route getSelfFixedRoute(Route sourceRoute, SelfRouteFixation routeFixation){
        Route fixedRoute = new Route();
        //Add start depot
        if(routeFixation.isChangeSource()){
            fixedRoute.addLocation(sourceRoute.getEndDepot());
        }else{
            fixedRoute.addLocation(sourceRoute.getStartDepot());
        }
        // Add trips
        for(int index = 1; index <= sourceRoute.size() - 2; index++){
            fixedRoute.addLocation(sourceRoute.get(index));
        }

        //Add final depot
        if(routeFixation.isChangeSource()){
            fixedRoute.addLocation(sourceRoute.getEndDepot());
        }else{
            fixedRoute.addLocation(sourceRoute.getStartDepot());
        }
        return fixedRoute;
    }

    public static RouteFixation getSpecificRouteFixationFromList(Route sinkRoute, List<RouteFixation> routeFixations){
        for(RouteFixation routeFixation : routeFixations){
            if(routeFixation instanceof SelfRouteFixation){
                continue;
            }
            if(((PairRouteFixation)routeFixation).getSinkRoute().equals(sinkRoute)){
                return routeFixation;
            }
        }
        return null;
    }
}
