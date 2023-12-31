package br.com.yomigae.cloudstash.core.d2s.model;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public enum Difficulty {
    NORMAL("Normal"),
    NIGHTMARE("Nightmare"),
    HELL("Hell");

    private final String name;

    public static Collection<Difficulty> all() {
        return List.of(values());
    }

    public static Difficulty fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid Difficulty index (%d-%d): %d",
                    0, values().length - 1, index));
        }
        return values()[index];
    }

    @Override
    public String toString() {
        return name;
    }

}
