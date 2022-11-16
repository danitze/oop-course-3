package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerAirplaneTest {

    private PassengerAirplane passengerAirplane = null;

    @BeforeEach
    public void init() {
        passengerAirplane = new PassengerAirplane("A", 1, 1, 1);
    }

    @Test
    public void testConstructor() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PassengerAirplane("A", 1, 1, -1)
        );
        Assertions.assertEquals(exception.getMessage(), "PassengersCapacity should not be negative");
    }

    @Test
    public void testGetPassengerCapacity() {
        assert passengerAirplane.getPassengersCapacity() == 1;
    }

    @Test
    public void testSetPassengerCapacity() {
        passengerAirplane.setPassengersCapacity(2);
        Assertions.assertEquals(
                passengerAirplane,
                new PassengerAirplane("A", 1, 1, 2)
        );
    }
}
