package com.labEleven.pojo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public class TripPOJO extends LocationPOJO implements Serializable {
    private static int MEAN_OF_KM_PER_HOUR = 60;
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Position startPosition;
    private Position endPosition;

     public TripPOJO(String name, LocalTime startingTime, double startLatitude, double startLongitude, double finishLatitude, double finishLongitude){
        super(name);
        this.startingTime = startingTime;
        this.startPosition = new Position(startLatitude, startLongitude);
        this.endPosition = new Position(finishLatitude, finishLongitude);
        this.endingTime = computeEndingTime();
    }

    /*public TripPOJO(String name, LocalTime startingTime, Position startPosition, Position endPosition) {
        super(name);
        this.startingTime = startingTime;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.endingTime = computeEndingTime();
    }*/
    private LocalTime computeEndingTime(){
        long numberOfMinutes = startPosition.getMinutesTo(endPosition);
        return startingTime.plus(Duration.ofMinutes(numberOfMinutes));
    }


    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }
}
