package com.epam.ak.model.model;

import java.util.List;
import java.util.UUID;

public class Pavilion extends NamedEntity {
    private List<Gem> gems;

    public Pavilion() {
    }

    public Pavilion(UUID uuid, Integer id, String name, List<Gem> gems) {
        super(uuid, id, name);
        this.gems = gems;
    }

    public List<Gem> getGems() {
        return gems;
    }

    public void setGems(List<Gem> gems) {
        this.gems = gems;
    }
}
