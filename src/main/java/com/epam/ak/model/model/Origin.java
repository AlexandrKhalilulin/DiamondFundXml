package com.epam.ak.model.model;

import java.util.UUID;

public class Origin extends NamedEntity {
    public Origin() {
    }

    public Origin(UUID uuid, Integer id, String name) {
        super(uuid, id, name);
    }

    @Override
    public String toString() {
        return "Origin { "+
                "Name = " + getName() +
                " ,Id = " + getId() +
                " ,UUID = " + getUuid() + "}";
    }
}
