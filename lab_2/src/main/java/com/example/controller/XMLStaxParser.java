package com.example.controller;

import com.example.entity.*;
import com.example.util.Provider;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLStaxParser implements Parser {

    private final String fileName;

    public XMLStaxParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Flowers getFlowers() {
        XMLInputFactory xmlInputFactory = Provider.provideXMLInputFactory();
        FlowersHandler handler = new FlowersHandler();
        try {
            XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while (eventReader.hasNext()) {
                XMLEvent nextEvent = eventReader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();

                    nextEvent = eventReader.nextEvent();
                    String name = startElement.getName().getLocalPart();

                    List<Attribute> attributes = new ArrayList<>();
                    Iterator<Attribute> iterator = startElement.getAttributes();
                    while (iterator.hasNext()) {
                        attributes.add(iterator.next());
                    }

                    Map<String, String> attributeMap = new HashMap<>();

                    for (Attribute attribute : attributes) {
                        attributeMap.put(attribute.getName().getLocalPart(), attribute.getValue());
                    }

                    if (nextEvent.isCharacters()) {
                        handler.setField(name, nextEvent.asCharacters().getData(), attributeMap);
                    } else if (!nextEvent.isStartDocument() && !nextEvent.isEndDocument()) {
                        handler.setField(name, null, attributeMap);
                    }
                }
            }
            return handler.getFlowers();
        } catch (XMLStreamException | FileNotFoundException e) {
            Logger.getLogger(XMLStaxParser.class.getName())
                    .log(Level.WARNING, "Couldn't parse file " + fileName, e);
        }
        return new Flowers();
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
