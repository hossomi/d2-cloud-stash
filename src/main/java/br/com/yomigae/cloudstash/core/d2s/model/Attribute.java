package br.com.yomigae.cloudstash.core.d2s.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    VITALITY("Vitality"),
    ENERGY("Energy"),
    MIN_DAMAGE("Min. Damage"),
    MAX_DAMAGE("Max. Damage"),
    ATTACK_RATING("Attack Rating"),
    DEFENSE("Defense"),
    LIFE("Life"),
    MAX_LIFE("Max Life"),
    LIFE_REGENERATION("Life Regeneration", "/sec"),
    MANA("Mana"),
    MAX_MANA("Max Mana"),
    MANA_REGENERATION("Mana Regeneration", "/sec"),
    STAMINA("Stamina"),
    MAX_STAMINA("Max Stamina"),
    FIRE_RESISTANCE("Fire Resistance", "%"),
    COLD_RESISTANCE("Cold Resistance", "%"),
    LIGHTNING_RESISTANCE("Lightning Resistance", "%"),
    POISON_RESISTANCE("Poison Resistance", "%"),
    MAGIC_RESISTANCE("Magic Resistance", "%"),
    PHYSICAL_RESISTANCE("Physical Resistance", "%"),
    EXPERIENCE("Experience"),
    EXPERIENCE_REQUIRED("Experience Required"),
    GOLD("Gold", "$"),
    GOLD_BANK("Gold Bank", "$"),
    STAT_POINTS("Stat Points"),
    SKILL_POINTS("Skill Points");

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
