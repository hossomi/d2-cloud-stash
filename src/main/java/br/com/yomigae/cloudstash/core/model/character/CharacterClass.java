package br.com.yomigae.cloudstash.core.model.character;

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


    @Override
    public String toString() {
        return name;
    }
}
