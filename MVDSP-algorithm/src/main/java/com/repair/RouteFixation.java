package com.repair;

import com.core.Problem;
import com.core.Route;

public abstract class RouteFixation {
    /**
     * true - means: i will fix this route
     * false - means: is fixed or it can't be fixed
     */
    protected boolean isFixable;
    /**
     * The cost penalty of this fixation
     * if isFixable is false -> costPenalty must be 0
     */
    protected long penaltyCost;

    protected Route route;
    protected Problem problem;

    public RouteFixation(Route route, boolean isFixable, long penaltyCost, Problem problem) {
        this.isFixable = isFixable;
        if(isFixable){
            this.penaltyCost = penaltyCost;
        }else{
            this.penaltyCost = 0;
        }
        this.route = route;
        this.problem = problem;
    }

    public RouteFixation(){
        isFixable = false;
        penaltyCost = -1;
        route = null;
        problem = null;
    }

    public boolean isFixable() {
        return isFixable;
    }

    public long getPenaltyCost() {
        return penaltyCost;
    }

    public abstract Route getFixedRoute();
}
