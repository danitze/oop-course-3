package com.example;

import com.example.controller.Parser;
import com.example.controller.XMLDomParser;
import com.example.controller.XMLSaxParser;
import com.example.controller.XMLStaxParser;
import com.example.entity.Flower;
import com.example.entity.Flowers;
import com.example.entity.GrowingTips;
import com.example.entity.VisualParameters;
import com.example.util.Provider;
import com.example.util.Sorter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.constants.Attributes.LIGHT_REQUIRING;
import static com.example.constants.Attributes.MEASURE;
import static com.example.constants.Tags.*;
import static com.example.constants.Tags.MULTIPLYING;

public final class Main {

    private static final String[] OUT_NAMES = {"output.dom.xml", "output.sax.xml", "output.stax.xml"};

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(final String[] args) {
        parseAndWriteSorted(args[0]);
    }

    private static Parser getParser(int id, String fileName) {
        switch (id) {
            case 0:
                return new XMLDomParser(fileName);
            case 1:
                try {
                    return new XMLSaxParser(fileName);
                } catch (ParserConfigurationException | SAXException e) {
                    LOGGER.log(Level.WARNING, "Couldn't create SaxParser", e);
                }
                break;
            case 2:
                return new XMLStaxParser(fileName);
            default:
                throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }

    private static Method getSortingMethod(int id) {
        try {
            switch (id) {
                case 0:
                    return Sorter.class.getDeclaredMethod("sortByName", Flowers.class);
                case 1:
                    return Sorter.class.getDeclaredMethod("sortByTemperature", Flowers.class);
                case 2:
                    return Sorter.class.getDeclaredMethod("sortByOrigin", Flowers.class);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.WARNING, "Couldn't find method for id " + id, e);
        }
        throw new IllegalArgumentException();
    }

    public static void parseAndWriteSorted(String fileName) {
        for(int i = 0; i < OUT_NAMES.length; ++i) {
            if(!correctXML(fileName, "input.xsd")) {
                break;
            }
            Parser parser = getParser(i, fileName);
            Method sorter = getSortingMethod(i);
            Flowers flowers = parser.getFlowers();
            try {
                sorter.invoke(null, flowers);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.log(Level.WARNING, "Couldn't start method " + sorter.getName(), e);
            }
            writeData(flowers, OUT_NAMES[i]);
        }
    }

    public static boolean correctXML(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory = Provider.provideSchemaFactory();
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException | IOException e) {
            LOGGER.log(Level.INFO, "Invalid XML");
            return false;
        }
        return true;
    }

    public static void writeData(Flowers flowers, String newFileName) {
        try {
            DocumentBuilderFactory dbf = Provider.provideDocumentBuilderFactory();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(FLOWERS);
            List<Flower> flowerList = flowers.getFlowerList();
            for (Flower flower : flowerList) {
                root.appendChild(createFlower(document, flower));
            }
            document.appendChild(root);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(newFileName));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
            LOGGER.log(Level.WARNING, "Couldn't write data to file", e);
        }
    }

    private static Node createFlower(Document doc, Flower flower) {
        Element flowerEl = doc.createElement(FLOWER);

        Element name = doc.createElement(NAME);
        name.appendChild(doc.createTextNode(flower.getName()));
        flowerEl.appendChild(name);

        Element soil = doc.createElement(SOIL);
        soil.appendChild(doc.createTextNode(flower.getSoil().getEnumValue()));
        flowerEl.appendChild(soil);

        Element origin = doc.createElement(ORIGIN);
        origin.appendChild(doc.createTextNode(flower.getOrigin()));
        flowerEl.appendChild(origin);

        VisualParameters entityVisualParameters = flower.getVisualParameters();
        Element visualParameters = doc.createElement(VISUAL_PARAMETERS);
        Element stemColor = doc.createElement(STEM_COLOR);
        stemColor.appendChild(doc.createTextNode(entityVisualParameters.getStemColor()));
        Element leafColor = doc.createElement(LEAF_COLOR);
        leafColor.appendChild(doc.createTextNode(entityVisualParameters.getLeafColor()));
        visualParameters.appendChild(stemColor);
        visualParameters.appendChild(leafColor);
        if (entityVisualParameters.getAveLenFlower() != null) {
            Element aveLenFlower = doc.createElement(AVE_LEN_FLOWER);
            aveLenFlower.setAttribute(MEASURE,
                    entityVisualParameters.getAveLenFlower().getMeasure().getEnumValue());
            aveLenFlower.appendChild(doc.createTextNode(
                    String.valueOf(entityVisualParameters.getAveLenFlower().getValue())));
            visualParameters.appendChild(aveLenFlower);
        }
        flowerEl.appendChild(visualParameters);

        GrowingTips entityGrowingTips = flower.getGrowingTips();
        Element growingTips = doc.createElement(GROWING_TIPS);
        Element temperature = doc.createElement(TEMPERATURE);
        temperature.setAttribute(MEASURE, entityGrowingTips.getTemperature().getMeasure().getEnumValue());
        temperature.appendChild(doc.createTextNode(String.valueOf(entityGrowingTips.getTemperature().getValue())));
        Element lighting = doc.createElement(LIGHTING);
        lighting.setAttribute(LIGHT_REQUIRING, entityGrowingTips.getLighting().enumValue);
        growingTips.appendChild(temperature);
        growingTips.appendChild(lighting);
        if (entityGrowingTips.getWatering() != null) {
            Element watering = doc.createElement(WATERING);
            watering.setAttribute(MEASURE, entityGrowingTips.getWatering().getMeasure().getEnumValue());
            watering.appendChild(doc.createTextNode(String.valueOf(entityGrowingTips.getWatering().getValue())));
            growingTips.appendChild(watering);
        }
        flowerEl.appendChild(growingTips);

        Element multiplying = doc.createElement(MULTIPLYING);
        multiplying.appendChild(doc.createTextNode(flower.getMultiplying().getEnumValue()));
        flowerEl.appendChild(multiplying);

        return flowerEl;
    }

}
