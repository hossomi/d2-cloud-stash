package br.com.yomigae.cloudstash.core.d2s.model.character;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CharacterClass {
    AMAZON("Amazon"),
    SORCERESS("Sorceress"),
    NECROMANCER("Necromancer"),
    PALADIN("Paladin"),
    BARBARIAN("Barbarian"),
    DRUID("Druid"),
    ASSASSIN("Assassin");

    private final String name;

    public static CharacterClass fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid CharacterClass index (%d-%d): %d",
                    0, values().length, index));
        }
        return values()[index];
    }
    public static CharacterClass from(String label) {
        return switch(label) {
            case "ama" -> AMAZON;
            case "sor" -> SORCERESS;
            case "nec" -> NECROMANCER;
            case "pal" -> PALADIN;
            case "bar" -> BARBARIAN;
            case "dru" -> DRUID;
            case "ass" -> ASSASSIN;
            default -> throw new IllegalArgumentException(String.format(
                    "Invalid CharacterClass label: %s",
                    label));
        };
    }

    @Override
    public String toString() {
        return name;
    }
}
