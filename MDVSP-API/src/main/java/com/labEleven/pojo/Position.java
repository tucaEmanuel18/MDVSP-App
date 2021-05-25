package com.labEleven.pojo;

import java.io.Serializable;

public class Position implements Serializable {
    private static int MEAN_OF_KM_PER_HOUR = 60;
    private double latitude;
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getMinutesTo(Position anotherPosition){
        double distance = this.getDistance(anotherPosition);
        double numberOfHours = distance / MEAN_OF_KM_PER_HOUR;
        return (long)(numberOfHours * 60);
    }

    public double getDistance(Position anotherPosition){
        return distanceInKmBetweenEarthCoordinates(
                this.latitude,
                this.longitude,
                anotherPosition.latitude,
                anotherPosition.longitude);
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        var earthRadiusKm = 6371;

        var dLat = degreesToRadians(lat2-lat1);
        var dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", longitute=" + longitude +
                '}';
    }
}
