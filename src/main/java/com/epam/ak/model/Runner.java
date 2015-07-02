package com.epam.ak.model;

import com.epam.ak.model.model.PreciousGem;
import com.epam.ak.model.parser.SaxPreciousGemParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Runner {
    Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        //ModelParser parser = new ModelParser();
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream("preciousGem.xml");
        SaxPreciousGemParser saxPreciousGemParser = new SaxPreciousGemParser();
        saxPreciousGemParser.parse(inputStream, PreciousGem.class);
    }
}
