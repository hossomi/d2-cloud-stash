package br.com.yomigae.cloudstash.core.model;

public enum CharacterClass {
    AMAZON,
    SORCERESS,
    NECROMANCER,
    PALADIN,
    BARBARIAN,
    DRUID,
    ASSASSIN;

    private static final CharacterClass[] VALUES = values();

    public static CharacterClass fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid CharacterClass index (%d-%d): %d",
                    0, VALUES.length, index));
        }
        return VALUES[index];
    }
}
