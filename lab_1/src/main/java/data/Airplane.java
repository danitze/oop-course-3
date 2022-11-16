package data;

import java.io.Serializable;
import java.util.Objects;

public abstract class Airplane implements Serializable {
    private String model;
    private int flightDistance;
    private int fuelConsumption;

    public Airplane(String model, int flightDistance, int fuelConsumption) {
        if(model.isEmpty()) {
            throw new IllegalArgumentException("Model should not be empty");
        }
        if(flightDistance < 0) {
            throw new IllegalArgumentException("Flight distance should not be negative");
        }
        if(fuelConsumption < 0) {
            throw new IllegalArgumentException("Fuel consumption distance should not be negative");
        }
        this.model = model;
        this.flightDistance = flightDistance;
        this.fuelConsumption = fuelConsumption;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFlightDistance() {
        return flightDistance;
    }

    public void setFlightDistance(int flightDistance) {
        this.flightDistance = flightDistance;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "model='" + model + '\'' +
                ", flightDistance=" + flightDistance +
                ", fuelConsumption=" + fuelConsumption +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airplane airplane)) return false;
        return flightDistance == airplane.flightDistance && fuelConsumption == airplane.fuelConsumption && model.equals(airplane.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, flightDistance, fuelConsumption);
    }
}
