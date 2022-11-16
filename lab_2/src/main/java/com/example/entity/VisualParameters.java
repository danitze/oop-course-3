package com.example.entity;

import java.util.Objects;

public class VisualParameters {
    private String stemColor;
    private String leafColor;
    private AveLenFlower aveLenFlower;

    public String getStemColor() {
        return stemColor;
    }

    public String getLeafColor() {
        return leafColor;
    }

    public AveLenFlower getAveLenFlower() {
        return aveLenFlower;
    }

    public void setStemColor(String stemColor) {
        this.stemColor = stemColor;
    }

    public void setLeafColor(String leafColor) {
        this.leafColor = leafColor;
    }

    public void setAveLenFlower(AveLenFlower aveLenFlower) {
        this.aveLenFlower = aveLenFlower;
    }

    public VisualParameters(String stemColor, String leafColor, AveLenFlower aveLenFlower) {
        this.stemColor = stemColor;
        this.leafColor = leafColor;
        this.aveLenFlower = aveLenFlower;
    }

    public VisualParameters() {
        this.stemColor = null;
        this.leafColor = null;
        this.aveLenFlower = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualParameters that = (VisualParameters) o;
        return stemColor.equals(that.stemColor)
                && leafColor.equals(that.leafColor)
                && Objects.equals(aveLenFlower, that.aveLenFlower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stemColor, leafColor, aveLenFlower);
    }
}
