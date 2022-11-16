package com.example.controller;

import com.example.util.Provider;
import com.example.constants.Attributes;
import com.example.constants.Tags;
import com.example.entity.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLDomParser implements Parser{

    private final String fileName;

    public XMLDomParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Flowers getFlowers() {
        Flowers flowers = new Flowers();
        try {
            DocumentBuilderFactory dbf = Provider.provideDocumentBuilderFactory();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(fileName));
            NodeList flowerNodes = document.getDocumentElement().getElementsByTagName(Tags.FLOWER);
            for (int i = 0; i < flowerNodes.getLength(); ++i) {
                Node flowerNode = flowerNodes.item(i);
                Flower flower = new FlowerBuilder()
                        .name(getFlowerName(getNodeByTag(Tags.NAME, flowerNode)))
                        .soil(getFlowerSoil(getNodeByTag(Tags.SOIL, flowerNode)))
                        .origin(getFlowerOrigin(getNodeByTag(Tags.ORIGIN, flowerNode)))
                        .visualParameters(getVisualParameters(getNodeByTag(Tags.VISUAL_PARAMETERS, flowerNode)))
                        .growingTips(getGrowingTips(getNodeByTag(Tags.GROWING_TIPS, flowerNode)))
                        .multiplying(getMultiplying(getNodeByTag(Tags.MULTIPLYING, flowerNode)))
                        .build();
                flowers.add(flower);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(XMLDomParser.class.getName())
                    .log(Level.WARNING, "Couldn't parse file " + fileName, e);
        }
        return flowers;
    }

    private Node getNodeByTag(String tag, Node rootNode) {
        return ((Element) rootNode).getElementsByTagName(tag).item(0);
    }

    private boolean checkChildrenWithTag(String tag, Node rootNode) {
        return ((Element) rootNode).getElementsByTagName(tag).getLength() > 0;
    }

    private String getFlowerName(Node flowerNameNode) {
        return flowerNameNode.getTextContent();
    }

    private Soil getFlowerSoil(Node flowerSoilNode) {
        return Soil.getElByValue(flowerSoilNode.getTextContent());
    }

    private String getFlowerOrigin(Node flowerOriginNode) {
        return flowerOriginNode.getTextContent();
    }

    private VisualParameters getVisualParameters(Node visualParametersNode) {
        String stemColor = getNodeByTag(Tags.STEM_COLOR, visualParametersNode).getTextContent();
        String leafColor = getNodeByTag(Tags.LEAF_COLOR, visualParametersNode).getTextContent();
        AveLenFlower aveLenFlower = null;
        if (checkChildrenWithTag(Tags.AVE_LEN_FLOWER, visualParametersNode)) {
            aveLenFlower = getAveLenFlower(getNodeByTag(Tags.AVE_LEN_FLOWER, visualParametersNode));
        }
        return new VisualParameters(stemColor, leafColor, aveLenFlower);
    }

    private AveLenFlower getAveLenFlower(Node aveLenFlowerNode) {
        String measureName = ((Element) aveLenFlowerNode)
                .getAttribute(Attributes.MEASURE);
        AveLenFlower.Measure measure = AveLenFlower.Measure.getElByValue(measureName);
        int value = Integer.parseInt(aveLenFlowerNode.getTextContent());
        return new AveLenFlower(measure, value);
    }

    private GrowingTips getGrowingTips(Node growingTipsNode) {
        Temperature temperature = getTemperature(getNodeByTag(Tags.TEMPERATURE, growingTipsNode));
        Lighting lighting =
                getLighting(getNodeByTag(Tags.LIGHTING, growingTipsNode));
        Watering watering = null;
        if (checkChildrenWithTag(Tags.WATERING, growingTipsNode)) {
            watering = getWatering(getNodeByTag(Tags.WATERING, growingTipsNode));
        }
        return new GrowingTips(temperature, lighting, watering);
    }

    private Temperature getTemperature(Node temperatureNode) {
        String measureName = ((Element) temperatureNode)
                .getAttribute(Attributes.MEASURE);
        Temperature.Measure measure = Temperature.Measure.getElByValue(measureName);
        int value = Integer.parseInt(temperatureNode.getTextContent());
        return new Temperature(measure, value);
    }

    private Lighting getLighting(Node lightRequiringNode) {
        String lightRequiringVal = ((Element) lightRequiringNode)
                .getAttribute(Attributes.LIGHT_REQUIRING);
        return Lighting.getElByValue(lightRequiringVal);
    }

    private Watering getWatering(Node wateringNode) {
        String measureName = ((Element) wateringNode)
                .getAttribute(Attributes.MEASURE);
        Watering.Measure measure = Watering.Measure.getElByValue(measureName);
        int value = Integer.parseInt(wateringNode.getTextContent());
        return new Watering(measure, value);
    }

    private Multiplying getMultiplying(Node flowerWateringNode) {
        return Multiplying.getElByValue(flowerWateringNode.getTextContent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLDomParser that = (XMLDomParser) o;
        return fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
