package com.core;

import com.graph.*;
import com.graphPainter.GraphPainter;
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
    private List<Location> locations;
    private Map<LocationPair, Duration> costMap;
    private Integer numberOfDepots = 0 ;
    private Integer numberOfTrips = 0;

    public Problem() {
        this.locations = new ArrayList<>();
        this.costMap = new HashMap<>();
    }

    public List<Location> getLocations() {
        return locations;
    }

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

    public void setPairCost(Location source, Location destination, Duration cost){
        costMap.replace(new LocationPair(source, destination), cost);
    }

    public Duration getPairTime(Location source, Location destination){
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

    public Duration getPairCost(Location source, Location destination) {
        if (source instanceof Trip && destination instanceof Trip) { // trip -> trip
                // max(deplasationTime, deplasationTime + waitingTime)
                return maxBetweenDurations(
                    getPairTime(source, destination),
                    Duration.between(((Trip) source).getEndingTime(), ((Trip) destination).getStartingTime())
                );
        }
        // else
        // depot -> trip | trip -> depot | -> depot -> depot
        return getPairTime(source, destination);
    }

    private boolean checkIfRunnable(){
        boolean isRunnable = true;
        if(numberOfDepots < 1 || numberOfTrips < 1){
            isRunnable = false;
        }
        return isRunnable;
    }

    public Duration getCostOfRoute(Route route) {
        List<Location> actualRoute = route.getRoute();
        if (actualRoute.size() < 3) {
            throw new IllegalArgumentException();
        }

        // Add the duration between depot and first trip
        Duration cost = getPairCost(actualRoute.get(0), actualRoute.get(1));
        // Add the duration between last trip and depot
        cost = cost.plus(getPairCost(actualRoute.get(actualRoute.size() - 2),
                actualRoute.get(actualRoute.size() - 1))
        );

        // Add the duration between ending time of trip i and starting time of trip i + 1 for all the trips
        for (int i = 1; i < actualRoute.size() - 3; ++i) {
            if (actualRoute.get(i) instanceof Trip && actualRoute.get(i + 1) instanceof Trip) {
                // add cost between t_i and t_i+1
                cost = cost.plus(getPairCost(actualRoute.get(i), actualRoute.get(i + 1)));
                // add cost of trip t_i
                cost = cost.plus(((Trip) actualRoute.get(i)).getTripTimeCost());
            }
        }
        // add cost for the last trip on this route
        if (actualRoute.get(actualRoute.size() - 2) instanceof Trip) {
            cost = cost.plus(((Trip) actualRoute.get(actualRoute.size() - 2)).getTripTimeCost());
        }

        return cost;
    }

    public void resolve() throws IOException {
        Graph<Node, WeightEdge> graph = Mapping.createGraph(this);
        GraphPainter.paint(graph);

        SuccessiveShortestPathAlgorithmWithCapacityScaling.resolve(graph);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "locations=" + locations +
                ", costMap=" + costMap +
                '}';
    }

    // AUXILIAR METHODS
    private boolean contains(LocationPair pair){
        return costMap.containsKey(pair);
    }


    private Duration maxBetweenDurations(Duration d1, Duration d2){
        if(d1.compareTo(d2) >= 0){
            return d1;
        }else{
            return d2;
        }
    }

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

}
