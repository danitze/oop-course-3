package com.example.entity;

public class FlowerBuilder {
    private String name;
    private Soil soil;
    private String origin;
    private VisualParameters visualParameters;
    private GrowingTips growingTips;
    private Multiplying multiplying;

    public FlowerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FlowerBuilder soil(Soil soil) {
        this.soil = soil;
        return this;
    }

    public FlowerBuilder origin(String origin) {
        this.origin = origin;
        return this;
    }

    public FlowerBuilder visualParameters(VisualParameters visualParameters) {
        this.visualParameters = visualParameters;
        return this;
    }

    public FlowerBuilder growingTips(GrowingTips growingTips) {
        this.growingTips = growingTips;
        return this;
    }

    public FlowerBuilder multiplying(Multiplying multiplying) {
        this.multiplying = multiplying;
        return this;
    }

    public Flower build() {
        return new Flower(name, soil, origin, visualParameters, growingTips, multiplying);
    }
}
