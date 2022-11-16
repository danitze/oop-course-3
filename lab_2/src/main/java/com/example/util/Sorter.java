package com.example.util;

import com.example.entity.Flower;
import com.example.entity.Flowers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface Sorter {

     static Flowers sortByName(Flowers flowers) {
        List<Flower> flowerList = flowers.getFlowerList().stream()
                .sorted(Comparator.comparing(Flower::getName))
                .collect(Collectors.toList());
        flowers.setFlowerList(flowerList);
        return flowers;
    }

    static Flowers sortByTemperature(Flowers flowers) {
        List<Flower> flowerList = flowers.getFlowerList().stream()
                .sorted(Comparator.comparingInt(f -> f.getGrowingTips().getTemperature().getValue()))
                .collect(Collectors.toList());
        flowers.setFlowerList(flowerList);
        return flowers;
    }

    static Flowers sortByOrigin(Flowers flowers) {
        List<Flower> flowerList = flowers.getFlowerList().stream()
                .sorted(Comparator.comparing(Flower::getOrigin))
                .collect(Collectors.toList());
        flowers.setFlowerList(flowerList);
        return flowers;
    }

}
