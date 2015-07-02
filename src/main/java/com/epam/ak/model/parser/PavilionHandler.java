package com.epam.ak.model.parser;

import com.epam.ak.model.model.Pavilion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class PavilionHandler extends DefaultHandler {
    Logger log = LoggerFactory.getLogger(PavilionHandler.class);
    StringBuilder accumulator = new StringBuilder();
    private Pavilion pavilion;

    public PavilionHandler() {
        super();
    }

    public Pavilion getPavilion() {
        return pavilion;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        log.info("localName - {}", localName);
        log.info("qName - {}", qName);

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        log.info("localName - {}", localName);
        log.info("qName - {}", qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        accumulator.append(ch, start, length);
    }
}
