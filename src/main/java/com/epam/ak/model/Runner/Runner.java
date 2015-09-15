package com.epam.ak.model.Runner;

import com.epam.ak.model.model.Pavilion;
import com.epam.ak.model.parser.SaxModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Runner {
    static Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        testSaxModelParser();

    }

    private static void testSaxModelParser() {
        log.info("Try to load source file.xml");
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream("pavilion.xml");
        log.info("Loading source pavilion.xml");
        Properties properties = new Properties();
        log.info("Try to load properties from file");
        try {
            properties.load(Runner.class.getClassLoader().getResourceAsStream("parser.properties"));
        } catch (IOException e) {
            new RunnerException("IOException e", e);
        }
        log.info("Loading properties from file");
        SaxModelParser saxModelParser = new SaxModelParser();
        saxModelParser.configure(properties, Pavilion.class);
        saxModelParser.parse(inputStream, Pavilion.class);
    }

}
