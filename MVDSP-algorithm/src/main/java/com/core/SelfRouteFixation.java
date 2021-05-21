package com.core;

public class SelfRouteFixation extends RouteFixation{
    private boolean changeSource;

    public SelfRouteFixation(boolean isFixable, long costPenalty, boolean changeSource){
        super(isFixable, costPenalty);
        this.changeSource = changeSource;
    }

    public boolean isChangeSource() {
        return changeSource;
    }
}
