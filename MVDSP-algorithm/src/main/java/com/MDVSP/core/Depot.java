package com.MDVSP.core;

public class Depot extends Location {
    private Integer numberOfVehicles;
    public Depot(String name, Integer numberOfVehicles) {
        super(name);
        this.numberOfVehicles = numberOfVehicles;
    }

    public Integer getNumberOfVehicles() {
        return numberOfVehicles;
    }
}
