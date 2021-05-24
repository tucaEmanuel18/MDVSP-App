package com.repair;

import com.core.Location;
import com.core.Problem;
import com.core.Route;
import com.repair.RouteFixation;

public class SelfRouteFixation extends RouteFixation {
    /**
     * true -> we must to change source depot with destination depot to repair this route with optimum penalty cost
     *          i-t1, t1-t2, ... , tq-j ->>> j-t1, t1-t2, ... , tq-j
     * false -> we must to change the destination depot with source depot to repair this route
     *          i-t1, t1-t2, ... , tq-j ->>> i-t1, t1-t2, ... , tq-i
     */
    private boolean changeSource;

    public static RouteFixation create (Route route, Problem problem, boolean changeSource) {
        if (route.isFeasible()) {
            return new SelfRouteFixation();
        }
        long penaltyCost = getPenaltyCost(route, problem, changeSource);
        return new SelfRouteFixation(route, true, penaltyCost, false, problem);
    }

    private SelfRouteFixation(Route route, boolean isFixable, long penaltyCost, boolean changeSource, Problem problem){
        super(route, isFixable, penaltyCost, problem);
        this.changeSource = changeSource;
    }

    private SelfRouteFixation(){
        super();
    }

    public boolean isChangeSource() {
        return changeSource;
    }

    public Route getFixedRoute(){
        if(!isFixable){
            System.err.println("called RouteFixation.getFixedRoute() for a not fixable route");
            return null;
        }
        Route fixedRoute = new Route();

        //Add start depot
        if(changeSource){
            fixedRoute.addLocation(route.getEndDepot());
        }else{
            fixedRoute.addLocation(route.getStartDepot());
        }

        // Add trips
        for(int index = 1; index <= route.size() - 2; index++){
            fixedRoute.addLocation(route.get(index));
        }

        //Add final depot
        if(changeSource){
            fixedRoute.addLocation(route.getEndDepot());
        }else{
            fixedRoute.addLocation(route.getStartDepot());
        }
        return fixedRoute;
    }

    public static long getPenaltyCost(Route route, Problem problem, boolean changeSource){
        int size = route.size();
        long actualEdgeCost;
        long repairEdgeCost;

        if(changeSource){
            //cost(route[0], route[1]
            actualEdgeCost = problem.getPairCost(route.get(0), route.get(1)).toMinutes();
            // cost(route[size - 1], route[1])
            repairEdgeCost = problem.getPairCost(route.get(size - 1), route.get(1)).toMinutes();
        }else{
            // cost(route[size - 2], route[size - 1])
            actualEdgeCost = problem.getPairCost(route.get(size - 2), route.get(size - 1)).toMinutes();
            // cost(route[size - 2], route[0])
            repairEdgeCost = problem.getPairCost(route.get(size - 2), route.get(0)).toMinutes();
        }
        return repairEdgeCost - actualEdgeCost;
    }
}
