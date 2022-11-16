package data;

import java.io.Serializable;
import java.util.Objects;

public class PassengerAirplane extends Airplane implements Serializable {

    private int passengersCapacity;

    public PassengerAirplane(String model, int flightDistance, int fuelConsumption, int passengersCapacity) {
        super(model, flightDistance, fuelConsumption);
        if(passengersCapacity < 0) {
            throw new IllegalArgumentException("PassengersCapacity should not be negative");
        }
        this.passengersCapacity = passengersCapacity;
    }

    public int getPassengersCapacity() {
        return passengersCapacity;
    }

    public void setPassengersCapacity(int passengersCapacity) {
        this.passengersCapacity = passengersCapacity;
    }

    @Override
    public String toString() {
        return "PassengerAirplane{" +
                "passengersCapacity=" + passengersCapacity +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PassengerAirplane that)) return false;
        if (!super.equals(o)) return false;
        return passengersCapacity == that.passengersCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passengersCapacity);
    }
}
