package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AirplaneTest {

    private Airplane airplane = null;

    @BeforeEach
    public void initAirplane() {
        airplane = getAirplane("A", 200, 300);
    }

    @Test
    public void testConstructor() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                getAirplane("", 1, 1)
        );
        Assertions.assertEquals(exception.getMessage(), "Model should not be empty");

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                getAirplane("A", -1, 1)
        );
        Assertions.assertEquals(exception.getMessage(), "Flight distance should not be negative");

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                getAirplane("A", 1, -1)
        );
        Assertions.assertEquals(exception.getMessage(), "Fuel consumption distance should not be negative");
    }

    @Test
    public void testGetModel() {
        Assertions.assertEquals(airplane.getModel(), "A");
    }

    @Test
    public void testSetModel() {
        airplane.setModel("B");
        Assertions.assertEquals(airplane, getAirplane("B", 200, 300));
    }

    @Test
    public void testGetFlightDistance() {
        assert airplane.getFlightDistance() == 200;
    }

    @Test
    public void testSetFlightDistance() {
        airplane.setFlightDistance(100);
        Assertions.assertEquals(airplane, getAirplane("A", 100, 300));
    }

    @Test
    public void testGetFuelConsumption() {
        assert airplane.getFuelConsumption() == 300;
    }

    @Test
    public void testSetFuelConsumption() {
        airplane.setFuelConsumption(500);
        Assertions.assertEquals(airplane, getAirplane("A", 200, 500));
    }

    private Airplane getAirplane(String model, int flightDistance, int fuelConsumption) {
        return new Airplane(model, flightDistance, fuelConsumption) {
            @Override
            public String getModel() {
                return super.getModel();
            }

            @Override
            public void setModel(String model) {
                super.setModel(model);
            }

            @Override
            public int getFlightDistance() {
                return super.getFlightDistance();
            }

            @Override
            public void setFlightDistance(int flightDistance) {
                super.setFlightDistance(flightDistance);
            }

            @Override
            public int getFuelConsumption() {
                return super.getFuelConsumption();
            }

            @Override
            public void setFuelConsumption(int fuelConsumption) {
                super.setFuelConsumption(fuelConsumption);
            }

            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }
}
