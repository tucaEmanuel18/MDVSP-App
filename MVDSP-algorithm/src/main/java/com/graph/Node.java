package com.graph;

import com.core.Location;

import java.util.Objects;

public class Node {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return isExitNode == node.isExitNode && location.equals(node.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, isExitNode);
    }
}
