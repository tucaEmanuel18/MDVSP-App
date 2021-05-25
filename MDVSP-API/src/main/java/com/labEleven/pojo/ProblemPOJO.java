package com.labEleven.pojo;

import com.MDVSP.core.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemPOJO {
    private static final int MAX_DEPOTS = 100;
    private static final int MAX_TRIPS = 200;
    private List<DepotPOJO> depots;
    private List<TripPOJO> trips;
    private Map<LocationPairPOJO, Duration> distancesMap;

    public ProblemPOJO() {
        depots = new ArrayList<>();
        trips = new ArrayList<>();
        distancesMap = new HashMap<>();
    }

    public void addDepot(DepotPOJO newDepot){
        if (newDepot == null) {
            throw new NullPointerException();
        }
        //TODO check if trips contains this name
        if(depots.contains(newDepot)){
            throw new IllegalArgumentException("Location with this name already exist!");
        }

        // compute costs between this depot and all inserted trips
        for(TripPOJO trip : trips){
            // depot -> trip
            long minutesFromDepotToTrip = newDepot.getPosition().getMinutesTo(trip.getStartPosition());
            distancesMap.put(new LocationPairPOJO(newDepot, trip), Duration.ofMinutes(minutesFromDepotToTrip));

            // trip -> depot
            long minutesFromTripToDepot = trip.getEndPosition().getMinutesTo(newDepot.getPosition());
            distancesMap.put(new LocationPairPOJO(trip, newDepot),Duration.ofMinutes(minutesFromTripToDepot));
        }

        depots.add(newDepot);
    }



    public void addTrip(TripPOJO newTrip){
        if (newTrip == null) {
            throw new NullPointerException();
        }
        //TODO check if depots contains this name
        if(trips.contains(newTrip) || depots.contains(new DepotPOJO(newTrip.getName(), 0,  0, 0))){
            throw new IllegalArgumentException("Location with this name already exist!");
        }

        //1. compute costs between this trip and all inserted depots
        for(DepotPOJO depot : depots){
            // depot -> trip
            long minutesFromDepotToTrip = depot.getPosition().getMinutesTo(newTrip.getStartPosition());
            distancesMap.put(new LocationPairPOJO(depot, newTrip), Duration.ofMinutes(minutesFromDepotToTrip));

            // trip -> depot
            long minutesFromTripToDepot = newTrip.getEndPosition().getMinutesTo(depot.getPosition());
            distancesMap.put(new LocationPairPOJO(newTrip, depot),Duration.ofMinutes(minutesFromTripToDepot));
        }


        //2. computes costs between this trip and all inserted trips
        for(TripPOJO insertedTrip : trips){
            // depot -> trip
            long minutesFromInsertedToNew = insertedTrip.getEndPosition().getMinutesTo(newTrip.getStartPosition());
            distancesMap.put(new LocationPairPOJO(insertedTrip, newTrip), Duration.ofMinutes(minutesFromInsertedToNew));

            // trip -> depot
            long minutesFromNewToInserted = newTrip.getEndPosition().getMinutesTo(insertedTrip.getStartPosition());
            distancesMap.put(new LocationPairPOJO(newTrip, insertedTrip),Duration.ofMinutes(minutesFromNewToInserted));
        }
        //3. Add trip
        trips.add(newTrip);
    }

    public List<DepotPOJO> getDepots() {
        return depots;
    }

    public List<TripPOJO> getTrips() {
        return trips;
    }

    public Solution resolve(){
        try{
            Problem problem  = mappingToOriginalProblem();
            return problem.getSolution();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    private Problem mappingToOriginalProblem(){
        Problem problem = new Problem();

        //1. add depots
        for(DepotPOJO depot : depots){
            problem.addLocation(new Depot(depot.getName(), depot.getNumberOfVehicles()));
        }

        //2. add trips
        for(TripPOJO tripPOJO : trips){
            //2.1 create the actual new trip
            Trip actualTrip = new Trip(tripPOJO.getName(), tripPOJO.getStartingTime(), tripPOJO.getEndingTime());
            problem.addLocation(actualTrip);
            //2.2 set cost between this new trip and others location already inserted in problem
            for(Location location : problem.getLocations()){
                if(location instanceof Depot){
                    //2.2.1 Obtain pojo correspondent of location
                    LocationPOJO depotPOJO = new DepotPOJO(location.getName(), 0, 0,0);

                    //2.2.2 obtain costs
                    Duration costFromDepotToTrip = distancesMap.get(new LocationPairPOJO(depotPOJO, tripPOJO));
                    Duration costFromTripToDepot = distancesMap.get(new LocationPairPOJO(tripPOJO, depotPOJO));

                    //2.2.3 set costs
                    problem.setCost(location, actualTrip, costFromDepotToTrip);
                    problem.setCost(actualTrip, location, costFromTripToDepot);
                }else{
                    //2.2.1 Obtain pojo correspondent of inserted trip
                    LocationPOJO insertedPOJO = new TripPOJO(location.getName(), LocalTime.parse("00:00"), 0, 0, 0, 0);

                    // if is same trip -> continue
                    if(insertedPOJO.getName().equals(tripPOJO.getName())){
                        continue;
                    }


                    //2.2.2 obtain costs
                    Duration costFromInsertedToThis = distancesMap.get(new LocationPairPOJO(insertedPOJO, tripPOJO));
                    Duration costFromThisToInserted = distancesMap.get(new LocationPairPOJO(tripPOJO, insertedPOJO));

                    //2.2.3 set costs
                    problem.setCost(location, actualTrip, costFromInsertedToThis);
                    problem.setCost(actualTrip, location, costFromThisToInserted);
                }
            }


        }
        problem.print();
        return problem;
    }




}
