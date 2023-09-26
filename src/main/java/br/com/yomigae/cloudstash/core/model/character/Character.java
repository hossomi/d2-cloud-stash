package br.com.yomigae.cloudstash.core.model.character;

import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.quest.*;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
        Map<Difficulty, Quests> quests) {

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(name)
                .append(" (Level ").append(level)
                .append(" ").append(klass)
                .append(")\n");
        appendBoolean("Expansion", expansion, string).append(" ");
        appendBoolean("Ladder", ladder, string).append(" ");
        appendBoolean("Dead", dead, string).append("\n");
        string
                .append("Current act: ").append(currentAct).append("\n")
                .append("Last played: ").append(lastPlayed).append("\n");

        string.append("\nQUESTS\n====================\n");
        quests.forEach((d, qs) -> string.append(d)
                .append("\n--------------------\n")
                .append(qs));
        return string.toString();
    }

    private static StringBuilder appendBoolean(String label, boolean value, StringBuilder string) {
        return string
                .append("[")
                .append(value ? "x" : " ")
                .append("] ")
                .append(label);
    }

    @Builder
    public record Quests(
            Act1QuestStatus act1,
            Act2QuestStatus act2,
            Act3QuestStatus act3,
            Act4QuestStatus act4,
            Act5QuestStatus act5) {

        List<ActQuestStatus> acts() {
            return List.of(act1, act2, act3, act4, act5);
        }

        ActQuestStatus act(int a) {
            return acts().get(a);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            for (int a = 0; a < acts().size(); a++) {
                ActQuestStatus act = act(a);
                string.append("[")
                        .append(act.visited() ? "v" : " ")
                        .append(act.introduced() ? "i" : " ")
                        .append("] Act ")
                        .append(a + 1)
                        .append("\n");
                for (int q = 0; q < act.quests().size(); q++) {
                    QuestStatus quest = act.quest(q);
                    string.append("- [")
                            .append(quest.completed() ? "x" : " ")
                            .append("] ")
                            .append(Quest.forAct(a, q).label())
                            .append("\n");
                }
            }
            return string.toString();
        }
    }
}
