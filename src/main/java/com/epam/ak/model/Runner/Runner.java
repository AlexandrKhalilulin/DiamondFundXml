package com.epam.ak.model.Runner;

import com.epam.ak.model.model.PreciousGem;
import com.epam.ak.model.parser.SaxModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Runner {
    static Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        log.debug("Try to load source file.xml");
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream("preciousGem.xml");
        log.debug("Loading source file.xml");

        Properties properties = new Properties();

        log.debug("Try to load properties from file");
        try {
            properties.load(Runner.class.getClassLoader().getResourceAsStream("parser.properties"));
        } catch (IOException e) {
            new RunnerException("IOException e", e);
        }
        log.debug("Loading properties from file");
        SaxModelParser saxModelParser = new SaxModelParser();
        saxModelParser.configure(properties);
        saxModelParser.parse(inputStream, PreciousGem.class);
    }

}
