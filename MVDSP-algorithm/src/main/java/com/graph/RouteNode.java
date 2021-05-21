package com.graph;

import com.core.Location;
import com.core.Route;

import java.util.Objects;

public class RouteNode {
    private String name;
    private Route route;
    private boolean isPrime;

    public RouteNode(String name, Route route, boolean isPrime){
        if(route == null){
            throw new NullPointerException();
        }
        this.name = name;
        this.route = route;
        this.isPrime = isPrime;
    }

    public String getName() {
        return name;
    }

    public Route getRoute() {
        return route;
    }

    public RouteNode getCorespondentNode(){
        return new RouteNode(name, route, !isPrime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteNode)) return false;
        RouteNode routeNode = (RouteNode) o;
        return isPrime == routeNode.isPrime && name.equals(routeNode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isPrime);
    }

    @Override
    public String toString() {
        return name + (isPrime?"'":"");
    }

    public boolean isPrime() {
        return isPrime;
    }
}
