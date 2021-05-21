package com.core;

import com.graph.GraphUtils;
import com.graph.Node;
import com.graph.RouteNode;
import com.graph.WeightEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Route{
    List<Location> route;

    public Route() {
        route = new ArrayList<>();
    }

    public void addLocation(Location nextLocation){
        route.add(nextLocation);
    }

    public List<Location> getRoute() {
        return route;
    }

    public Location getStartDepot(){
        return route.get(0);
    }

    public Location getEndDepot(){
        return route.get(route.size() - 1);
    }

    public int size(){
        return route.size();
    }

    public Location get(int i){
        return route.get(i);
    }


    public Duration getCost(Problem problem) {

        if (route.size() < 3) {
            throw new IllegalArgumentException();
        }

        // Add the duration between depot and first trip
        Duration cost = problem.getPairCost(route.get(0), route.get(1));
        // Add the duration between last trip and depot
        cost = cost.plus(problem.getPairCost(route.get(route.size() - 2),
                route.get(route.size() - 1))
        );

        // Add the duration between ending time of trip i and starting time of trip i + 1 for all the trips
        for (int i = 1; i < route.size() - 3; ++i) {
            if (route.get(i) instanceof Trip && route.get(i + 1) instanceof Trip) {
                // add cost between t_i and t_i+1
                cost = cost.plus(problem.getPairCost(route.get(i), route.get(i + 1)));
                // add cost of trip t_i
                cost = cost.plus(((Trip) route.get(i)).getTripTimeCost());
            }
        }
        // add cost for the last trip on this route
        if (route.get(route.size() - 2) instanceof Trip) {
            cost = cost.plus(((Trip) route.get(route.size() - 2)).getTripTimeCost());
        }

        return cost;
    }

    public RouteFixation getFixation(Problem problem){
        if(this.isFeasible()){
            return new SelfRouteFixation(false, 0, false);
        }else{
            Integer routeSize = route.size();
            long gamma1 = problem.getPairCost(route.get(routeSize - 2), route.get(0)).toMinutes()
                    - problem.getPairCost(route.get(routeSize - 2), route.get(routeSize - 1)).toMinutes();
            long gamma2 = problem.getPairCost(route.get(routeSize - 1), route.get(1)).toMinutes()
                    - problem.getPairCost(route.get(0), route.get(1)).toMinutes();

            if(gamma1 <= gamma2){
                return new SelfRouteFixation(true, gamma1, false);
            }else{
                return new SelfRouteFixation(true, gamma2, true);
            }
        }
    }

    public RouteFixation getFixation(Route anotherRoute, Problem problem, Graph<Node, WeightEdge> initialGraph){
        if(!this.isFeasible() || !anotherRoute.isFeasible() ){
            return new PairRouteFixation();
        }
        // check if are complementary
        if(!this.isComplementary(anotherRoute.getRoute())){
            return new PairRouteFixation();
        }

        boolean isFixable = false;
        long costPenalty = Long.MAX_VALUE;
        int sourceChangePosition = -1;
        int destinationChangePosition = -1;
        for(int h = 1; h < route.size() - 3; h++){
            for(int k = 2; k < anotherRoute.getRoute().size() - 2; k++){
                WeightEdge firstEdge = GraphUtils.getEdgeBetween(route.get(h), anotherRoute.getRoute().get(k), initialGraph);
                if(firstEdge == null){
                    continue;
                }
                WeightEdge secondEdge = GraphUtils.getEdgeBetween(route.get(k - 1), anotherRoute.getRoute().get(h + 1), initialGraph);
                if(secondEdge == null){
                    continue;
                }
                long actualCostPenalty = problem.getPairPenaltyCost(route, h, anotherRoute.getRoute(), k);
                if(!isFixable){
                    isFixable = true;
                    costPenalty = actualCostPenalty;
                    sourceChangePosition = h;
                    destinationChangePosition = k;
                }else if(actualCostPenalty < costPenalty){
                        costPenalty = actualCostPenalty;
                        sourceChangePosition = h;
                        destinationChangePosition = k;
                }

            }
        }
       return new PairRouteFixation(isFixable, costPenalty, this, sourceChangePosition, anotherRoute, destinationChangePosition);
    }

    private boolean isFeasible(){
        if(route.get(0).equals(route.get(route.size() - 1))){
            return true;
        }
        return false;

    }


    public static void separateRoutes(List<Route> feasibleRoutes, List<Route> infeasibleRoutes, List<Route> allRoutes){
        for(Route route : allRoutes){
            List<Location> actualRoute = route.getRoute();
            if (actualRoute.size() < 3) {
                continue;
            }
            if(route.isFeasible()){
                feasibleRoutes.add(route);
            }else{
                infeasibleRoutes.add(route);
            }
        }
    }


    public boolean isComplementary( List<Location> anotherRoute){
        if(route == null || anotherRoute == null){
            throw new NullPointerException();
        }
        if(route.size() < 3 || anotherRoute.size() < 3){
            throw new IllegalArgumentException("At leat one of this routes is an incorrect route (size must be at least 3)");
        }
        if(!route.get(0).equals(anotherRoute.get(anotherRoute.size() - 1)) || !route.get(route.size() - 1).equals(anotherRoute.get(0))){
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
       StringBuilder result = new StringBuilder();
       result.append("Route{");
       for(Location currentLocation : route){
           result.append(currentLocation + "->");
       }
       result.append("|");
       return result.toString();
    }
}
