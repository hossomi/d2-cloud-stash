package br.com.yomigae.cloudstash.core.model.hireling;

import lombok.AllArgsConstructor;

import static java.lang.String.format;

@AllArgsConstructor
public enum HirelingClass {
    ROGUE_SCOUT("Rogue Scout"),
    DESERT_MERCENARY("Desert Mercenary"),
    IRON_WOLF("Iron Wolf"),
    BARBARIAN("Barbarian");

    private final String name;

    public static HirelingClass fromName(String name) {
        return switch (name) {
            case "Rogue Scout" -> ROGUE_SCOUT;
            case "Desert Mercenary" -> DESERT_MERCENARY;
            case "Eastern Sorceror" -> IRON_WOLF;
            case "Barbarian" -> BARBARIAN;
            default -> throw new IllegalArgumentException(format(
                    "Unknown HirelingClass name: %s",
                    name));
        };
    }

    @Override
    public String toString() {
        return name;
    }
}
