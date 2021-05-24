package com.repair;

import com.core.Problem;
import com.core.Route;
import com.graph.GraphUtils;
import com.graph.WeightEdge;

public class PairRouteFixation extends RouteFixation {
    /**
     * a point in our route were is possible to repair this route (merging it with sinkRoute)
     */
    int routeChangePosition;

    /**
     * an infeasible route, complementary with the our route  | a route like->  (j-f1, f1-f2, -...-, fq-i)
     */
    Route pairRoute;
    /**
     * a point in sinkRoute were is possible to repair the sourceRoute
     * (merging the part first part of sourceRoute with the second part of sinkRoute )
     */
    int pairRouteChangePosition;

    public Route getPairRoute() {
        return pairRoute;
    }

    public int getRouteChangePosition() {
        return routeChangePosition;
    }

    public int getPairRouteChangePosition() {
        return pairRouteChangePosition;
    }


    public static RouteFixation create (Route route, Route pairRoute, Problem problem) {
        //check if are infeasible
        if(!route.isFeasible() || !pairRoute.isFeasible() ){
            return new PairRouteFixation();
        }
        // check if are complementary infeasible routes
        if(!route.isComplementary(pairRoute)){
            return new PairRouteFixation();
        }

        //default values for a PairRouteFixation if there is not checked conditions for a repair between these tow routes
        boolean isFixable = false;
        long costPenalty = Long.MAX_VALUE;
        int routeChangePosition = -1;
        int pairRouteChangePosition = -1;

        // Find fixation with minimum penaltyCost
        for(int h = 1; h < route.size() - 3; h++){
            for(int k = 2; k < pairRoute.size() - 2; k++){
                //check if exists h-k edge from this route and other route
                WeightEdge firstEdge = GraphUtils.getEdgeBetween(route.get(h), pairRoute.get(k), problem.getGraph());
                if(firstEdge == null){
                    continue;
                }
                // check if exists edge (k - 1, h + 1) from other route to this route
                WeightEdge secondEdge = GraphUtils.getEdgeBetween(route.get(k - 1), pairRoute.get(h + 1), problem.getGraph());
                if(secondEdge == null){
                    continue;
                }
                long actualCostPenalty = getPairPenaltyCost(route, h, pairRoute, k, problem);

                if(!isFixable || actualCostPenalty < costPenalty) {

                    isFixable = true;
                    costPenalty = actualCostPenalty;
                    routeChangePosition = h;
                    pairRouteChangePosition = k;
                }
            }
        }
        return new PairRouteFixation(route, isFixable, costPenalty, routeChangePosition, pairRoute, pairRouteChangePosition, problem);
    }

    /**
     * Construct a PariRouteFixation which is no Fixable
     */
    private PairRouteFixation(){
        super();
        this.routeChangePosition = -1;
        this.pairRoute = null;
        this.pairRouteChangePosition = -1;
    }

    /**
     * Construct a PairRouteFixation
     * @param route the route which will be repaired
     * @param isFixable true - means: i will fix this route | false - means: is fixed or it can't be fixed
     * @param penaltyCost the penalty cost of repairing this route
     * @param routeChangePosition the position where we merge the sourceRoute with pairRoute
     * @param pairRoute the corresponding infeasible route that allows the repair of the source route
     * @param pairRouteChangePosition the position were we can merge source route to repair it
     */
    private PairRouteFixation(Route route, boolean isFixable, long penaltyCost, int routeChangePosition,
                             Route pairRoute, int pairRouteChangePosition, Problem problem){
        super(route, isFixable, penaltyCost, problem);
        this.routeChangePosition = routeChangePosition;
        this.pairRoute = pairRoute;
        this.pairRouteChangePosition = pairRouteChangePosition;
    }

    /**
     * If sourceRoute can be repaired by merging k->i part of sinkRoute at i->h part of sourceRoute =>
     * => sinkRoute can be also repaired by merging h+1 -> j part of sourceRoute at j->k-1 part of sinkRout
     * @return correspondent RouteFixation that repair sinkRoute
     */
    public RouteFixation getReversePairFixation(){
        return  new PairRouteFixation(this.pairRoute,
                this.isFixable,
                this.penaltyCost,
                this.pairRouteChangePosition - 1,
                this.route,
                this.pairRouteChangePosition+ 1,
                problem
        );
    }

    /**
     * Calculate the penalty cost of repairing this routes with switch in (h = sourceChangePosition, k = sinkChangePosition) point
     * @param route an infeasible route (i-t1, t1-t2, ...-..., tp-j)
     * @param h = sourceChangePosition, a point in route were is possible to repair this route
     * @param pairRoute an complementary infeasible route (j-f1, f1-f2, -...-, fq-i)
     * @param k = sinkChangePosition, a point in pairRoute were is possible to repair this route
     * @param problem actual problem
     * @return the penalty cost of repairing this routes: c(h,k) + c(k-1, h+1) - c(h, h+1) - c(k - 1, k)
     */
    private static long getPairPenaltyCost(Route route, int h, Route pairRoute, int k, Problem problem){

        long routeActualEdge = problem.getPairCost(route.get(h), route.get(h + 1)).toMinutes();
        long routeRepairEdge = problem.getPairCost(route.get(h), pairRoute.get(k)).toMinutes();
        long pairRouteActualEdge = problem.getPairCost(pairRoute.get(k - 1), pairRoute.get(k)).toMinutes();
        long pairRouteRepairEdge = problem.getPairCost(route.get(k - 1), pairRoute.get(h + 1)).toMinutes();

        return routeRepairEdge + pairRouteRepairEdge - routeActualEdge - pairRouteActualEdge;
    }

    public Route getFixedRoute(){
        if(!isFixable){
            System.err.println("called RouteFixation.getFixedRoute() for a not fixable route");
            return null;
        }
        Route fixedRoute = new Route();
        // add locations from our route
        for(int index = 0; index <= routeChangePosition; index++){
            fixedRoute.addLocation(route.get(index));
        }
        //add locations from sinkRoute
        for(int index = pairRouteChangePosition; index <= pairRoute.size() - 1; index++){
            fixedRoute.addLocation(pairRoute.get(index));
        }
        return fixedRoute;
    }
}
