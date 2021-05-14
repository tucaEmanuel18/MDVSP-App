package com.core;

import java.util.Objects;

public class LocationPair {
    private Location source;
    private Location destination;

    public LocationPair(Location source, Location destination) {
        this.source = source;
        this.destination = destination;
    }

    public Location getSource() {
        return source;
    }

    public Location getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationPair that = (LocationPair) o;
        return source.equals(that.source) && destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }

    @Override
    public String toString() {
        return "LocationPair{" +
                "source=" + source +
                ", destination=" + destination +
                '}';
    }
}
