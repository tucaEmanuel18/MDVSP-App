import java.io.Serializable;

public class Depot implements Serializable {
    private String name;
    private int numberOfVehicles;
    private Position position;

    public Depot(String name, int numberOfVehicles, Position position) {
        this.name = name;
        this.numberOfVehicles = numberOfVehicles;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public Position getPosition() {
        return position;
    }
}
