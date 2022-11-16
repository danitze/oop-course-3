import data.Airplane;
import data.CargoAirplane;
import data.PassengerAirplane;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Airline {
    private String name;
    private List<Airplane> airplanes;

    public Airline(String name, List<Airplane> airplanes) {
        this.name = name;
        this.airplanes = airplanes;
    }

    public Airline(String name) {
        this(name, new LinkedList<>());
    }

    public void addAirplane(Airplane airplane) {
        airplanes.add(airplane);
    }

    public List<Airplane> filterByFuelConsumption(double minConsumption, double maxConsumption) {
        return airplanes
                .stream()
                .filter(airplane -> airplane.getFuelConsumption() >= minConsumption && airplane.getFuelConsumption() <= maxConsumption)
                .collect(Collectors.toList());
    }

    public int getLoadCapacity() {
        return airplanes
                .stream()
                .filter(CargoAirplane.class::isInstance)
                .map(CargoAirplane.class::cast)
                .map(CargoAirplane::getLoadCapacity)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int getPassengersCapacity() {
        return airplanes
                .stream()
                .filter(PassengerAirplane.class::isInstance)
                .map(PassengerAirplane.class::cast)
                .map(PassengerAirplane::getPassengersCapacity)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<Airplane> sortByFlightDistance(boolean isAscending) {
        return airplanes
                .stream()
                .sorted((airplane1, airplane2) -> {
                    if (isAscending) {
                        return airplane1.getFlightDistance() - airplane2.getFlightDistance();
                    } else {
                        return airplane2.getFlightDistance() - airplane1.getFlightDistance();
                    }
                }).collect(Collectors.toList());
    }

    public void getAirplanesFromFile(String fileName) {
        airplanes.clear();
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Airplane airplane;
            while ((airplane = (Airplane) inputStream.readObject()) != null) {
                airplanes.add(airplane);
            }
        }
        catch (EOFException ignored) {}
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public List<Airplane> getAirplanes() {
        return airplanes;
    }

    public void setAirplanes(List<Airplane> airplanes) {
        this.airplanes = airplanes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airline airline)) return false;
        return name.equals(airline.name) && airplanes.equals(airline.airplanes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, airplanes);
    }
}
