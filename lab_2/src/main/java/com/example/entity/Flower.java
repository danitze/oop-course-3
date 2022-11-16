package com.example.entity;

import java.util.Objects;

public class Flower {
    private final String name;
    private final Soil soil;
    private final String origin;
    private final VisualParameters visualParameters;
    private final GrowingTips growingTips;
    private final Multiplying multiplying;

    public String getName() {
        return name;
    }

    public Soil getSoil() {
        return soil;
    }

    public String getOrigin() {
        return origin;
    }

    public VisualParameters getVisualParameters() {
        return visualParameters;
    }

    public GrowingTips getGrowingTips() {
        return growingTips;
    }

    public Multiplying getMultiplying() {
        return multiplying;
    }

    @Override
    public String toString() {
        assert visualParameters != null;
        assert soil != null;
        String result = "Name: " + name + "\n" +
                "Soil: " + soil.getEnumValue() + "\n" +
                "Origin: " + origin + "\n" +
                "VisualParameters: " + visualParameters.getStemColor() + ", " + visualParameters.getLeafColor();
        if(visualParameters.getAveLenFlower() != null) {
            result += ", " + visualParameters.getAveLenFlower().getValue() +
                    " " + visualParameters.getAveLenFlower().getMeasure().getEnumValue();
        }
        result += "\n";
        assert growingTips != null;
        result += "GrowingTips: " + growingTips.getTemperature().getValue() +
                " " + growingTips.getTemperature().getMeasure() +
                ", " + growingTips.getLighting().enumValue;
        if(growingTips.getWatering() != null) {
            result += ", " + growingTips.getWatering().getValue() + " " +
                    growingTips.getWatering().getMeasure().getEnumValue();
        }
        assert multiplying != null;
        result += "\n" +
                "Multiplying: " + multiplying.getEnumValue();
        return result;
    }

    public Flower(String name, Soil soil, String origin, VisualParameters visualParameters,
                  GrowingTips growingTips, Multiplying multiplying) {
        this.name = name;
        this.soil = soil;
        this.origin = origin;
        this.visualParameters = visualParameters;
        this.growingTips = growingTips;
        this.multiplying = multiplying;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return name.equals(flower.name)
                && soil == flower.soil
                && origin.equals(flower.origin)
                && visualParameters.equals(flower.visualParameters)
                && growingTips.equals(flower.growingTips)
                && multiplying == flower.multiplying;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, soil, origin, visualParameters, growingTips, multiplying);
    }
}
