package com.epam.ak.model.model;

import java.util.UUID;

public class SemiPreciousGem extends Gem {
    private Tracery tracery;

    public SemiPreciousGem(UUID uuid, Integer id, String name, Type type, int transparency, Color color, int facet, Origin origin, int weight, int price, Tracery tracery) {
        super(uuid, id, name, type, transparency, color, facet, origin, weight, price);
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

    public static class Tracery extends NamedEntity {
        public Tracery() {
        }

        public Tracery(UUID uuid, Integer id, String name) {
            super(uuid, id, name);
        }
    }
}
