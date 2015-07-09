package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModelParserHandler extends DefaultHandler {
    Logger log = LoggerFactory.getLogger(ModelParserHandler.class);
    StringBuilder accumulator = new StringBuilder();
    Map<String, Method> methodMap;

    public ModelParserHandler() {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        for (String s : methodMap.keySet()) {
            if (qName.equals(s.toLowerCase())) {
                Method method = methodMap.get(s);

            }
        }
        accumulator.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        accumulator.append(ch, start, length);
    }

    public <T extends BaseModel> void configure(Class clazz) {
        methodMap = new HashMap<>();
        T one;
        try {
            one = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ModelParserHandlerException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new ModelParserHandlerException("IllegalAccessException", e);
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().contains("set")) {
                String s = method.getName();
                String l = s.substring(3, s.length());
                methodMap.put(l, method);
            }
        }
    }

    public <T extends BaseModel> T getInsCl(Class clazz) {
        T insCl = null;
        try {
            insCl = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return insCl;
    }
}
