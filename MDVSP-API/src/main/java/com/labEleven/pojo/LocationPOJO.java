package com.labEleven.pojo;

import com.MDVSP.core.Location;

import java.util.Objects;

public abstract class LocationPOJO {
    private String name;

    public LocationPOJO(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationPOJO location = (LocationPOJO) o;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                '}';
    }
}
