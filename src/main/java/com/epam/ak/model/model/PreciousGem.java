package com.epam.ak.model.model;

import java.util.UUID;

public class PreciousGem extends Gem {
    private int hardness;

    public PreciousGem(UUID uuid, long id, String name, Type type, int transparency, Gem.Color color, int facet, Origin miningOrigin, int weight) {
        super(uuid, id, name, type, transparency, color, facet, miningOrigin, weight);
        this.hardness = hardness;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }
}