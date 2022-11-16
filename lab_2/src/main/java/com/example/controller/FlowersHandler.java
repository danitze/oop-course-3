package com.example.controller;

import com.example.constants.Tags;
import com.example.entity.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Objects;

public class FlowersHandler extends DefaultHandler {

    private Flowers flowers;

    private FlowerBuilder flowerBuilder;
    private StringBuilder tagTextBuilder;

    private VisualParameters visualParameters;
    private AveLenFlower aveLenFlower;

    private GrowingTips growingTips;
    private Temperature temperature;
    private Lighting lighting;
    private Watering watering;

    @Override
    public void startDocument() {
        flowers = new Flowers();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case Tags.FLOWER:
                flowerBuilder = new FlowerBuilder();
                break;
            case Tags.NAME:
            case Tags.SOIL:
            case Tags.ORIGIN:
            case Tags.STEM_COLOR:
            case Tags.LEAF_COLOR:
            case Tags.MULTIPLYING:
                tagTextBuilder = new StringBuilder();
                break;
            case Tags.VISUAL_PARAMETERS:
                visualParameters = new VisualParameters();
                break;
            case Tags.AVE_LEN_FLOWER:
                aveLenFlower = new AveLenFlower();
                AveLenFlower.Measure aveLenFlowerMeasure =
                        AveLenFlower.Measure.getElByValue(attributes.getValue(com.example.constants.Attributes.MEASURE));
                aveLenFlower.setMeasure(aveLenFlowerMeasure);
                tagTextBuilder = new StringBuilder();
                break;
            case Tags.GROWING_TIPS:
                growingTips = new GrowingTips();
                break;
            case Tags.TEMPERATURE:
                temperature = new Temperature();
                Temperature.Measure temperatureMeasure =
                        Temperature.Measure.getElByValue(attributes.getValue(com.example.constants.Attributes.MEASURE));
                temperature.setMeasure(temperatureMeasure);
                tagTextBuilder = new StringBuilder();
                break;
            case Tags.LIGHTING:
                lighting = Lighting.getElByValue(attributes.getValue(com.example.constants.Attributes.LIGHT_REQUIRING));
                break;
            case Tags.WATERING:
                watering = new Watering();
                Watering.Measure wateringMeasure =
                        Watering.Measure.getElByValue(attributes.getValue(com.example.constants.Attributes.MEASURE));
                watering.setMeasure(wateringMeasure);
                tagTextBuilder = new StringBuilder();
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case Tags.FLOWER:
                flowers.add(flowerBuilder.build());
                break;
            case Tags.NAME:
                flowerBuilder.name(tagTextBuilder.toString());
                break;
            case Tags.SOIL:
                flowerBuilder.soil(Soil.getElByValue(tagTextBuilder.toString()));
                break;
            case Tags.ORIGIN:
                flowerBuilder.origin(tagTextBuilder.toString());
                break;
            case Tags.VISUAL_PARAMETERS:
                flowerBuilder.visualParameters(visualParameters);
                break;
            case Tags.STEM_COLOR:
                visualParameters.setStemColor(tagTextBuilder.toString());
                break;
            case Tags.LEAF_COLOR:
                visualParameters.setLeafColor(tagTextBuilder.toString());
                break;
            case Tags.AVE_LEN_FLOWER:
                aveLenFlower.setValue(Integer.parseInt(tagTextBuilder.toString()));
                visualParameters.setAveLenFlower(aveLenFlower);
                break;
            case Tags.GROWING_TIPS:
                flowerBuilder.growingTips(growingTips);
                break;
            case Tags.TEMPERATURE:
                temperature.setValue(Integer.parseInt(tagTextBuilder.toString()));
                growingTips.setTemperature(temperature);
                break;
            case Tags.LIGHTING:
                growingTips.setLightRequiring(lighting);
                break;
            case Tags.WATERING:
                watering.setValue(Integer.parseInt(tagTextBuilder.toString()));
                growingTips.setWatering(watering);
                break;
            case Tags.MULTIPLYING:
                flowerBuilder.multiplying(Multiplying.getElByValue(tagTextBuilder.toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if(tagTextBuilder == null) {
            tagTextBuilder = new StringBuilder();
            return;
        }
        tagTextBuilder.append(ch, start, length);
    }

    public Flowers getFlowers() {
        return flowers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowersHandler that = (FlowersHandler) o;
        return Objects.equals(flowers, that.flowers)
                && Objects.equals(flowerBuilder, that.flowerBuilder)
                && Objects.equals(tagTextBuilder, that.tagTextBuilder)
                && Objects.equals(visualParameters, that.visualParameters)
                && Objects.equals(aveLenFlower, that.aveLenFlower)
                && Objects.equals(growingTips, that.growingTips)
                && Objects.equals(temperature, that.temperature)
                && lighting == that.lighting
                && Objects.equals(watering, that.watering);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowers, flowerBuilder, tagTextBuilder, visualParameters, aveLenFlower, growingTips, temperature, lighting, watering);
    }
}
