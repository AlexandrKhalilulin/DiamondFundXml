package com.epam.ak.model.parser;

import com.epam.ak.model.model.BaseEntity;

import java.io.InputStream;

public interface AbstractParser {
    <T extends BaseEntity> T parse(String filename, Class clazz);

    <T extends BaseEntity> T parse(InputStream inputStream, Class clazz);
}
