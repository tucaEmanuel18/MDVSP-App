package com.core;

public abstract class RouteFixation {
    /**
     * true - means: i will fix this route
     * false - means: is fixed or it can't be fixed
     */
    private boolean isFixable;
    /**
     * The cost penalty of this fixation
     * if isFixable is false -> costPenalty must be 0
     */
    private long costPenalty;

    public RouteFixation(boolean isFixable, long costPenalty) {
        this.isFixable = isFixable;
        if(isFixable){
            this.costPenalty = costPenalty;
        }else{
            this.costPenalty = 0;
        }
    }

    public boolean isFixable() {
        return isFixable;
    }

    public long getCostPenalty() {
        return costPenalty;
    }
}
