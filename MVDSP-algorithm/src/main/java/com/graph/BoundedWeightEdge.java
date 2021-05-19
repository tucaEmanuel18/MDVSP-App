package com.graph;

public class BoundedWeightEdge extends WeightEdge {
    private int lowerBound;
    private int upperBound;

    public BoundedWeightEdge(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }
}
