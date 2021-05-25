package com.labEleven.pojo;

import com.MDVSP.core.Location;
import com.MDVSP.core.LocationPair;

import java.util.Objects;

public class LocationPairPOJO {
    private LocationPOJO source;
    private LocationPOJO destination;

    public LocationPairPOJO(LocationPOJO source, LocationPOJO destination) {
        this.source = source;
        this.destination = destination;
    }

    public LocationPOJO getSource() {
        return source;
    }

    public LocationPOJO getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationPairPOJO that = (LocationPairPOJO) o;
        return source.equals(that.source) && destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }

    @Override
    public String toString() {
        return "LocationPairPOJO{" +
                "source=" + source +
                ", destination=" + destination +
                '}';
    }
}
