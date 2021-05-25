package com.labEleven.dto;

import com.MDVSP.core.Location;
import com.MDVSP.core.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteDTO implements Serializable {
    private List<String> locations;
    private long cost;

    public RouteDTO(Route route){
        locations = new ArrayList<>();

        for(Location location : route.getRoute()){
            locations.add(location.getName());
        }
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
