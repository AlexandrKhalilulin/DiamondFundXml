package com.epam.ak.model.model;

import java.util.Comparator;
import java.util.UUID;

public abstract class NamedEntity extends BaseEntity {
    public static final Comparator<NamedEntity> NameOrder = new NameComporator();
    private String name;

    public NamedEntity(UUID uuid, Integer id, String name) {
        super(uuid, id);
        this.name = name;
    }

    public NamedEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static class NameComporator implements Comparator<NamedEntity> {
        public int compare(NamedEntity o1, NamedEntity o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
