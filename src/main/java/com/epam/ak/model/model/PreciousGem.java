package com.epam.ak.model.model;

import java.util.UUID;

public class PreciousGem extends Gem {
    private int hardness;

    public PreciousGem(UUID uuid, Integer id, String name, Type type, int transparency, Color color, int facet, Origin miningOrigin, int weight, int price, int hardness) {
        super(uuid, id, name, type, transparency, color, facet, miningOrigin, weight, price);
        this.hardness = hardness;
    }

    public PreciousGem() {
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    @Override
    public String toString() {
        return "PreciousGem{" +
                "hardness=" + hardness +
                ", name=" + getName() +
                ", uuid=" + getUuid() +
                ", id=" + getId() +
                ", transparency=" + getTransparency() +
                ", facet=" + getFacet() +
                ", color=" + getColor() +
                ", weight=" + getWeight() +
                ", price=" + getPrice() +
                ", type=" + getType() +
                ", miningOrigin=" + getName() +
                '}';
    }
}
