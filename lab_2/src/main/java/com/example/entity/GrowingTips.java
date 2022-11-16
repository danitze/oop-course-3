package com.example.entity;

import java.util.Objects;

public class GrowingTips {
    private Temperature temperature;
    private Lighting lightRequiring;
    private Watering watering;

    public Temperature getTemperature() {
        return temperature;
    }

    public Lighting getLighting() {
        return lightRequiring;
    }

    public Watering getWatering() {
        return watering;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void setLightRequiring(Lighting lightRequiring) {
        this.lightRequiring = lightRequiring;
    }

    public void setWatering(Watering watering) {
        this.watering = watering;
    }

    public GrowingTips(Temperature temperature, Lighting lighting, Watering watering) {
        this.temperature = temperature;
        this.lightRequiring = lighting;
        this.watering = watering;
    }

    public GrowingTips() {
        this.temperature = null;
        this.lightRequiring = null;
        this.watering = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrowingTips that = (GrowingTips) o;
        return temperature.equals(that.temperature)
                && lightRequiring == that.lightRequiring
                && Objects.equals(watering, that.watering);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, lightRequiring, watering);
    }
}
