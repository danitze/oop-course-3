package com.example.entity;

import java.util.Objects;

public class Temperature {
    private Measure measure;
    private int value;

    public Measure getMeasure() {
        return measure;
    }

    public int getValue() {
        return value;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Temperature(Measure measure, int value) {
        this.measure = measure;
        this.value = value;
    }

    public Temperature() {
        this.measure = null;
        this.value = 0;
    }

    public enum Measure {
        CELCIUS("celcius");
        private final String enumValue;

        public String getEnumValue() {
            return enumValue;
        }

        Measure(String enumValue) {
            this.enumValue = enumValue;
        }

        public static Measure getElByValue(String enumValue) {
            for (Measure measure : values()) {
                if (measure.enumValue.equals(enumValue)) {
                    return measure;
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temperature that = (Temperature) o;
        return value == that.value && measure == that.measure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measure, value);
    }
}
