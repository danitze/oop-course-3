package com.example.entity;

public enum Soil {
    PODZOLIC("подзолистая"),
    GROUND("грунтовая"),
    SOD_PODZOLIC("дерново-подзолистая");
    private final String enumValue;

    public String getEnumValue() {
        return enumValue;
    }

    Soil(String enumValue) {
        this.enumValue = enumValue;
    }

    public static Soil getElByValue(String enumValue) {
        for (Soil soil : values()){
            if (soil.enumValue.equals(enumValue)) {
                return soil;
            }
        }
        return null;
    }

}
