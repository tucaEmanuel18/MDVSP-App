package com.core;

import java.util.List;

public class Solution {
    private Problem problem;
    private List<Route> routes;
    private long cost;

    public Solution(List<Route> routes, Problem problem) {
        this.routes = routes;
        this.problem = problem;
        this.cost = 0;
        for(Route route : routes){
            this.cost += route.getCost(problem).toMinutes();
        }
    }

    public List<Route> getSchedules() {
        return routes;
    }

    public void print(){
        System.out.println("Solution: \n cost = " + cost);
        for(Route route : routes){
            System.out.println(route + "cost = " + route.getCost(problem).toMinutes());
        }
    }
}
