package br.com.yomigae.cloudstash.core.model.character;

public enum CharacterClass {
    AMAZON,
    SORCERESS,
    NECROMANCER,
    PALADIN,
    BARBARIAN,
    DRUID,
    ASSASSIN;

    public static CharacterClass fromIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid CharacterClass index (%d-%d): %d",
                    0, values().length, index));
        }
        return values()[index];
    }
}
