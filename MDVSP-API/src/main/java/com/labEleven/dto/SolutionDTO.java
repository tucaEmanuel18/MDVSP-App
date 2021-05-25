package com.labEleven.dto;

import com.MDVSP.core.Route;
import com.MDVSP.core.Solution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SolutionDTO implements Serializable {
    private List<RouteDTO> routes;
    private long totalCost;

    public SolutionDTO(Solution solution){
        routes = new ArrayList<>();
        for(Route route : solution.getRoutes()){
            routes.add(new RouteDTO(route));
        }
        totalCost = solution.getCost();
    }

    public List<RouteDTO> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteDTO> routes) {
        this.routes = routes;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }
}
