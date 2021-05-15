package com.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Route {
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
}
