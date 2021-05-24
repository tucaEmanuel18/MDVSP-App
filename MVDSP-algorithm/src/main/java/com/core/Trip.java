package com.core;

import java.time.Duration;
import java.time.LocalTime;

public class Trip extends Location{
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Duration tripTimeCost;


    public Trip(String name, LocalTime startingTime, LocalTime endingTime) {
        super(name);
        if(startingTime.compareTo(endingTime) >= 0){
            throw new IllegalArgumentException("The starting time can't be greater or equal with starting time!");
        }

        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.tripTimeCost = Duration.between(startingTime, endingTime);
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public Duration getTripTimeCost() {
        return tripTimeCost;
    }

}
