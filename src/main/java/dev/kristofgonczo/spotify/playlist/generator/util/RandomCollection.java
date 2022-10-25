package dev.kristofgonczo.spotify.playlist.generator.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomCollection<E> {

    @Data
    @AllArgsConstructor
    public static class WeightedItem<E> {
        private Integer weight;
        private E item;
    }

    private final ArrayList<WeightedItem> content = new ArrayList<>();
    private final Random random;
    private Integer total = 0;

    public RandomCollection() {
        this.random = new Random();
    }

    public RandomCollection<E> add(Integer weight, E item) {
        if (weight <= 0) return this;
        total += weight;
        content.add(new WeightedItem(weight, item));
        return this;
    }

    public RandomCollection<E> addAll(WeightedItem<E>[] items) {
        if (Arrays.stream(items).anyMatch(weightedItem -> weightedItem.getWeight() <= 0)) return this;
        Arrays.stream(items).forEach(weightedItem -> total += weightedItem.getWeight());
        content.addAll(Arrays.asList(items));
        return this;
    }

    public E next() {
        Integer targetValue = random.nextInt(total);
        Integer currentValue = 0;
        for (WeightedItem weightedItem : content) {
            currentValue += weightedItem.getWeight();
            if (targetValue <= currentValue) {
                return (E) weightedItem.getItem();
            }
        }
        return null;
    }

    public void remove(E item) {
        WeightedItem itemToRemove =
                content.stream().filter(weightedItem -> ((E) weightedItem.getItem()).equals(item)).findFirst()
                        .orElse(null);
        content.remove(itemToRemove);
        total -= itemToRemove.getWeight();
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public int size() {
        return content.size();
    }
}
