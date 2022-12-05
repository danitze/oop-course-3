package com.example.controller;

import com.example.util.Provider;
import com.example.entity.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLDomParser implements Parser {

    private final String fileName;

    public XMLDomParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Flowers getFlowers() {
        try {
            DocumentBuilderFactory dbf = Provider.provideDocumentBuilderFactory();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(fileName));
            Element rootNode = document.getDocumentElement();
            FlowersHandler flowersHandler = new FlowersHandler();
            NodeList flowerNodes = rootNode.getElementsByTagName(flowersHandler.getName());
            for (int flowerNode = 0; flowerNode < flowerNodes.getLength(); flowerNode++) {
                Element flowerElement = (Element) flowerNodes.item(flowerNode);
                traverseNodes(flowerElement, flowersHandler);
            }
            return flowersHandler.getFlowers();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(XMLDomParser.class.getName())
                    .log(Level.WARNING, "Couldn't parse file " + fileName, e);
        }
        return new Flowers();
    }

    private void traverseNodes(Node node, FlowersHandler flowersHandler) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Map<String, String> attributes = new HashMap<>();
            if (node.getAttributes() != null) {
                for (int i = 0; i < node.getAttributes().getLength(); i++) {
                    attributes.put(node.getAttributes().item(i).getNodeName(),
                            node.getAttributes().item(i).getTextContent());
                }
            }
            flowersHandler.setField(node.getNodeName(), node.getTextContent(), attributes);
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                traverseNodes(node.getChildNodes().item(i), flowersHandler);
            }
        }
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
