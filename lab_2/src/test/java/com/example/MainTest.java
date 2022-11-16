package com.example;

import com.example.controller.XMLDomParser;
import com.example.controller.XMLSaxParser;
import com.example.controller.XMLStaxParser;
import com.example.entity.*;
import com.example.util.Sorter;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainTest {

    private static final String MOCK_FILENAME = "mock.xml";

    private static final Logger LOGGER = Logger.getLogger(MainTest.class.getName());
    private static final String FIND_METHOD_EXCEPTION = "Could not find method";
    private static final String INVOCATION_EXCEPTION = "Could not invoke method";
    private static final String SAX_EXCEPTION = "Could not create SAX parser";
    private static final String FILE_EXCEPTION = "Could not delete file";

    @Test
    public void getParser_Id0Given_ShouldProvideDomParser() {
        try {
            Method method = Main.class.getDeclaredMethod("getParser", int.class, String.class);
            method.setAccessible(true);
            Assert.assertEquals(new XMLDomParser(MOCK_FILENAME), method.invoke(null, 0, MOCK_FILENAME));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test
    public void getParser_Id1Given_ShouldProvideSaxParser() {
        try {
            Method method = Main.class.getDeclaredMethod("getParser", int.class, String.class);
            method.setAccessible(true);
            Assert.assertEquals(new XMLSaxParser(MOCK_FILENAME), method.invoke(null, 1, MOCK_FILENAME));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.log(Level.WARNING, SAX_EXCEPTION, e);
        }
    }

    @Test
    public void getParser_Id2Given_ShouldProvideStaxParser() {
        try {
            Method method = Main.class.getDeclaredMethod("getParser", int.class, String.class);
            method.setAccessible(true);
            Assert.assertEquals(new XMLStaxParser(MOCK_FILENAME), method.invoke(null, 2, MOCK_FILENAME));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getParser_Id3Given_ShouldThrowIllegalArgumentException() {
        try {
            Method method = Main.class.getDeclaredMethod("getParser", int.class, String.class);
            method.setAccessible(true);
            method.invoke(null, 3, MOCK_FILENAME);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test
    public void getSortingMethod_Id0Given_ShouldGetSortByNameMethod() {
        try {
            Method method = Main.class.getDeclaredMethod("getSortingMethod", int.class);
            method.setAccessible(true);
            Assert.assertEquals(Sorter.class.getDeclaredMethod("sortByName", Flowers.class),
                    method.invoke(null, 0));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test
    public void getSortingMethod_Id1Given_ShouldGetSortByTemperatureMethod() {
        try {
            Method method = Main.class.getDeclaredMethod("getSortingMethod", int.class);
            method.setAccessible(true);
            Assert.assertEquals(Sorter.class.getDeclaredMethod("sortByTemperature", Flowers.class),
                    method.invoke(null, 1));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test
    public void getSortingMethod_Id2Given_ShouldGetSortByOriginMethod() {
        try {
            Method method = Main.class.getDeclaredMethod("getSortingMethod", int.class);
            method.setAccessible(true);
            Assert.assertEquals(Sorter.class.getDeclaredMethod("sortByOrigin", Flowers.class),
                    method.invoke(null, 2));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, INVOCATION_EXCEPTION, e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSortingMethod_Id3Given_ShouldThrowIllegalArgumentException() throws Throwable {
        try {
            Method method = Main.class.getDeclaredMethod("getSortingMethod", int.class);
            method.setAccessible(true);
            Assert.assertEquals(Sorter.class.getDeclaredMethod("sortByOrigin", Flowers.class),
                    method.invoke(null, 3));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, FIND_METHOD_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @Test
    public void correctXml_CorrectXMLGiven_ShouldReturnTrue() {
        String xmlPath = "input.xml";
        String xsdPath = "input.xsd";
        Assert.assertTrue(Main.correctXML(xmlPath, xsdPath));
    }

    @Test
    public void correctXml_IncorrectXMLGiven_ShouldReturnFalse() {
        String xmlPath = "invalidXML.xml";
        String xsdPath = "input.xsd";
        Assert.assertFalse(Main.correctXML(xmlPath, xsdPath));
    }

    @Test
    public void writeData_EmptyFlowersGiven_FileContentShouldEqualString() {
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><flowers/>";
        String fileName = "testOut.xml";
        Main.writeData(new Flowers(), "testOut.xml");
        String content = readFileContent(fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, FILE_EXCEPTION, e);
        }
        Assert.assertEquals(expectedResult, content);
    }

    @Test
    public void writeData_FlowersWithOneFlowerGiven_FileContentShouldEqualString() {
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<flowers>" +
                "<flower>" +
                "<name>Flower</name>" +
                "<soil>грунтовая</soil>" +
                "<origin>Country</origin>" +
                "<visualParameters>" +
                "<stemColor>color</stemColor>" +
                "<leafColor>color</leafColor>" +
                "<aveLenFlower measure=\"cm\">1</aveLenFlower>" +
                "</visualParameters>" +
                "<growingTips>" +
                "<temperature measure=\"celcius\">1</temperature>" +
                "<lighting lightRequiring=\"yes\"/>" +
                "<watering measure=\"mlPerWeek\">1</watering>" +
                "</growingTips>" +
                "<multiplying>семена</multiplying>" +
                "</flower>" +
                "</flowers>";
        String fileName = "testOut.xml";
        Flower flower = new FlowerBuilder()
                .name("Flower")
                .soil(Soil.GROUND)
                .origin("Country")
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
        Flowers flowers = new Flowers();
        flowers.add(flower);
        Main.writeData(flowers, "testOut.xml");
        String content = readFileContent(fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, FILE_EXCEPTION, e);
        }
        Assert.assertEquals(expectedResult, content);
    }

    private String readFileContent(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "IOException thrown", e);
        }
        return sb.toString();
    }

}
