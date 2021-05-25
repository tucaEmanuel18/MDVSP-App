package com.labEleven.pojo;

import java.io.Serializable;

public class DepotPOJO extends LocationPOJO implements Serializable {
    private int numberOfVehicles;
    private Position position;

    public DepotPOJO(String name, int numberOfVehicles, double latitude, double longitude) {
        super(name);
        this.numberOfVehicles = numberOfVehicles;
        this.position = new Position(latitude, longitude);
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


}