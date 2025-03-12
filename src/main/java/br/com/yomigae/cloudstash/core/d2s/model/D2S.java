package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.d2s.model.Tuples.Alternate;
import br.com.yomigae.cloudstash.core.d2s.model.Tuples.Dual;
import br.com.yomigae.cloudstash.core.d2s.model.progress.Progress.*;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.d2s.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.util.Flags;
import lombok.Builder;
import lombok.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
public final class D2S {
    private String name;
    private CharacterClass type;
    private int level;

    private boolean ladder;
    private boolean expansion;
    private boolean hardcore;
    private boolean dead;

    private final Instant lastPlayed;
    private Act currentAct;

    private Alternate<Dual<Skill>> mouseSkill;
    private List<SkillHotkey> skillHotkeys;
    private Hireling hireling;

    @Singular("progress")
    private final Map<Difficulty, Progress> progress;
    @Singular
    private final Map<Attribute, Integer> attributes;
    @Singular
    private final Map<Skill, Integer> skills;
    @Singular
    private final Collection<Object> items;

    @Data
    @AllArgsConstructor
    public static final class SkillHotkey {
        private boolean left;
        private Skill skill;
    }

    @Data
    @AllArgsConstructor
    public static final class Progress {
        private final Act1 act1;
        private final Act2 act2;
        private final Act3 act3;
        private final Act4 act4;
        private final Act5 act5;

        public static Builder builder(Difficulty difficulty) {
            return new Builder(difficulty);
        }

        public List<br.com.yomigae.cloudstash.core.d2s.model.progress.Progress<?, ?>> all() {
            return List.of(act1, act2, act3, act4, act5);
        }

        @Getter
        public static final class Builder {

            private final Act1.Builder act1;
            private final Act2.Builder act2;
            private final Act3.Builder act3;
            private final Act4.Builder act4;
            private final Act5.Builder act5;

            public Builder(Difficulty difficulty) {
                this.act1 = Act1.builder().difficulty(difficulty);
                this.act2 = Act2.builder().difficulty(difficulty);
                this.act3 = Act3.builder().difficulty(difficulty);
                this.act4 = Act4.builder().difficulty(difficulty);
                this.act5 = Act5.builder().difficulty(difficulty);
            }

            public Progress build() {
                return new Progress(
                        act1.build(),
                        act2.build(),
                        act3.build(),
                        act4.build(),
                        act5.build());
            }

        }
    }

    public static class Builder {

        public CharacterClass type() {
            return type;
        }

        public boolean expansion() {
            return expansion;
        }

        public Builder type(CharacterClass type) {
            this.type = type;
            return this;
        }

        public Builder expansion(boolean expansion) {
            this.expansion = expansion;
            return this;
        }
    }

}












