package com.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private int lowerBound;
    private int upperBound;
    private long cost;

    public Edge(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    @Override
    public String toString() {
        return "(" + this.getSource() + " : " + this.getTarget() + ")[" + this.getWeight() + "]";
    }
}
