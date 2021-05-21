package com.core;

import java.util.List;

public class PairRouteFixation extends RouteFixation{
    /**
     * an infeasible route like -> (i-t1, t1-t2, ...-..., tp-j)
     */
    Route sourceRoute;
    /**
     * a point in sourceRoute were is possible to repair this route (merging it with sinkRoute)
     */
    int sourceChangePosition;

    /**
     * an infeasible route, complementary with the sourceRoute  | a route like->  (j-f1, f1-f2, -...-, fq-i)
     */
    Route sinkRoute;
    /**
     * a point in sinkRoute were is possible to repair the sourceRoute
     * (merging the part first part of sourceRoute with the second part of sinkRoute )
     */
    int sinkChangePosition;

    /**
     * Construct a PariRouteFixation which is no Fixable
     */
    public PairRouteFixation(){
        super(false, 0);
        this.sourceRoute = null;
        this.sourceChangePosition = -1;
        this.sinkRoute = null;
        this.sinkChangePosition = -1;
    }

    /**
     * Construct a PairRouteFixation
     * @param isFixable
     * @param costPenalty
     * @param sourceRoute
     * @param sourceChangePosition
     * @param sinkRoute
     * @param sinkChangePosition
     */
    public PairRouteFixation(boolean isFixable, long costPenalty,
                             Route sourceRoute, int sourceChangePosition,
                             Route sinkRoute, int sinkChangePosition){
        super(isFixable, costPenalty);
        this.sourceRoute = sourceRoute;
        this.sourceChangePosition = sourceChangePosition;
        this.sinkRoute = sinkRoute;
        this.sinkChangePosition = sinkChangePosition;
    }

    public RouteFixation getReversePairFixation(){
        return  new PairRouteFixation(
                this.isFixable(),
                this.getCostPenalty(),
                this.getSinkRoute(),
                this.getSinkChangePosition() - 1,
                this.getSourceRoute(),
                this.getSourceChangePosition() + 1
        );
    }


    /**
     * Calculate the penalty cost of repairing sourceRoute by merging it in
     * point h = sourceChangePosition with the sinkRoute after k = sinkChangePosition
     * The sourceRoute after fix will be like: (i-t1, t1-t2, ...-... th-fk fk-fk+1 ...-... fq-i)
     * @return the penalty cost of repairing this routes: c(h,k) + c(k-1, h+1) - c(h, h+1) - c(k - 1, k)
     */
  /*  public long getPairPenaltyCost(Problem problem){
        int h = sourceChangePosition;
        int k = sinkChangePosition;

        return problem.getPairCost(sourceRoute.get(h), sinkRoute.get(k)).toMinutes()
                + problem.getPairCost(sourceRoute.get(h + 1), sinkRoute.get(k + 1)).toMinutes()
                - problem.getPairCost(sourceRoute.get(h), sourceRoute.get(h + 1)).toMinutes()
                - problem.getPairCost(sinkRoute.get(k + 1), sinkRoute.get(k)).toMinutes();
    }*/

    public Route getSourceRoute() {
        return sourceRoute;
    }

    public Route getSinkRoute() {
        return sinkRoute;
    }

    public int getSourceChangePosition() {
        return sourceChangePosition;
    }

    public int getSinkChangePosition() {
        return sinkChangePosition;
    }


}
