package com.example.util;

import com.example.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SorterTest {

    private static Flower flower1;
    private static Flower flower2;
    private static Flower flower3;
    private static Flowers flowers;

    @BeforeClass
    public static void initialiseFlowerElements() {
        flower1 = new FlowerBuilder()
                .name("B")
                .soil(Soil.GROUND)
                .origin("C")
                .visualParameters(new VisualParameters(
                        "color", "color", new AveLenFlower(AveLenFlower.Measure.CM, 1)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 3),
                        Lighting.YES,
                        new Watering(Watering.Measure.ML_PER_WEEK, 1)
                ))
                .multiplying(Multiplying.SEEDS)
                .build();
        flower2 = new FlowerBuilder()
                .name("C")
                .soil(Soil.GROUND)
                .origin("B")
                .visualParameters(new VisualParameters(
                        "color", "color", new AveLenFlower(AveLenFlower.Measure.CM, 1)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 1),
                        Lighting.YES,
                        new Watering(Watering.Measure.ML_PER_WEEK, 1)
                ))
                .multiplying(Multiplying.SEEDS)
                .build();
        flower3 = new FlowerBuilder()
                .name("A")
                .soil(Soil.GROUND)
                .origin("A")
                .visualParameters(new VisualParameters(
                        "color", "color", new AveLenFlower(AveLenFlower.Measure.CM, 1)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 2),
                        Lighting.YES,
                        new Watering(Watering.Measure.ML_PER_WEEK, 1)
                ))
                .multiplying(Multiplying.SEEDS)
                .build();
    }

    @Before
    public void initialiseFlowers() {
        flowers = new Flowers();
        flowers.add(flower1);
        flowers.add(flower2);
        flowers.add(flower3);
    }

    @Test
    public void sortByName_FlowersProvided_ShouldReturnFlowersSortedByName() {
        Flowers sortedByNameFlowers = new Flowers();
        sortedByNameFlowers.add(flower3);
        sortedByNameFlowers.add(flower1);
        sortedByNameFlowers.add(flower2);
        Assert.assertEquals(sortedByNameFlowers, Sorter.sortByName(flowers));
    }

    @Test
    public void sortByTemperature_FlowersProvided_ShouldReturnFlowersSortedByName() {
        Flowers sortedByNameFlowers = new Flowers();
        sortedByNameFlowers.add(flower2);
        sortedByNameFlowers.add(flower3);
        sortedByNameFlowers.add(flower1);
        Assert.assertEquals(sortedByNameFlowers, Sorter.sortByTemperature(flowers));
    }

    @Test
    public void sortByOrigin_FlowersProvided_ShouldReturnFlowersSortedByName() {
        Flowers sortedByNameFlowers = new Flowers();
        sortedByNameFlowers.add(flower3);
        sortedByNameFlowers.add(flower2);
        sortedByNameFlowers.add(flower1);
        Assert.assertEquals(sortedByNameFlowers, Sorter.sortByOrigin(flowers));
    }

}
