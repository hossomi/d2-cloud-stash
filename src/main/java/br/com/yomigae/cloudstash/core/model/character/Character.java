package br.com.yomigae.cloudstash.core.model.character;

import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.progression.*;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
        Act currentAct,
        Instant lastPlayed,
        boolean dead,
        EquipmentSet activeEquipmentSet,
        Swap<Dual<Skill>> mouseSkill,
        List<Skill> skillHotkeys,

        Hireling hireling,
        Map<Difficulty, Progression> progression) {

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
                .append("\n\n").append(hireling);
        return string.toString();
    }

    public Progression progression(Difficulty difficulty) {
        return progression.get(difficulty);
    }

    @Builder
    public record Progression(
            Act1Status act1,
            Act2Status act2,
            Act3Status act3,
            Act4Status act4,
            Act5Status act5) {

        List<ActStatus<?, ?>> acts() {
            return List.of(act1, act2, act3, act4, act5);
        }

        ActStatus<?, ?> act(int a) {
            return acts().get(a);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            acts().forEach(act -> string.append("\n\n").append(act));
            return string.toString();
        }
    }
}
