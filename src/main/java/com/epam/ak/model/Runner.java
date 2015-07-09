package com.epam.ak.model;

import com.epam.ak.model.model.NamedModel;
import com.epam.ak.model.model.PreciousGem;
import com.epam.ak.model.parser.SaxPreciousGemParser;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Set;

public class Runner {
    Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        //ModelParser parser = new ModelParser();
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream("preciousGem.xml");
        SaxPreciousGemParser saxPreciousGemParser = new SaxPreciousGemParser();

        Reflections reflections = new Reflections("com.epam.ak.model");
        Set<Class<? extends NamedModel>> allClasses = reflections.getSubTypesOf(NamedModel.class);
        saxPreciousGemParser.configure(allClasses);
        saxPreciousGemParser.parse(inputStream, PreciousGem.class);
        //saxPreciousGemParser.parse(inputStream, PreciousGem.class);

    }
}
