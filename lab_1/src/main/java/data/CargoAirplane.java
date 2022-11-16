package data;

import java.io.Serializable;
import java.util.Objects;

public class CargoAirplane extends Airplane implements Serializable {

    private int loadCapacity;

    public CargoAirplane(String model, int flightDistance, int fuelConsumption, int loadCapacity) {
        super(model, flightDistance, fuelConsumption);
        if(loadCapacity < 0) {
            throw new IllegalArgumentException("LoadCapacity should not be negative");
        }
        this.loadCapacity = loadCapacity;
    }

    public int getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(int loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    @Override
    public String toString() {
        return "CargoAirplane{" +
                "loadCapacity=" + loadCapacity +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CargoAirplane that)) return false;
        if (!super.equals(o)) return false;
        return loadCapacity == that.loadCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loadCapacity);
    }
}
