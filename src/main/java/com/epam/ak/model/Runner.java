package com.epam.ak.model;

import com.epam.ak.model.model.NamedModel;
import com.epam.ak.model.parser.ModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Runner {
    Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        ModelParser parser = new ModelParser();
        InputStream inputStream = Runner.class.getClassLoader().getResourceAsStream("pavilion.xml");
        //parser.parsePavilion(inputStream);
        //parser.scanClass(Pavilion.class);
        //parser.scanClass(BaseModel.class);
        parser.scanClass(NamedModel.class);
    }
}
