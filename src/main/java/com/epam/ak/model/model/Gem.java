package com.epam.ak.model.model;

import java.util.Comparator;
import java.util.UUID;

public abstract class Gem extends NamedModel {
    public static final Comparator<Gem> Weight_Order = new WeightComparator();
    public static final Comparator<Gem> Transparency_Order = new TransparencyComparator();

    private Type type;
    private int weight;
    private Origin miningOrigin;
    private int facet;
    private Color color;
    private int transparency;

    public Gem(UUID uuid, long id, String name, Type type, int transparency, Color color, int facet, Origin miningOrigin, int weight) {
        super(uuid, id, name);
        this.type = type;
        this.transparency = transparency;
        this.color = color;
        this.facet = facet;
        this.miningOrigin = miningOrigin;
        this.weight = weight;
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Origin getMiningOrigin() {
        return miningOrigin;
    }

    public void setMiningOrigin(Origin miningOrigin) {
        this.miningOrigin = miningOrigin;
    }

    public int getFacet() {
        return facet;
    }

    public void setFacet(int facet) {
        this.facet = facet;
    }

    public static class Type extends NamedModel {
        public Type(UUID uuid, long id, String name) {
            super(uuid, id, name);
        }

        public Type() {
        }
    }

    public static class Color extends NamedModel {

        public Color(UUID uuid, long id, String name) {
            super(uuid, id, name);
        }

        public Color() {
        }
    }

    private static class TransparencyComparator implements Comparator<Gem> {
        public int compare(Gem o1, Gem o2) {
            return o1.getTransparency() - o2.getTransparency();
        }
    }

    private static class WeightComparator implements Comparator<Gem> {
        public int compare(Gem o1, Gem o2) {
            return (int) (o1.getWeight() - o2.getWeight());
        }
    }
}