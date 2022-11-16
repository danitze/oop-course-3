package com.example.controller;

import com.example.entity.Flowers;
import com.example.util.Provider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLSaxParser implements Parser {

    private final SAXParser saxParser;
    private final FlowersHandler handler;
    private final String fileName;

    public XMLSaxParser(String fileName) throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = Provider.provideSAXParserFactory();
        this.saxParser = factory.newSAXParser();
        this.handler = new FlowersHandler();
        this.fileName = fileName;
    }

    @Override
    public Flowers getFlowers() {
        try {
            saxParser.parse(new File(fileName), handler);
        } catch (SAXException | IOException e) {
            Logger.getLogger(XMLSaxParser.class.getName())
                    .log(Level.WARNING, "Couldn't parse file " + fileName, e);
        }
        return handler.getFlowers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLSaxParser saxParser1 = (XMLSaxParser) o;
        return handler.equals(saxParser1.handler)
                && fileName.equals(saxParser1.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handler, fileName);
    }
}
