package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseEntity;
import com.epam.ak.model.model.NamedEntity;
import org.reflections.Reflections;
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
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withPrefix;

public class SaxModelParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(SaxModelParser.class);
    Map<Class, Map<String, Method>> classesMethodsMap;
    Map<Class, Function> functionMap;

    public <T extends BaseEntity> T parse(String filename, Class clazz) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return parse(inputStream, clazz);
        } catch (FileNotFoundException e) {
            throw new SaxModelParserException("File Not Found", e);
        } catch (IOException e) {
            throw new SaxModelParserException("IO Exception", e);
        }
    }

    public <T extends BaseEntity> T parse(InputStream inputStream, Class clazz) {
        T instance;
        try {
            instance = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new SaxModelParserException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new SaxModelParserException("IllegalAccessException", e);
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            SaxModelParserHandler saxModelParserHandler = new SaxModelParserHandler();
            saxParser.parse(inputStream, saxModelParserHandler);
            instance = (T) saxModelParserHandler.getT();
        } catch (ParserConfigurationException e) {
            throw new SaxModelParserException("SaxParserConfigurationException", e);
        } catch (SAXException e) {
            throw new SaxModelParserException("SAXException", e);
        } catch (IOException e) {
            throw new SaxModelParserException("IO Exception", e);
        }
        return instance;
    }

    public void configure(Properties properties) {
        Reflections reflections = new Reflections(properties.getProperty("package"));
        Set<Class<? extends NamedEntity>> allClasses = reflections.getSubTypesOf(NamedEntity.class);

        //set map adapters
        setFunctionMap();

        //set classesMethodsMap
        Map<String, Method> methodMap = new HashMap<>();
        classesMethodsMap = new HashMap<>();
        for (Class cl : allClasses) {
            Set<Method> setters = getAllMethods(cl, withPrefix("set"));
            for (Method method : setters) {
                methodMap.put(method.getName(), method);
            }
            classesMethodsMap.put(cl, methodMap);
            log.info("Class - {}, Map - {}", cl, String.valueOf(classesMethodsMap.get(cl)));
        }
    }

    public void setFunctionMap() {
        functionMap = new HashMap<>();
        Function<String, Integer> integerAdapter = Integer::parseInt;
        functionMap.put(Integer.class, integerAdapter);
        Function<String, Double> doubleAdapter = Double::parseDouble;
        functionMap.put(Double.class, doubleAdapter);
        Function<String, String> stringAdapter = String::new;
        functionMap.put(String.class, stringAdapter);
        Function<String, UUID> uuidAdapter = UUID::fromString;
        functionMap.put(UUID.class, uuidAdapter);
        functionMap.put(int.class, integerAdapter);
    }

    public class SaxModelParserHandler<T extends BaseEntity> extends DefaultHandler {
        Logger log = LoggerFactory.getLogger(SaxModelParserHandler.class);
        StringBuilder accumulator = new StringBuilder();
        Map<String, Method> methodMap;
        Deque<String> stackTags = new LinkedList<>();
        T t;

        public SaxModelParserHandler() {
        }

        public T getT() {
            return t;
        }

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            stackTags.add(qName);
            log.info(String.valueOf(stackTags));
            for (Class cl : classesMethodsMap.keySet()) {
                if (qName.toLowerCase().equals(cl.getSimpleName().toLowerCase())) {
                    log.info("Correct");
                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            Map<String, Method> map = null;

            accumulator.setLength(0);
            stackTags.removeLast();
            log.info(String.valueOf(stackTags));
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            accumulator.append(ch, start, length);
        }
    }
}
