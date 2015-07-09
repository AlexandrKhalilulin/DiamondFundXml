package com.epam.ak.model.parser;

import com.epam.ak.model.model.NamedModel;
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
import java.lang.reflect.Parameter;
import java.util.*;

public class SaxPreciousGemParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(SaxPreciousGemParser.class);
    Map<String, Map<String, Method>> classesSettersMap;

    public void configure(Set<Class<? extends NamedModel>> allClasses) {
        classesSettersMap = new HashMap<>();
        for (Class cl : allClasses) {
            Map<String, Method> methodMap = new HashMap<>();
            Method[] methods = cl.getMethods();
            for (Method method : methods) {
                if (method.getName().contains("set")) {
                    String s = method.getName();
                    String l = s.substring(3, s.length());
                    methodMap.put(l.toLowerCase(), method);
                }
            }
            classesSettersMap.put(cl.getSimpleName().toLowerCase(), methodMap);
        }
        for (String s : classesSettersMap.keySet()) {
            log.info("Class - {}, Map - {}", s, String.valueOf(classesSettersMap.get(s)));
        }
    }

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
        List<String> stackTags = new LinkedList<>();
        private PreciousGem gem = new PreciousGem();

        public PreciousGem getGem() {
            return gem;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            stackTags.add(qName);
            log.info(String.valueOf(stackTags));
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            Map<String, Method> methodMap = classesSettersMap.get("preciousgem");
            String acc = String.valueOf(accumulator);
            for (String s : methodMap.keySet()) {
                if (qName.equals(s)) try {
                    methodMap.get(s).setAccessible(true);
                    for (Parameter parameter : methodMap.get(s).getParameters()) {
                        try {
                            Object obj = ((Class) parameter.getParameterizedType()).newInstance();
                            log.info(String.valueOf(obj));
                            obj = ((obj.getClass()) acc);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }

                    }

                    methodMap.get(s).invoke(gem, acc);
                } catch (IllegalAccessException e) {
                    throw new SaxPreciousGemParserException("IllegalAccessException", e);
                } catch (InvocationTargetException e) {
                    throw new SaxPreciousGemParserException("InvocationTargetException", e);
                }
            }
            log.info(String.valueOf(gem));
            accumulator.setLength(0);
            stackTags.remove(qName);
            log.info(String.valueOf(stackTags));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            accumulator.append(ch, start, length);
        }

    }

}
