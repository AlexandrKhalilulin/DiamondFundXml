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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withPrefix;

public class SaxModelParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(SaxModelParser.class);
    Map<String, Map<String, Method>> classesMethodsMap;
    Map<Class, Function> adaptersMap;
    Set<Class> setSubClasses;
    Map<String, Object> mapTagClass;

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

    public <T extends BaseEntity> void configure(Properties properties, Class clazz) {
        Reflections reflections = new Reflections(properties.getProperty("package"));
        Set<Class<? extends NamedEntity>> allClasses = reflections.getSubTypesOf(NamedEntity.class);
        //set map adapters
        setAdaptersMap();
        //set classesMethodsMap
        classesMethodsMap = new HashMap<>();
        for (Class cl : allClasses) {
            Map<String, Method> methodMap = new HashMap<>();
            Set<Method> setters = getAllMethods(cl, withPrefix("set"));
            for (Method method : setters) {
                methodMap.put(method.getName().toLowerCase().substring(3), method);
            }
            classesMethodsMap.put(cl.getSimpleName().toLowerCase(), methodMap);
            log.info(String.valueOf(classesMethodsMap.get(cl.getSimpleName().toLowerCase())));
        }
        //setSubClasses
        setSubClasses = new HashSet<>();
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] paramTypes = constructor.getParameterTypes();
            for (Class paramType : paramTypes) {
                if (paramType.getName().contains(properties.getProperty("package"))) {
                    setSubClasses.add(paramType);
                }
            }
        }
        setSubClasses.add(clazz);
    }

    public void setAdaptersMap() {
        adaptersMap = new HashMap<>();
        Function<String, Integer> integerAdapter = Integer::parseInt;
        adaptersMap.put(Integer.class, integerAdapter);
        Function<String, Double> doubleAdapter = Double::parseDouble;
        adaptersMap.put(Double.class, doubleAdapter);
        Function<String, String> stringAdapter = String::new;
        adaptersMap.put(String.class, stringAdapter);
        Function<String, UUID> uuidAdapter = UUID::fromString;
        adaptersMap.put(UUID.class, uuidAdapter);
        adaptersMap.put(int.class, integerAdapter);
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
            log.info("Start parsing");
            mapTagClass = new HashMap<>();
        }

        @Override
        public void endDocument() throws SAXException {
            log.info("Our instance is - {}", String.valueOf(mapTagClass.get(stackTags.getLast().toLowerCase())));
            log.info("End parsing");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            stackTags.add(qName);
            log.info(String.valueOf(stackTags));

            //set map of instance innerclasses of source class + instance source class
            for (Class cl : setSubClasses) {
                if (qName.toLowerCase().equals(cl.getSimpleName().toLowerCase())) {
                    try {
                        t = (T) cl.newInstance();
                        mapTagClass.put(stackTags.getLast().toLowerCase(), t);
                        log.info(stackTags.getLast().toLowerCase());
                    } catch (InstantiationException e) {
                        throw new SaxModelParserException("InstantiationException", e);
                    } catch (IllegalAccessException e) {
                        throw new SaxModelParserException("IllegalAccessException", e);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            //prepare
            Map<String, Method> map = new HashMap<>();
            String acc = String.valueOf(accumulator).trim();
            String currentElement = null;

            //
            if (stackTags.size() > 1) {
                //load map method for current element
                stackTags.removeLast();

                log.info(stackTags.getLast().toLowerCase());
                map = classesMethodsMap.get(stackTags.getLast().toLowerCase());
                log.info(String.valueOf(map));
                currentElement = stackTags.getLast().toLowerCase();
                log.info(currentElement);
                stackTags.add(qName);

                //invoke method for current parametr current elementa
                Class parametrClass = map.get(stackTags.getLast().toLowerCase()).getParameterTypes()[0];
                log.info(String.valueOf(parametrClass));
                try {
                    if (mapTagClass.keySet().contains(stackTags.getLast().toLowerCase()))
                        map.get(stackTags.getLast().toLowerCase()).invoke(mapTagClass.get(currentElement), mapTagClass.get(stackTags.getLast().toLowerCase()));
                    else
                        map.get(stackTags.getLast().toLowerCase()).invoke(mapTagClass.get(currentElement), adaptersMap.get(parametrClass).apply(acc));
                } catch (IllegalAccessException e) {
                    throw new SaxModelParserException("IllegalAccessException", e);
                } catch (InvocationTargetException e) {
                    throw new SaxModelParserException("InvocationTargetException", e);
                }

                //clear accamulator
                accumulator.setLength(0);
                stackTags.removeLast();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            accumulator.append(ch, start, length);

        }
    }
}
