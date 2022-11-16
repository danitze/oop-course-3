package com.example.controller;

import com.example.Main;
import com.example.entity.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserTest {

    private static Flowers flowers;
    private static final Logger LOGGER = Logger.getLogger(ParserTest.class.getName());

    private static final String DELETE_FILE_EXCEPTION = "Could not delete file";

    @BeforeClass
    public static void initialiseFlowers() {
        Flower flower1 = new FlowerBuilder()
                .name("Flower1")
                .soil(Soil.GROUND)
                .origin("Country1")
                .visualParameters(new VisualParameters(
                        "color11", "color12", new AveLenFlower(AveLenFlower.Measure.CM, 1)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 1),
                        Lighting.YES,
                        new Watering(Watering.Measure.ML_PER_WEEK, 1)
                ))
                .multiplying(Multiplying.SEEDS)
                .build();
        Flower flower2 = new FlowerBuilder()
                .name("Flower2")
                .soil(Soil.PODZOLIC)
                .origin("Country2")
                .visualParameters(new VisualParameters(
                        "color21", "color22", new AveLenFlower(AveLenFlower.Measure.CM, 2)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 2),
                        Lighting.NO,
                        new Watering(Watering.Measure.ML_PER_WEEK, 2)
                ))
                .multiplying(Multiplying.CUTTINGS)
                .build();
        Flower flower3 = new FlowerBuilder()
                .name("Flower3")
                .soil(Soil.SOD_PODZOLIC)
                .origin("Country3")
                .visualParameters(new VisualParameters(
                        "color31", "color32", new AveLenFlower(AveLenFlower.Measure.CM, 3)
                ))
                .growingTips(new GrowingTips(
                        new Temperature(Temperature.Measure.CELCIUS, 3),
                        Lighting.YES,
                        new Watering(Watering.Measure.ML_PER_WEEK, 3)
                ))
                .multiplying(Multiplying.LEAVES)
                .build();
        flowers = new Flowers();
        flowers.add(flower1);
        flowers.add(flower2);
        flowers.add(flower3);
    }

    @Test
    public void getFlowers_DomParser_ShouldGetCorrectList() {
        String xmlPath = "temp.xml";
        Main.writeData(flowers, xmlPath);
        Parser domParser = new XMLDomParser(xmlPath);
        Flowers resultFlowers = domParser.getFlowers();
        try {
            Files.delete(Paths.get(xmlPath));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, DELETE_FILE_EXCEPTION, e);
        }
        Assert.assertEquals(flowers, resultFlowers);
    }

    @Test
    public void getFlowers_SaxParser_ShouldGetCorrectList() {
        String xmlPath = "temp.xml";
        Main.writeData(flowers, xmlPath);
        Flowers resultFlowers = null;
        try {
            Parser saxParser = new XMLSaxParser(xmlPath);
            resultFlowers = saxParser.getFlowers();
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.log(Level.WARNING, "Could not create SAX parser", e);
        }
        try {
            Files.delete(Paths.get(xmlPath));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, DELETE_FILE_EXCEPTION, e);
        }
        Assert.assertEquals(flowers, resultFlowers);
    }

    @Test
    public void getFlowers_StaxParser_ShouldGetCorrectList() {
        String xmlPath = "temp.xml";
        Main.writeData(flowers, xmlPath);
        Parser staxParser = new XMLStaxParser(xmlPath);
        Flowers resultFlowers = staxParser.getFlowers();
        try {
            Files.delete(Paths.get(xmlPath));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, DELETE_FILE_EXCEPTION, e);
        }
        Assert.assertEquals(flowers, resultFlowers);
    }

}
