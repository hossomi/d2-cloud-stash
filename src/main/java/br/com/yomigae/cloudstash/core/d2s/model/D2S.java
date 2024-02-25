package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.d2s.model.acts.ActStatus;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.d2s.model.hireling.Hireling;
import lombok.Getter;
import lombok.Singular;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@lombok.Builder
public record D2S(
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
        Map<Skill, Integer> skills,
        @Singular
        Collection<Object> items) {

    public static class Builder {

        public CharacterClass clazz() {
            return clazz;
        }

        public boolean expansion() {
            return expansion;
        }

        public Builder clazz(CharacterClass type) {
            this.clazz = type;
            return this;
        }

        public Builder expansion(boolean expansion) {
            this.expansion = expansion;
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

        public List<ActStatus<?, ?>> all() {
            return List.of(act1, act2, act3, act4, act5);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            all().forEach(act -> string.append("\n\n").append(act));
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
