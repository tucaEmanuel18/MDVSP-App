package com.graph;

import com.core.Depot;
import com.core.Location;
import com.core.Problem;
import com.core.Trip;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Mapping {

    public static Graph<Node, Edge> createGraph(Problem problem){
        Graph<Node, Edge> graph = new DefaultDirectedWeightedGraph<>(Edge.class);
        List<Node> depots = new ArrayList<>();
        List<Node> trips = new ArrayList<>();

        for(Location location : problem.getLocations()){
            if(location instanceof Depot){
                addDepotToGraph(((Depot) location), graph, depots, trips, problem);
            }

            if(location instanceof Trip){
                addTripToGraph(((Trip) location), graph, depots, trips, problem);
            }
        }
        return graph;
    }

    private static void addDepotToGraph(Depot newDepot, Graph<Node, Edge> graph, List<Node> depots, List<Node> trips, Problem problem){
        // For each depot Di, add two nodes in V(G), representing a source and a sing
        // The capacity for source is number of available vehicles (the supply);
        // The capacity for sink is negate of number of available vehicles (the demand);
        // add the depot source vertex
        Node depotSource = new Node(newDepot, newDepot.getNumberOfVehicles(), true);
        graph.addVertex(depotSource);
        depots.add(depotSource);

        // add the depot sink vertex
        Node depotSink = new Node(newDepot, -newDepot.getNumberOfVehicles(), false);
        graph.addVertex(depotSink);
        depots.add(depotSink);

        // add edges from this depot source to all existing trips entry
        // add edges from all existing trips exit to this depot sink
        for(Node trip : trips){
            if(!trip.isExitNode()){
                // from depot source to trip startPoint
                Edge depotToTripEdge = new Edge(0, 1);
                graph.addEdge(depotSource, trip, depotToTripEdge);
                graph.setEdgeWeight(depotToTripEdge, problem.getPairTime(depotSource.getLocation(), trip.getLocation()).toMinutes());
            }else{ //ending point
                // from trip endPoint to depot sink
                Edge tripToDepotEdge = new Edge(0, 1);
                graph.addEdge(trip, depotSink, tripToDepotEdge);
                graph.setEdgeWeight(tripToDepotEdge, problem.getPairTime(trip.getLocation(), depotSink.getLocation()).toMinutes());
            }
        }

        // add arc from depot source to depot sink with lowerBound: 0 end upperBound: depot.numberOfAvailableVehicles
        // this arc is necessary when some vehicles in the depot are nod used
        Integer numberOfAvailableVehicles = ((Depot) depotSource.getLocation()).getNumberOfVehicles();
        Edge sourceToSinkEdge = new Edge(0, numberOfAvailableVehicles);
        graph.addEdge(depotSource, depotSink, sourceToSinkEdge);
        graph.setEdgeWeight(sourceToSinkEdge, 0);

    }

    private static void addTripToGraph(Trip newTrip, Graph<Node, Edge> graph, List<Node> depots, List<Node> trips, Problem problem){
        // For each trip add two nodes representing the startPoint and endPoint of a trip
        // The capacity of these nodes is 0 (are transit nodes)
        // create and add these this vertexes
        Node tripStart = new Node(newTrip, 0, false);
        Node tripEnd = new Node(newTrip, 0, true);

        graph.addVertex(tripStart);
        graph.addVertex(tripEnd);

        // Add edge from trip startPoint to trip endPoint
        // lowerBound and upperBound for this edges is 1, because we are looking for solution that saturates all trips
        Edge tripInternalEdge = new Edge(1,1);
        graph.addEdge(tripStart, tripEnd, tripInternalEdge);
        graph.setEdgeWeight(tripInternalEdge, 0);

        for(Node depot : depots){
            if(depot.isExitNode()){ // is source
                // add edge from depot source to trip startPoint
                Edge depotToTripEdge = new Edge(0, 1);
                graph.addEdge(depot, tripStart, depotToTripEdge);
                graph.setEdgeWeight(depotToTripEdge, problem.getPairTime(depot.getLocation(), tripStart.getLocation()).toMinutes());
            }else{ // is depot sink
                // add edge from trip endPoint to depot sink
                Edge tripToDepotEdge = new Edge(0, 1);
                graph.addEdge(tripEnd, depot, tripToDepotEdge);
                graph.setEdgeWeight(tripToDepotEdge, problem.getPairTime(tripEnd.getLocation(), depot.getLocation()).toMinutes());
            }
        }

        for(Node otherTrip : trips){
            if(otherTrip.isExitNode()){
                // add feasible edges from all trips endPoints to this trip startPoint
                if(isFeasibleEdge(otherTrip, tripStart, problem)){
                    Edge otherTripToThisTrip =  new Edge(0, 1);
                    graph.addEdge(otherTrip, tripStart, otherTripToThisTrip);
                    graph.setEdgeWeight(otherTripToThisTrip, problem.getPairCost(otherTrip.getLocation(), tripStart.getLocation()).toMinutes());
                }
            }else{
                // add feasible edges from this trip endPoint to all trips startPoints
                if(isFeasibleEdge(tripEnd, otherTrip, problem)){
                    Edge thisTripToOtherTrip = new Edge(0, 1);
                    graph.addEdge(tripEnd, otherTrip, thisTripToOtherTrip);
                    graph.setEdgeWeight(thisTripToOtherTrip, problem.getPairCost(tripEnd.getLocation(), otherTrip.getLocation()).toMinutes());
                }
            }
        }

        // add this nodes in our trips list;
        trips.add(tripStart);
        trips.add(tripEnd);
    }

    private static boolean isFeasibleEdge(Node tripEndPoint, Node tripStartPoint, Problem problem){
        if(tripEndPoint == null || tripStartPoint == null){
            throw new NullPointerException();
        }
        // check if the tripEndPoint is an endPoint and tripStartPoint is an startPoint
        if(!tripEndPoint.isExitNode() || tripStartPoint.isExitNode()){
            throw new IllegalArgumentException("isFeasibleEdge: The first parameter must be a endPoint end second parameter must be a startPoint");
        }

        // check if this nodes really belongs to trips
        if(!(tripEndPoint.getLocation() instanceof Trip) || !(tripStartPoint.getLocation() instanceof Trip)){
            throw new IllegalArgumentException("isFeasibleEdge: both parameters must be trips nodes! At least one of the parameters is not a trip!");
        }

        // check if this nodes belongs to the same trip
        if(tripEndPoint.getLocation().equals(tripStartPoint.getLocation())){
            throw new IllegalArgumentException("isFeasibleEdge: the starting point and the end point do not have to belong to the same trip!");
        }

        // if feasible edge if tripEndPoint.time + time between this two points <= tripStartPoint.time
        LocalTime tripEndPointTime = ((Trip)tripEndPoint.getLocation()).getEndingTime();
        Duration  timeBetweenTheseTrips = problem.getPairTime(tripEndPoint.getLocation(), tripStartPoint.getLocation());
        LocalTime tripStartPointTime = ((Trip)tripStartPoint.getLocation()).getStartingTime();

        if((tripEndPointTime.plus(timeBetweenTheseTrips)).compareTo(tripStartPointTime) <= 0){
            return true;
        }else{
            return false;
        }
    }
}
