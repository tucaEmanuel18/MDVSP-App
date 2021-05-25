package com.MDVSP.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Route{
    /**
     * Ordered list of locations starting and finishing with a depot
     */
    List<Location> route;

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
        Duration cost = problem.getCost(route.get(0), route.get(1));
        // Add the duration between last trip and depot
        cost = cost.plus(problem.getCost(route.get(route.size() - 2),
                route.get(route.size() - 1))
        );

        // Add the duration between ending time of trip i and starting time of trip i + 1 for all the trips
        for (int i = 1; i <= route.size() - 3; ++i) {
            if (route.get(i) instanceof Trip && route.get(i + 1) instanceof Trip) {
                // add cost between t_i and t_i+1
                cost = cost.plus(problem.getCost(route.get(i), route.get(i + 1)));
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

    public Duration getCostWithoutTripsCost(Problem problem){
        if (route.size() < 3) {
            throw new IllegalArgumentException();
        }
        Duration cost =  Duration.ZERO;


        for (int i = 0; i <= route.size() - 2; ++i) {
            cost = cost.plus(problem.getCost(route.get(i), route.get(i+1)));
        }
        return cost;
    }

    /**
     * A route is Feasible if start and finish with the same depot
     * @return `true` if start an finish with the same depot and `false` otherwise
     */
    public boolean isFeasible(){
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
