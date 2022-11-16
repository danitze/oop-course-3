package com.example.entity;

public enum Lighting {
    YES("yes"),
    NO("no");
    public final String enumValue;

    Lighting(String enumValue) {
        this.enumValue = enumValue;
    }

    public static Lighting getElByValue(String enumValue) {
        for (Lighting lighting : values()) {
            if (lighting.enumValue.equals(enumValue)) {
                return lighting;
            }
        }
        return null;
    }

}
