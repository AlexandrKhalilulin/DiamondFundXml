package com.epam.ak.model.model;

import java.util.Comparator;
import java.util.UUID;

public abstract class NamedModel extends BaseModel {
    public static final Comparator<NamedModel> Name_Order = new NameComporator();
    private String name;

    public NamedModel(UUID uuid, long id, String name) {
        super(uuid, id);
        this.name = name;
    }

    public NamedModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static class NameComporator implements Comparator<NamedModel> {
        public int compare(NamedModel o1, NamedModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
