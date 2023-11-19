package br.com.yomigae.cloudstash.core.model.character;

import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.acts.ActStatus;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import lombok.Getter;
import lombok.Singular;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static br.com.yomigae.cloudstash.core.util.StringUtils.Divider.SINGLE;
import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.divider;
import static java.util.Comparator.comparing;

@lombok.Builder
public record Character(
        String name,
        CharacterClass clazz,
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

        @Singular("acts")
        Map<Difficulty, Acts> acts,

        @Singular
        Map<Attribute, Integer> attributes,
        @Singular
        Map<Skill, Integer> skills) {

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Character character = (Character) o;
        return ladder == character.ladder
                && expansion == character.expansion
                && hardcore == character.hardcore
                && level == character.level
                && dead == character.dead
                && name.equals(character.name)
                && clazz == character.clazz
                && currentAct == character.currentAct
                && activeEquipmentSet == character.activeEquipmentSet
                && mouseSkill.equals(character.mouseSkill)
                && skillHotkeys.equals(character.skillHotkeys)
                && Objects.equals(hireling, character.hireling)
                && acts.equals(character.acts)
                && attributes.equals(character.attributes)
                && skills.equals(character.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clazz, ladder, expansion, hardcore, level, currentAct,
                dead, activeEquipmentSet, mouseSkill, skillHotkeys, hireling, acts,
                attributes, skills);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(name)
                .append(" (level ").append(level).append(" ").append(clazz)
                .append(")\n").append(checkbox("Expansion", expansion))
                .append(" ").append(checkbox("Ladder", ladder))
                .append(" ").append(checkbox("Dead", dead))

                .append("\n").append("Current act: ").append(currentAct)
                .append("\n").append("Last played: ").append(lastPlayed);

        acts.forEach((difficulty, quests) -> string
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
        string
                .append("\n\nSkills")
                .append("\n").append(divider(SINGLE));
        skills.keySet()
                .stream()
                .sorted(comparing(Skill::id))
                .forEach((s) -> string
                        .append("\n").append(s.name()).append(": ")
                        .append(skills.get(s)));

        if (hireling != null) {
            string.append("\n\n").append(hireling);
        }
        return string.toString();
    }

    public static class Builder {
        public CharacterClass clazz() {
            return clazz;
        }

        public Builder clazz(CharacterClass type) {
            this.clazz = type;
            return this;
        }
    }

    public record Acts(
            ActStatus.Act1 act1,
            ActStatus.Act2 act2,
            ActStatus.Act3 act3,
            ActStatus.Act4 act4,
            ActStatus.Act5 act5) {

        public static Builder builder(Difficulty difficulty) {
            return new Builder(difficulty);
        }

        List<ActStatus<?, ?>> acts() {
            return List.of(act1, act2, act3, act4, act5);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            acts().forEach(act -> string.append("\n\n").append(act));
            return string.toString();
        }

        @Getter
        public static final class Builder {
            private final ActStatus.Act1.Builder act1;
            private final ActStatus.Act2.Builder act2;
            private final ActStatus.Act3.Builder act3;
            private final ActStatus.Act4.Builder act4;
            private final ActStatus.Act5.Builder act5;

            public Builder(Difficulty difficulty) {
                this.act1 = ActStatus.Act1.builder().difficulty(difficulty);
                this.act2 = ActStatus.Act2.builder().difficulty(difficulty);
                this.act3 = ActStatus.Act3.builder().difficulty(difficulty);
                this.act4 = ActStatus.Act4.builder().difficulty(difficulty);
                this.act5 = ActStatus.Act5.builder().difficulty(difficulty);
            }

            public Acts build() {
                return new Acts(
                        act1.build(),
                        act2.build(),
                        act3.build(),
                        act4.build(),
                        act5.build());
            }
        }
    }
}
