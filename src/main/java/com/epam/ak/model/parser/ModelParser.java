package com.epam.ak.model.parser;

import com.epam.ak.model.model.Pavilion;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelParser implements AbstractParser{
    Logger log = LoggerFactory.getLogger(ModelParser.class);

    public Pavilion parsePavilion(String filename) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return parsePavilion(inputStream);
        } catch (FileNotFoundException e) {
            throw new ModelParserException("File Not Found", e);
        } catch (IOException e) {
            throw new ModelParserException("IO Exception", e);
        }
    }

    public Pavilion parsePavilion(InputStream inputStream) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        Pavilion pavilion;
        try {
            saxParser = factory.newSAXParser();
            PavilionHandler pavilionHandler = new PavilionHandler();
            saxParser.parse(inputStream, pavilionHandler);
            pavilion = pavilionHandler.getPavilion();
        } catch (ParserConfigurationException e) {
            throw new ModelParserException("SaxParserConfigurationException", e);
        } catch (SAXException e) {
            throw new ModelParserException("SAXException", e);
        } catch (IOException e) {
            throw new ModelParserException("IO Exception", e);
        }
        return pavilion;
    }

    public Map<String, Class> scanClass(Class aClass) {
        Map<String, Class> map = new HashMap<>();
        List<Class> list = new ArrayList<>();

        // 	TODO recursively calling the superclass map and fill all fields of the superclasses
        
        Field[] fields = aClass.getDeclaredFields();
        for(Field field: fields){
            Class fieldType = field.getType();
            log.info("Name - {}, type - {}", field.getName(), fieldType.getName());
            map.put(field.getName(), fieldType);
        }
        
        return null;
    }

}
