package com.example.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Flowers {
    private List<Flower> flowerList;

    public Flowers() {
        flowerList = new LinkedList<>();
    }

    public void add(Flower flower) {
        flowerList.add(flower);
    }

    public List<Flower> getFlowerList() {
        return flowerList;
    }

    public void setFlowerList(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }

    @Override
    public String toString() {
        return "Flowers{" +
                "flowerList=" + flowerList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flowers flowers = (Flowers) o;
        return flowerList.equals(flowers.flowerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowerList);
    }
}
