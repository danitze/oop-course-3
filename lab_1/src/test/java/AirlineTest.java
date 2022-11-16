import data.Airplane;
import data.CargoAirplane;
import data.PassengerAirplane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AirlineTest {

    private static final String AIRLINE_NAME = "ABC";

    @Test
    public void testSetAirplanes() {
        Airline airline = new Airline(AIRLINE_NAME);
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("a", 1, 1,1),
                new CargoAirplane("b", 2, 2, 2)
        );
        airline.setAirplanes(airplanes);
        Assertions.assertEquals(new Airline(AIRLINE_NAME, airplanes), airline);
    }

    @Test
    public void testGetAirplanes() {
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("a", 1, 1,1),
                new CargoAirplane("b", 2, 2, 2)
        );
        Assertions.assertEquals(new Airline(AIRLINE_NAME, airplanes).getAirplanes(), airplanes);
    }

    @Test
    public void testGetName() {
        Airline airline = new Airline(AIRLINE_NAME);
        Assertions.assertEquals(airline.getName(), AIRLINE_NAME);
    }

    @Test
    public void testAddPlane() {
        Airline airline = new Airline(AIRLINE_NAME);
        Airplane airplane = new PassengerAirplane("a", 1, 1,1);
        airline.addAirplane(airplane);
        Assertions.assertEquals(new Airline(AIRLINE_NAME, List.of(airplane)), airline);
    }

    @Test
    public void testFilterByFuelConsumption() {
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("a", 1, 1500,1),
                new PassengerAirplane("b", 1, 700,1),
                new PassengerAirplane("c", 1, 1600,1),
                new PassengerAirplane("d", 1, 2100,1),
                new PassengerAirplane("e", 1, 1000,1),
                new PassengerAirplane("f", 1, 2000,1)
        );
        List<Airplane> filteredAirplanes = List.of(
                new PassengerAirplane("a", 1, 1500,1),
                new PassengerAirplane("c", 1, 1600,1),
                new PassengerAirplane("e", 1, 1000,1),
                new PassengerAirplane("f", 1, 2000,1)
        );
        Airline airline = new Airline(AIRLINE_NAME, airplanes);
        Assertions.assertEquals(airline.filterByFuelConsumption(1000d, 2000d), filteredAirplanes);
    }

    @Test
    public void testGetLoadCapacity() {
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("a", 1, 1,1),
                new CargoAirplane("b", 2, 2, 20),
                new CargoAirplane("c", 2, 2, 30),
                new CargoAirplane("d", 2, 2, 25),
                new CargoAirplane("e", 2, 2, 3),
                new CargoAirplane("f", 2, 2, 1)
        );
        Airline airline = new Airline(AIRLINE_NAME, airplanes);
        assert airline.getLoadCapacity() == 79;
    }

    @Test
    public void testGetPassengersCapacity() {
        List<Airplane> airplanes = List.of(
                new CargoAirplane("a", 1, 1,1),
                new PassengerAirplane("b", 2, 2, 20),
                new PassengerAirplane("c", 2, 2, 30),
                new PassengerAirplane("d", 2, 2, 25),
                new PassengerAirplane("e", 2, 2, 3),
                new PassengerAirplane("f", 2, 2, 1)
        );
        Airline airline = new Airline(AIRLINE_NAME, airplanes);
        assert airline.getPassengersCapacity() == 79;
    }

    @Test
    public void testSortByFlightDistance() {
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("a", 1, 1500,1),
                new PassengerAirplane("b", 3, 700,1),
                new PassengerAirplane("c", 2, 1600,1),
                new PassengerAirplane("d", 12, 2100,1),
                new PassengerAirplane("e", 4, 1000,1),
                new PassengerAirplane("f", 9, 2000,1)
        );
        List<Airplane> ascSortedAirplanes = List.of(
                new PassengerAirplane("a", 1, 1500,1),
                new PassengerAirplane("c", 2, 1600,1),
                new PassengerAirplane("b", 3, 700,1),
                new PassengerAirplane("e", 4, 1000,1),
                new PassengerAirplane("f", 9, 2000,1),
                new PassengerAirplane("d", 12, 2100,1)
        );
        List<Airplane> descSortedAirplanes = List.of(
                new PassengerAirplane("d", 12, 2100,1),
                new PassengerAirplane("f", 9, 2000,1),
                new PassengerAirplane("e", 4, 1000,1),
                new PassengerAirplane("b", 3, 700,1),
                new PassengerAirplane("c", 2, 1600,1),
                new PassengerAirplane("a", 1, 1500,1)
        );
        Airline airline = new Airline(AIRLINE_NAME, airplanes);
        Assertions.assertEquals(airline.sortByFlightDistance(true), ascSortedAirplanes);
        Assertions.assertEquals(airline.sortByFlightDistance(false), descSortedAirplanes);
    }
}
