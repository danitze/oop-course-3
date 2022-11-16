package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CargoAirplaneTest {
    private CargoAirplane cargoAirplane = null;

    @BeforeEach
    public void init() {
        cargoAirplane = new CargoAirplane("A", 1, 1, 1);
    }

    @Test
    public void testConstructor() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CargoAirplane("A", 1, 1, -1)
        );
        Assertions.assertEquals(exception.getMessage(), "LoadCapacity should not be negative");
    }

    @Test
    public void testGetLoadCapacity() {
        assert cargoAirplane.getLoadCapacity() == 1;
    }

    @Test
    public void testSetLoadCapacity() {
        cargoAirplane.setLoadCapacity(2);
        Assertions.assertEquals(
                cargoAirplane,
                new CargoAirplane("A", 1, 1, 2)
        );
    }
}
