package com.epam.ak.model.parser;

import com.epam.ak.model.model.PreciousGem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SaxPreciousGemParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(SaxPreciousGemParser.class);

    @Override
    public PreciousGem parse(String filename, Class clazz) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return parse(inputStream, clazz);
        } catch (FileNotFoundException e) {
            throw new SaxPreciousGemParserException("File Not Found", e);
        } catch (IOException e) {
            throw new SaxPreciousGemParserException("IO Exception", e);
        }
    }

    @Override
    public PreciousGem parse(InputStream inputStream, Class clazz) {
        PreciousGem gem = new PreciousGem();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;

        try {
            saxParser = factory.newSAXParser();
            PreciousGemParserHandler handler = new PreciousGemParserHandler();
            handler.configure();
            saxParser.parse(inputStream, handler);
            gem = handler.getGem();
        } catch (ParserConfigurationException e) {
            throw new SaxPreciousGemParserException("SaxParserConfigurationException", e);
        } catch (SAXException e) {
            throw new SaxPreciousGemParserException("SAXException", e);
        } catch (IOException e) {
            throw new SaxPreciousGemParserException("IO Exception", e);
        }
        return gem;
    }

    public class PreciousGemParserHandler extends DefaultHandler {
        StringBuilder accumulator = new StringBuilder();
        Map<String, Method> methodMap;
        Map<String, Map<String, Method>> info;
        private Stack stack = new Stack();
        private PreciousGem gem;

        public PreciousGem getGem() {
            return gem;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            stack.push(qName);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            log.info(qName);
            for (String s : methodMap.keySet()) {
                log.info(s);
                if (qName.equals(s)) try {
                    methodMap.get(s).invoke(gem, accumulator);
                    log.info(gem.toString());
                } catch (IllegalAccessException e) {
                    throw new SaxPreciousGemParserException("IllegalAccessException", e);
                } catch (InvocationTargetException e) {
                    throw new SaxPreciousGemParserException("InvocationTargetException", e);
                }
            }
            accumulator.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            accumulator.append(ch, start, length);
        }

        public void configure() {
            info = new HashMap<>();
            methodMap = new HashMap<>();
            Method[] methods = PreciousGem.class.getMethods();
            for (Method method : methods) {
                if (method.getName().contains("set")) {
                    String s = method.getName();
                    String l = s.substring(3, s.length());
                    methodMap.put(l.toLowerCase(), method);
                }
            }
            log.info(String.valueOf(methodMap));
            info.put(PreciousGem.class.getSimpleName().toLowerCase(), methodMap);
        }
    }

}
