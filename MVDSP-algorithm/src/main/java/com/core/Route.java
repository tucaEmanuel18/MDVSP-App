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
    /**
     * Ordered list of locations starting and finishing with a depot
     */
    List<Location> route;

    /**
     * Construc a new Route
     */
    public Route() {
        route = new ArrayList<>();
    }

    /**
     * Add a new location at the end of this route
     * @param nextLocation
     */
    public void addLocation(Location nextLocation){
        route.add(nextLocation);
    }

    /**
     * @return the ordered list of locations that forms this route
     */
    public List<Location> getRoute() {
        return route;
    }

    /**
     * @return the first location from this route (which should be a depot)
     */
    public Location getStartDepot(){
        return route.get(0);
    }

    /**
     * @return the last location from this route (which should be a depot)
     */
    public Location getEndDepot(){
        return route.get(route.size() - 1);
    }

    public int size(){
        return route.size();
    }

    public Location get(int i){
        return route.get(i);
    }

    /**
     *
     * @param problem: the problem it belongs to
     * @return the cost of this route
     */
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

    /**
     * @param problem: the problem it belongs to
     * @return the optimum SelfRouteFixation (if isFixable)
     */
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

    /**
     * Get the PairRouteFixation with optimum penalty cost between this route and the given route (if it exists)
     * @param anotherRoute an infeasible route
     * @param problem: the problem it belongs to
     * @param initialGraph the graph obtained after mapping our problem to a minimum-cost circulation problem (After Step1)
     * @return the optimum PairRouteFixation between this route and given route (if exists)
     */
    public RouteFixation getFixation(Route anotherRoute, Problem problem, Graph<Node, WeightEdge> initialGraph){
        //check if are infeasible
        if(!this.isFeasible() || !anotherRoute.isFeasible() ){
            return new PairRouteFixation();
        }
        // check if are complementary infeasible routes
        if(!this.isComplementary(anotherRoute)){
            return new PairRouteFixation();
        }

        //default values for a PairRouteFixation if there is not checked conditions for a repair between these tow routes
        boolean isFixable = false;
        long costPenalty = Long.MAX_VALUE;
        int sourceChangePosition = -1;
        int destinationChangePosition = -1;


        for(int h = 1; h < route.size() - 3; h++){
            for(int k = 2; k < anotherRoute.size() - 2; k++){
                //check if exists h-k edge from this route and other route
                WeightEdge firstEdge = GraphUtils.getEdgeBetween(route.get(h), anotherRoute.get(k), initialGraph);
                if(firstEdge == null){
                    continue;
                }
                // check if exists edge (k - 1, h - 1) from other route to this route
                WeightEdge secondEdge = GraphUtils.getEdgeBetween(route.get(k - 1), anotherRoute.get(h + 1), initialGraph);
                if(secondEdge == null){
                    continue;
                }

                long actualCostPenalty = problem.getPairPenaltyCost(route, h, anotherRoute.getRoute(), k);

                if(!isFixable || actualCostPenalty < costPenalty) {

                    isFixable = true;
                    costPenalty = actualCostPenalty;
                    sourceChangePosition = h;
                    destinationChangePosition = k;
                }
            }
        }
       return new PairRouteFixation(isFixable, costPenalty, this, sourceChangePosition, anotherRoute, destinationChangePosition);
    }

    /**
     * A route is Feasible if start and finish with the same depot
     * @return `true` if start an finish with the same depot and `false` otherwise
     */
    private boolean isFeasible(){
        if(route.get(0).equals(route.get(route.size() - 1))){
            return true;
        }
        return false;

    }

    /**
     * Check if this and another route are complementary
     * thisRoute: i-t1, t1-t2, -...-, tq-j
     * anotherRoute: j-f1, f1-f2, -...-, fp-i
     * @param anotherRoute an infeasible routes
     * @return 'true' if are complementary infeasible routes,  `false` otherwise
     */
    // TODO
    public boolean isComplementary( Route anotherRoute){
        if(route == null || anotherRoute == null){
            throw new NullPointerException();
        }
        // check if is a correct route (at least one trip)
        if(route.size() < 3 || anotherRoute.size() < 3){
            throw new IllegalArgumentException("At leat one of this routes is an incorrect route (size must be at least 3)");
        }
        //check if is complementary
        if(!route.get(0).equals(anotherRoute.get(anotherRoute.size() - 1)) || !route.get(route.size() - 1).equals(anotherRoute.get(0))){
            return false;
        }
        return true;
    }


    /**
     * this method separates a lists of routes into two lists: a list of feasibleRoutes and a list of infeasibleRoutes
     * @param feasibleRoutes a empty list which will be populate with feasibleRoutes
     * @param infeasibleRoutes a empty list which will be populate with infeasibleRoutes
     * @param allRoutes a list with feasible and infeasible routes which need to be separately
     */
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
