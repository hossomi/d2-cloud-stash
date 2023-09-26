

package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

import java.util.List;

@Builder
public record Act4QuestStatus(
        boolean visited,
        boolean introduced,
        QuestStatus theFallenAngel,
        QuestStatus hellsForge,
        QuestStatus terrorsEnd)
        implements ActQuestStatus {

    @Override
    public List<QuestStatus> quests() {
        return List.of(theFallenAngel, hellsForge, terrorsEnd);
    }
}
