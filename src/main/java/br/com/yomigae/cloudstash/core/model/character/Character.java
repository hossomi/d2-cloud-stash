package br.com.yomigae.cloudstash.core.model.character;

import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.progression.*;
import lombok.Builder;
import lombok.Singular;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static br.com.yomigae.cloudstash.core.util.StringUtils.Divider.SINGLE;
import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.divider;

@Builder
public record Character(
        String name,
        CharacterClass klass,
        boolean ladder,
        boolean expansion,
        boolean hardcore,

        int level,
        Instant lastPlayed,
        Act currentAct,
        boolean dead,
        EquipmentSet activeEquipmentSet,
        Swap<Dual<Skill>> mouseSkill,
        List<Skill> skillHotkeys,

        Hireling hireling,
        DifficultyMap<Progression> progression,

        @Singular
        Map<Attribute, Integer> attributes) {
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Character character = (Character) o;
        return ladder == character.ladder
                && expansion == character.expansion
                && hardcore == character.hardcore
                && level == character.level
                && dead == character.dead
                && name.equals(character.name)
                && klass == character.klass
                && currentAct == character.currentAct
                && activeEquipmentSet == character.activeEquipmentSet
                && mouseSkill.equals(character.mouseSkill)
                && skillHotkeys.equals(character.skillHotkeys)
                && Objects.equals(hireling, character.hireling)
                && progression.equals(character.progression)
                && attributes.equals(character.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, klass, ladder, expansion, hardcore, level, currentAct, dead, activeEquipmentSet, mouseSkill, skillHotkeys, hireling, progression, attributes);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(name)
                .append(" (level ").append(level).append(" ").append(klass)
                .append(")\n").append(checkbox("Expansion", expansion))
                .append(" ").append(checkbox("Ladder", ladder))
                .append(" ").append(checkbox("Dead", dead))

                .append("\n").append("Current act: ").append(currentAct)
                .append("\n").append("Last played: ").append(lastPlayed);

        progression.forEach((difficulty, quests) -> string
                .append("\n\n").append(difficulty)
                .append("\n").append(divider(SINGLE))
                .append(quests));

        string
                .append("\n\nAttributes")
                .append("\n").append(divider(SINGLE));
        attributes.keySet()
                .stream()
                .sorted()
                .forEach((a) -> string
                        .append("\n").append(a.label()).append(": ")
                        .append(a.format(attributes.get(a))));

        if (hireling != null) {
            string.append("\n\n").append(hireling);
        }
        return string.toString();
    }

    public Progression progression(Difficulty difficulty) {
        return progression.get(difficulty);
    }

    @lombok.Builder
    public record Progression(
            Act1Progression act1,
            Act2Progression act2,
            Act3Progression act3,
            Act4Progression act4,
            Act5Progression act5) {

        List<ActProgression<?, ?>> acts() {
            return List.of(act1, act2, act3, act4, act5);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            acts().forEach(act -> string.append("\n\n").append(act));
            return string.toString();
        }

        public static class Builder {
            public Builder set(ActProgression<?, ?> status) {
                return switch (status.act().number()) {
                    case 0 -> act1((Act1Progression) status);
                    case 1 -> act2((Act2Progression) status);
                    case 2 -> act3((Act3Progression) status);
                    case 3 -> act4((Act4Progression) status);
                    case 4 -> act5((Act5Progression) status);
                    default -> throw new IllegalArgumentException("Invalid act: " + status.act());
                };
            }
        }
    }
}
