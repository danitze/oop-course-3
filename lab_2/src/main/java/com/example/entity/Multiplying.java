package com.example.entity;

public enum Multiplying {
    LEAVES("листья"),
    CUTTINGS("черенки"),
    SEEDS("семена");
    private final String enumValue;

    public String getEnumValue() {
        return enumValue;
    }

    Multiplying(String enumValue) {
        this.enumValue = enumValue;
    }

    public static Multiplying getElByValue(String enumValue) {
        for (Multiplying multiplying : values()){
            if (multiplying.enumValue.equals(enumValue)) {
                return multiplying;
            }
        }
        return null;
    }
}
