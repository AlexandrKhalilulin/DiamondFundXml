package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseEntity;
import com.epam.ak.model.model.NamedEntity;
import com.epam.ak.model.model.PreciousGem;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class SaxPreciousGemParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(SaxPreciousGemParser.class);
    Map<String, Map<String, Method>> classesSettersMap;
    Map<Class, Function> functionMap;

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

    public void configure(Properties properties) {
        setFunctionMap();
        log.info(String.valueOf(functionMap));
        classesSettersMap = new HashMap<>();
        Reflections reflections = new Reflections(properties.getProperty("package"));
        Set<Class<? extends NamedEntity>> allClasses = reflections.getSubTypesOf(NamedEntity.class);
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
    public <T extends BaseEntity> T parse(String filename, Class clazz) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return parse(inputStream, clazz);
        } catch (FileNotFoundException e) {
            throw new SaxPreciousGemParserException("File Not Found", e);
        } catch (IOException e) {
            throw new SaxPreciousGemParserException("IO Exception", e);
        }
    }

    @Override
    public <T extends BaseEntity> T parse(InputStream inputStream, Class clazz) {
        T instance = null;
        try {
            instance = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new SaxPreciousGemParserException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new SaxPreciousGemParserException("IllegalAccessException", e);
        }

        PreciousGem gem;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;

        try {
            saxParser = factory.newSAXParser();
            PreciousGemParserHandler handler = new PreciousGemParserHandler(instance);
            saxParser.parse(inputStream, handler);
            instance = handler.getInstance();
        } catch (ParserConfigurationException e) {
            throw new SaxPreciousGemParserException("SaxParserConfigurationException", e);
        } catch (SAXException e) {
            throw new SaxPreciousGemParserException("SAXException", e);
        } catch (IOException e) {
            throw new SaxPreciousGemParserException("IO Exception", e);
        }
        return instance;
    }

    public class PreciousGemParserHandler extends DefaultHandler {
        StringBuilder accumulator = new StringBuilder();
        Deque<String> stackTags = new LinkedList<>();
        private PreciousGem gem = new PreciousGem();

        public <T extends BaseEntity> PreciousGemParserHandler(T instance) {
            // T instance = (T) clazz.new
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            stackTags.add(qName);
            log.info(String.valueOf(stackTags));
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            Map<String, Method> map = null;
            if (stackTags.size() > 1) {
                stackTags.removeLast();
                map = classesSettersMap.get(stackTags.getLast().toLowerCase());
                stackTags.add(qName);

            } else {
                map = classesSettersMap.get(stackTags.getLast());
            }
            log.info(String.valueOf(map));
            String acc = String.valueOf(accumulator);
            acc = acc.trim();
            Type a = null;
            if (map != null) {
                for (Parameter parameter : map.get(stackTags.getLast()).getParameters()) {
                    a = parameter.getParameterizedType();
                }

                log.info(String.valueOf(a));

                log.info(String.valueOf(functionMap.get(a)));

                try {
                    map.get(stackTags.getLast()).invoke(gem, functionMap.get(a).apply(acc));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            log.info(String.valueOf(gem));
            accumulator.setLength(0);
            stackTags.removeLast();
            log.info(String.valueOf(stackTags));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            accumulator.append(ch, start, length);
        }

        public <T extends BaseEntity> T getInstance() {
            return null;
        }
    }

}
