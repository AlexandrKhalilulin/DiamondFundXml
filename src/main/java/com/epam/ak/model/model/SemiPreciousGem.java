package com.epam.ak.model.model;

import java.util.UUID;

public class SemiPreciousGem extends Gem {
    private Tracery tracery;

    public SemiPreciousGem(UUID uuid, long id, String name, Type type, int transparency, Color color, int facet, Origin miningOrigin, int weight, int price, Tracery tracery) {
        super(uuid, id, name, type, transparency, color, facet, miningOrigin, weight, price);
        this.tracery = tracery;
    }

    public SemiPreciousGem() {
    }

    public Tracery getTracery() {
        return tracery;
    }

    public void setTracery(Tracery tracery) {
        this.tracery = tracery;
    }

    public static class Tracery extends NamedModel {
        public Tracery() {
        }

        public Tracery(UUID uuid, long id, String name) {
            super(uuid, id, name);
        }
    }
}
