package br.com.yomigae.cloudstash.core.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Difficulty {
    NORMAL("Normal"),
    NIGHTMARE("Nightmare"),
    HELL("Hell");

    private final String name;

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
