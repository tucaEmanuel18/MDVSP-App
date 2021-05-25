package com.MDVSP.core;

import com.MDVSP.flow.FlowGraph;
import com.MDVSP.graph.ProblemMapping;
import com.MDVSP.graph.WeightEdge;
import com.MDVSP.repair.RepairSolution;
import com.MDVSP.graph.Node;
import org.jgrapht.Graph;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {
    private static final Duration DURATION_INFINIT = ChronoUnit.FOREVER.getDuration();
    /**
     * The list of locations (Depots and Trips) from this problem
     */
    private List<Location> locations;
    /**
     * The data structure which store the cost between any two location from this problem
     */
    private Map<LocationPair, Duration> costMap;
    private Integer numberOfDepots = 0 ;
    private Integer numberOfTrips = 0;

    private Graph<Node, WeightEdge> graph;
    /**
     * Check if the problem was modified after generate the problem graph
     */
    private boolean isModified = false;
    /**
     * Construct a new Problem object
     */
    public Problem() {
        this.locations = new ArrayList<>();
        this.costMap = new HashMap<>();
    }

    /**
     * Getter for locations
     * @return List<Location> -> a list with all locations from this problem
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Add a new location into this problem
     * @param newLocation: a Depot or a Trip
     */
    public void addLocation(Location newLocation){
        if(newLocation == null){
            throw new NullPointerException();
        }
        if(locations.contains(newLocation)){
            throw new IllegalArgumentException("Location with this name already exist!");
        }
        for(Location location : locations){
            // Poate vom inlocui DURATION_INFINIT cu "Maps.getDurationBetween(location, newLocation)"
            costMap.put(new LocationPair(location, newLocation), DURATION_INFINIT);
            costMap.put(new LocationPair(newLocation, location), DURATION_INFINIT);

        }
        locations.add(newLocation);
        isModified = true;
        if(newLocation instanceof Depot){
            numberOfDepots++;
        }
        if(newLocation instanceof Trip){
            numberOfTrips++;
        }
    }

    //TODO removeLocation
    public void removeLocation(Location newLocation){
        // to be implemented
        System.out.println("remove Location: is not implemented yet!");
    }

    /**
     * Set cost (duration) between two locations:
     * !! The cost between (L1, L2) can be different than cost between (L2, L1);
     * @param source -> the start location of this measurement
     * @param destination -> the end location of this measurement
     * @param cost -> the duration between these locations
     */
    public void setCost(Location source, Location destination, Duration cost){
        costMap.replace(new LocationPair(source, destination), cost);
        isModified = true;
    }

    /**
     * Get the costMap value between these location
     * @param source -> the start location of this measurement
     * @param destination -> the end location of this measurement
     * @return costMap value between these location
     */
    //TODO getPairCost -> getCost
    private Duration getPairTime(Location source, Location destination){
        if(source == null || destination == null){
            throw new NullPointerException();
        }
        if(!locations.contains(source)){
            throw new IllegalArgumentException("The source location must be added to the problem first!");
        }
        if(!locations.contains(destination)){
            throw new IllegalArgumentException("The source location must be added to the problem first!");
        }

        return costMap.get(new LocationPair(source, destination));
    }

    /**
     * Get the cost between two locations
     * @param source the start location
     * @param destination the end location
     * @return the cost between two locations
     * !!! getPairCost(trip, trip) =  max(travelTime, travelTime + waitingTime)
     */
    public Duration getCost(Location source, Location destination) {
        if (source instanceof Trip && destination instanceof Trip) { // trip -> trip
                // max(travelTime, travelTime + waitingTime)
                return maxBetweenDurations(
                    getPairTime(source, destination),
                    Duration.between(((Trip) source).getEndingTime(), ((Trip) destination).getStartingTime())
                );
        }
        // depot -> trip | trip -> depot | -> depot -> depot
        return getPairTime(source, destination);
    }

    public Graph<Node, WeightEdge> getGraph(){
        if(this.graph == null || isModified){
           graph = ProblemMapping.createGraph(this);
           isModified = false;
        }
        return graph;
    }
    /**
     * Check if we have at least one depot and at least one trip
     * @return `true` if the above condition if checked and false otherwise
     */
    private boolean isRunnable(){
        boolean isRunnable = true;
        if(numberOfDepots < 1 || numberOfTrips < 1){
            isRunnable = false;
        }
        return isRunnable;
    }

    /**
     * Resolve the problem and get a solution;
     * 1. Map our problem to a minimum-cost circulation problem
     * 2. Solve the new problem with the successive shortest path algorithm with capacity scaling
     * 3. Repair the obtained solution to find a solution for our problem
     * @return a Solution object
     * @throws IOException if we print some graphs :)
     */
    public Solution getSolution() throws IOException {
        if(!isRunnable()){
            System.out.println("The problem is not runnable! Please check your depots and trips!");
            return null;
        }
        //1. Map our problem to a minimum-cost circulation problem
        Graph<Node, WeightEdge> graph = this.getGraph();

        //GraphPainter.paint(graph);

        //2. Solve the new problem with the successive shortest path algorithm with capacity scaling
        // (find the minimum cost flow)
        FlowGraph flowGraph = new FlowGraph(graph);

        //3. Repair the obtained solution to find a solution for our problem
        // -- in the obtained solution we can have routes that start from a depot and finish in another depot
        // -- repairing this solution means repairing this infeasible routes.
        RepairSolution repairSolution  = new RepairSolution(flowGraph.getFlowGraph(), this);
        return new Solution(repairSolution.getRepairedSolution(),this);
    }

    /**
     * Print the problem details
     */
    public void print(){
        System.out.println("Problem");
        System.out.println("Locations:");
        for(Location location : locations){
            System.out.println(location.toString());
        }

        System.out.println("costMap:");
        for(LocationPair key : costMap.keySet()){
            System.out.println(key.getSource() + " | " + key.getDestination() +  " -> " + costMap.get(key));
        }
    }


    @Override
    public String toString() {
        return "Problem{" +
                "locations=" + locations +
                ", costMap=" + costMap +
                '}';
    }


    /////////////////////////////////////////////////////////

    /**
     * Get max duration between two durations
     * @param d1
     * @param d2
     * @return max duration between two durations
     */
    private Duration maxBetweenDurations(Duration d1, Duration d2){
        if(d1.compareTo(d2) >= 0){
            return d1;
        }else{
            return d2;
        }
    }
//////////////////////////////////////////////////////////////////

}
