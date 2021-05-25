package com.MDVSP.core;

import java.util.List;

public class Solution {
    private Problem problem;
    private List<Route> routes;
    private long cost;
    private long costWithTripsCostZero;

    public Solution(List<Route> routes, Problem problem) {
        this.routes = routes;
        this.problem = problem;
        this.cost = 0;
        this.costWithTripsCostZero = 0;
        for(Route route : routes){
            this.cost += route.getCost(problem).toMinutes();
            this.costWithTripsCostZero += route.getCostWithoutTripsCost(problem).toMinutes();
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void print(){
        System.out.println("Solution: \n cost = " + cost);
        for(Route route : routes){
            System.out.println(route
                    + "cost = " + route.getCostWithoutTripsCost(problem).toMinutes()
                    + " | costWithTripsCostZero = " + route.getCostWithoutTripsCost(problem).toMinutes());
        }
    }

    public long getCost() {
        return cost;
    }

    public long getCostWithTripsCostZero() {
        return costWithTripsCostZero;
    }
}
