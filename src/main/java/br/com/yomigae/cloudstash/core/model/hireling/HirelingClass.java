package br.com.yomigae.cloudstash.core.model.hireling;

public enum HirelingClass {
    ROGUE_SCOUT,
    DESERT_MERCENARY,
    IRON_WOLF,
    BARBARIAN;

    private static final HirelingClass[] VALUES = values();

    public static HirelingClass fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid HirelingClass index (%d-%d): %d",
                    0, VALUES.length, index));
        }
        return VALUES[index];
    }

    public static HirelingClass fromName(String name) {
        return switch (name) {
            case "Rogue Scout" -> ROGUE_SCOUT;
            case "Desert Mercenary" -> DESERT_MERCENARY;
            case "Eastern Sorceror" -> IRON_WOLF;
            case "Barbarian" -> BARBARIAN;
            default -> throw new IllegalArgumentException(String.format(
                    "Unknown HirelingClass name: %s",
                    name));
        };
    }
}
