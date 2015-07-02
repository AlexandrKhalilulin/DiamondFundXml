package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseModel;

import java.io.InputStream;

public interface AbstractParser {
    <T extends BaseModel> T parse(String filename, Class clazz);

    <T extends BaseModel> T parse(InputStream inputStream, Class clazz);
}
