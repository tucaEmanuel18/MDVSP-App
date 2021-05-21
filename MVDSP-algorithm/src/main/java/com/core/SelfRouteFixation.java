package com.core;

public class SelfRouteFixation extends RouteFixation{
    /**
     * true -> we must to change source depot with destination depot to repair this route with optimum penalty cost
     *          i-t1, t1-t2, ... , tq-j ->>> j-t1, t1-t2, ... , tq-j
     * false -> we must to change the destination depot with source depot to repair this route
     *          i-t1, t1-t2, ... , tq-j ->>> i-t1, t1-t2, ... , tq-i
     */
    private boolean changeSource;


    public SelfRouteFixation(boolean isFixable, long costPenalty, boolean changeSource){
        super(isFixable, costPenalty);
        this.changeSource = changeSource;
    }

    public boolean isChangeSource() {
        return changeSource;
    }
}
