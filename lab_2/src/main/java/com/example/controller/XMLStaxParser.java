package com.example.controller;

import com.example.entity.*;
import com.example.util.Provider;
import com.example.constants.Attributes;
import com.example.constants.Tags;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLStaxParser implements Parser {

    private final String fileName;

    public XMLStaxParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Flowers getFlowers() {
        Flowers flowers = new Flowers();
        FlowerBuilder flowerBuilder = null;

        VisualParameters visualParameters = null;
        AveLenFlower aveLenFlower = null;

        GrowingTips growingTips = null;
        Temperature temperature = null;
        Watering watering = null;

        XMLInputFactory xmlInputFactory = Provider.provideXMLInputFactory();
        try {
            XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String tag = startElement.getName().getLocalPart();
                    switch (tag) {
                        case Tags.FLOWER:
                            flowerBuilder = new FlowerBuilder();
                            break;
                        case Tags.NAME:
                            String name = getEventData(eventReader.nextEvent());
                            assert flowerBuilder != null;
                            flowerBuilder.name(name);
                            break;
                        case Tags.SOIL:
                            Soil soil = Soil.getElByValue(getEventData(eventReader.nextEvent()));
                            assert flowerBuilder != null;
                            flowerBuilder.soil(soil);
                            break;
                        case Tags.ORIGIN:
                            String origin = getEventData(eventReader.nextEvent());
                            assert flowerBuilder != null;
                            flowerBuilder.origin(origin);
                            break;
                        case Tags.VISUAL_PARAMETERS:
                            visualParameters = new VisualParameters();
                            break;
                        case Tags.STEM_COLOR:
                            String stemColor = getEventData(eventReader.nextEvent());
                            assert visualParameters != null;
                            visualParameters.setStemColor(stemColor);
                            break;
                        case Tags.LEAF_COLOR:
                            String leafColor = getEventData(eventReader.nextEvent());
                            assert visualParameters != null;
                            visualParameters.setLeafColor(leafColor);
                            break;
                        case Tags.AVE_LEN_FLOWER:
                            aveLenFlower = new AveLenFlower();
                            AveLenFlower.Measure lenFlowerMeasure =
                                    AveLenFlower
                                            .Measure
                                            .getElByValue(startElement
                                                    .getAttributeByName(new QName(Attributes.MEASURE))
                                                    .getValue());
                            String lenFlowerData = getEventData(eventReader.nextEvent());
                            aveLenFlower.setMeasure(lenFlowerMeasure);
                            assert lenFlowerData != null;
                            aveLenFlower.setValue(Integer.parseInt(lenFlowerData));
                            break;
                        case Tags.GROWING_TIPS:
                            growingTips = new GrowingTips();
                            break;
                        case Tags.TEMPERATURE:
                            temperature = new Temperature();
                            Temperature.Measure temperatureMeasure =
                                    Temperature
                                            .Measure
                                            .getElByValue(startElement
                                                    .getAttributeByName(new QName(Attributes.MEASURE))
                                                    .getValue());
                            String temperatureData = getEventData(eventReader.nextEvent());
                            temperature.setMeasure(temperatureMeasure);
                            assert temperatureData != null;
                            temperature.setValue(Integer.parseInt(temperatureData));
                            break;
                        case Tags.LIGHTING:
                            String lightingAttrData = startElement
                                    .getAttributeByName(new QName(Attributes.LIGHT_REQUIRING))
                                    .getValue();
                            assert growingTips != null;
                            growingTips.setLightRequiring(Lighting.getElByValue(lightingAttrData));
                            break;
                        case Tags.WATERING:
                            watering = new Watering();
                            Watering.Measure wateringMeasure =
                                    Watering
                                            .Measure
                                            .getElByValue(startElement
                                                    .getAttributeByName(new QName(Attributes.MEASURE))
                                                    .getValue());
                            String wateringData = getEventData(eventReader.nextEvent());
                            watering.setMeasure(wateringMeasure);
                            assert wateringData != null;
                            watering.setValue(Integer.parseInt(wateringData));
                            break;
                        case Tags.MULTIPLYING:
                            Multiplying multiplying = Multiplying.getElByValue(getEventData(eventReader.nextEvent()));
                            assert flowerBuilder != null;
                            flowerBuilder.multiplying(multiplying);
                            break;
                        default:
                            break;
                    }
                }

                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    String tag = endElement.getName().getLocalPart();
                    switch (tag) {
                        case Tags.FLOWER:
                            assert flowerBuilder != null;
                            flowers.add(flowerBuilder.build());
                            break;
                        case Tags.VISUAL_PARAMETERS:
                            assert visualParameters != null;
                            visualParameters.setAveLenFlower(aveLenFlower);
                            assert flowerBuilder != null;
                            flowerBuilder.visualParameters(visualParameters);
                            break;
                        case Tags.GROWING_TIPS:
                            assert growingTips != null;
                            growingTips.setTemperature(temperature);
                            growingTips.setWatering(watering);
                            assert flowerBuilder != null;
                            flowerBuilder.growingTips(growingTips);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            Logger.getLogger(XMLStaxParser.class.getName())
                    .log(Level.WARNING, "Couldn't parse file " + fileName, e);
        }
        return flowers;
    }

    private String getEventData(XMLEvent event) {
        if (!event.isCharacters()) {
            return null;
        }
        return event.asCharacters().getData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLStaxParser that = (XMLStaxParser) o;
        return fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
