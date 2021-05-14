package com.core;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {
    private static final Duration DURATION_INFINIT = ChronoUnit.FOREVER.getDuration();;
    private List<Location> locations;
    private Map<LocationPair, Duration> costMap;
    private Integer numberOfDepots = 0 ;
    private Integer numberOfTrips = 0;

    public Problem() {
        /*this.trips = new ArrayList<>();
        this.depots = new ArrayList<>();*/
        this.locations = new ArrayList<>();
        this.costMap = new HashMap<>();
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

    private boolean contains(LocationPair pair){
        return costMap.containsKey(pair);
    }

    public void setPairCost(Location source, Location destination, Duration cost){
        costMap.replace(new LocationPair(source, destination), cost);
    }

    public Duration getPairCost(Location source, Location destination){
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

    private boolean checkIfRunnable(){
        boolean isRunnable = true;
        if(numberOfDepots < 1 || numberOfTrips < 1){
            isRunnable = false;
        }
        return isRunnable;
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


    @Override
    public String toString() {
        return "Problem{" +
                "locations=" + locations +
                ", costMap=" + costMap +
                '}';
    }
}
