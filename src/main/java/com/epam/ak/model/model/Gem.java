package com.epam.ak.model.model;

import java.util.Comparator;
import java.util.UUID;

public abstract class Gem extends NamedModel {
    public static final Comparator<Gem> Weight_Order = new WeightComparator();
    public static final Comparator<Gem> Transparency_Order = new TransparencyComparator();
    public static final Comparator<Gem> Price_Order = new PriceComparator();

    private Type type;
    private int weight;
    private Origin miningOrigin;
    private int facet;
    private Color color;
    private int transparency;
    private int price;

    public Gem(UUID uuid, Integer id, String name, Type type, int transparency, Color color, int facet, Origin miningOrigin, int weight, int price) {
        super(uuid, id, name);
        this.type = type;
        this.transparency = transparency;
        this.color = color;
        this.facet = facet;
        this.miningOrigin = miningOrigin;
        this.weight = weight;
        this.price = price;
    }

    protected Gem() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
        public Type(UUID uuid, Integer id, String name) {
            super(uuid, id, name);
        }

        public Type() {
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

    private static class PriceComparator implements Comparator<Gem> {
        public int compare(Gem o1, Gem o2) {
            return o1.getPrice() - o2.getPrice();
        }
    }
}