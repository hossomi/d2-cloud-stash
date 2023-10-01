package br.com.yomigae.cloudstash.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
    LIFE("Life"),
    LIFE_REGENERATION("Life Regeneration", "/sec"),
    MANA("Mana"),
    MANA_REGENERATION("Mana Regeneration", "/sec"),
    DEFENSE("Defense"),
    ATTACK_RATING("Attack Rating"),
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    VITALITY("Vitality"),
    ENERGY("Energy"),
    MIN_DAMAGE("Min. Damage"),
    MAX_DAMAGE("Max. Damage"),
    FIRE_RESISTANCE("Fire Resistance", "%"),
    COLD_RESISTANCE("Cold Resistance", "%"),
    LIGHTNING_RESISTANCE("Lightning Resistance", "%"),
    POISON_RESISTANCE("Poison Resistance", "%"),
    MAGIC_RESISTANCE("Magic Resistance", "%"),
    PHYSICAL_RESISTANCE("Physical Resistance", "%"),
    EXPERIENCE("Experience"),
    EXPERIENCE_REQUIRED("Experience Required"),
    GOLD("Gold", "$");

    private final String label;
    private final String unit;

    Attribute(String label) {
        this(label, null);
    }

    public String format(int value) {
        return unit != null
                ? "%d%s".formatted(value, unit)
                : String.valueOf(value);
    }
}
