package com.epam.ak.model.model;

import java.util.UUID;

public class Origin extends NamedModel {
    public Origin() {
    }

    public Origin(UUID uuid, long id, String name) {
        super(uuid, id, name);
    }
}
