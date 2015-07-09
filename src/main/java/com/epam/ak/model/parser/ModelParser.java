package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseModel;
import com.epam.ak.model.model.NamedModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelParser implements AbstractParser {
    static Logger log = LoggerFactory.getLogger(ModelParser.class);
    Map<String, Map<String, Method>> classesSettersMap;

    public void configure(Set<Class<? extends NamedModel>> allClasses) {
        classesSettersMap = new HashMap<>();
        for (Class cl : allClasses) {
            Map<String, Method> settersMap = new HashMap<>();
            Method[] methods = cl.getMethods();
            for (Method method : methods) {
                if (method.getName().contains("set")) {
                    String s = method.getName();
                    String l = s.substring(3, s.length());
                    settersMap.put(l.toLowerCase(), method);
                }
            }
            classesSettersMap.put(cl.getSimpleName().toLowerCase(), settersMap);
        }
        for (String s : classesSettersMap.keySet()) {
        }
    }


    public <T extends BaseModel> T parse(String filename, Class clazz) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return parse(inputStream, clazz);
        } catch (FileNotFoundException e) {
            throw new ModelParserException("File Not Found", e);
        } catch (IOException e) {
            throw new ModelParserException("IO Exception", e);
        }
    }

    public <T extends BaseModel> T parse(InputStream inputStream, Class clazz) {
        T one;
        try {
            one = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ModelParserException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new ModelParserException("IllegalAccessException", e);
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;

        try {
            saxParser = factory.newSAXParser();
            ModelParserHandler modelParserHandler = new ModelParserHandler();
            modelParserHandler.configure(clazz);
            saxParser.parse(inputStream, modelParserHandler);
            one = modelParserHandler.getInsCl(clazz);
        } catch (ParserConfigurationException e) {
            throw new ModelParserException("SaxParserConfigurationException", e);
        } catch (SAXException e) {
            throw new ModelParserException("SAXException", e);
        } catch (IOException e) {
            throw new ModelParserException("IO Exception", e);
        }
        return one;
    }

}
