package com.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightEdge extends DefaultWeightedEdge {
    public Object getSourceNode(){
        return this.getSource();
    }

    public Object getTargetNode(){
        return this.getTarget();
    }

    public int getEdgeWeight(){
        return this.getEdgeWeight();
    }
    @Override
    public String toString() {
        return "(" + this.getSource() + " : " + this.getTarget() + ")[" + this.getWeight() + "]";
    }
}
