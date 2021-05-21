package com.graph;

import com.core.*;
import com.graphPainter.GraphPainter;
import org.jgrapht.Graph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import java.io.IOException;
import java.util.*;

public class GraphUtils {
    public static List<Route> repairInfeasibleRoutes(Graph<Node, WeightEdge> flowGraph, Problem problem, Graph<Node, WeightEdge> problemGraph){
        List<Route> feasibleRoutes = new ArrayList<>();
        List<Route> infeasibleRoutes = new ArrayList<>();

        Route.separateRoutes(feasibleRoutes, infeasibleRoutes, getRoutes(flowGraph));

        System.out.println("Fesible Routes:");
        for(Route feasibleRoute : feasibleRoutes){
            System.out.println(feasibleRoute);
        }

        System.out.println("Infesible Routes: ");
        for(Route infeasibleRoute : infeasibleRoutes){
            System.out.println(infeasibleRoute);
        }

        Map<Route, List<RouteFixation>> routesFixations = new HashMap<>();
        Set<RouteNode> simpleNodesPartition = new HashSet<>();
        Set<RouteNode> primeNodesPartition = new HashSet<>();

        Graph<RouteNode, WeightEdge> bipartiteGraph = Mapping.createRepairBipartiteGraph(
                infeasibleRoutes, problem, problemGraph, routesFixations,
                simpleNodesPartition, primeNodesPartition);

        try{
            GraphPainter.routePaint(bipartiteGraph, "bipartiteGraph");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        Set<WeightEdge> edgesFromMatching =
            MinimalBipartitePerfectMatchingKuhnMunkres.resolve(
                    bipartiteGraph, simpleNodesPartition, primeNodesPartition);

        for(WeightEdge edge : edgesFromMatching){
            feasibleRoutes.add(repairRoute(
                    (RouteNode)edge.getSourceNode(),
                    (RouteNode) edge.getTargetNode(),
                    routesFixations)
            );
        }

        System.out.println("Fesible Routes:");
        for(Route feasibleRoute : feasibleRoutes){
            System.out.println(feasibleRoute);
        }
        return feasibleRoutes;
    }


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


    public static List<Route> getRoutes(Graph<Node, WeightEdge> flowGraph){
        List<Route> routes = new ArrayList<>();

        for(Node node : flowGraph.vertexSet()){
            if(node.getLocation() instanceof Depot && node.isExitNode()){
                for(WeightEdge currentEdge : flowGraph.edgesOf(node)){
                    Route actualRoute = new Route();
                    actualRoute.addLocation(node.getLocation());

                    System.out.print(node);
                    GraphIterator<Node, WeightEdge> dfsIterator =
                            new DepthFirstIterator<Node, WeightEdge>(flowGraph, (Node)currentEdge.getTargetNode());
                    while(dfsIterator.hasNext()){
                        Node currentNode = dfsIterator.next();
                        if(!currentNode.isExitNode()) {
                            actualRoute.addLocation(currentNode.getLocation());
                            System.out.print(" -> " + currentNode);
                        }
                    }
                    System.out.println();
                    routes.add(actualRoute);
                }
            }
        }

        return routes;
    }

    public static WeightEdge getEdgeBetween(Location h, Location k, Graph<Node, WeightEdge> graph){
        if(graph.containsEdge(new Node(h, 0, true), new Node(k, 0, false))){
            return graph.getEdge(new Node(h, 0, true), new Node(k, 0, false));
        }
        return null;
    }
}
