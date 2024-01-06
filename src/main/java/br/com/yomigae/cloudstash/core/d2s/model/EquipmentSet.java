package br.com.yomigae.cloudstash.core.d2s.model;

public enum EquipmentSet {
    MAIN, SWAP;

    private static final EquipmentSet[] VALUES = values();

    public static EquipmentSet fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid EquipmentSet index (%d-%d): %d",
                    0, VALUES.length, index));
        }
        return VALUES[index];
    }
}
