package com.epam.ak.model.model;

import java.util.UUID;

public class Color extends NamedEntity {

    public Color(UUID uuid, Integer id, String name) {
        super(uuid, id, name);
    }

    public Color() {
    }

    @Override
    public String toString() {
        return "Color { " +
                "Name = " + getName() +
                ", Id = " + getId() +
                ", UUID = " + getUuid() + "}";
    }
}
