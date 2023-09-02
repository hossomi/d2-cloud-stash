package br.com.yomigae.cloudstash.core.model;

public enum Difficulty {
    NORMAL, NIGHTMARE, HELL;

    private static final Difficulty[] VALUES = values();

    public static Difficulty fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid Difficulty index (%d-%d): %d",
                    0, VALUES.length - 1, index));
        }
        return VALUES[index];
    }
}
