package com.graph;

import com.core.Location;

public class Node{
    private Location location;
    private Integer capacity;
    private boolean isExitNode;

    public Node(Location location, Integer capacity, boolean isExitNode) {
        this.location = location;
        this.capacity = capacity;
        this.isExitNode = isExitNode;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public boolean isExitNode() {
        return isExitNode;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(location.getName());
        if(isExitNode){
            result.append("-");
        }
        else{
            result.append("+");
        }
        return result.toString();
    }
}
