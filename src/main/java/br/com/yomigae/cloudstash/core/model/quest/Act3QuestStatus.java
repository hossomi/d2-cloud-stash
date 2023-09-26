

package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

import java.util.List;

@Builder
public record Act3QuestStatus(
        boolean visited,
        boolean introduced,
        QuestStatus theGoldenBird,
        QuestStatus bladeOfTheOldReligion,
        QuestStatus khalimsWill,
        QuestStatus lamEsensTome,
        QuestStatus theBlackenedTemple,
        QuestStatus theGuardian)
        implements ActQuestStatus {

    @Override
    public List<QuestStatus> quests() {
        return List.of(
                theGoldenBird, bladeOfTheOldReligion, khalimsWill,
                lamEsensTome, theBlackenedTemple, theGuardian);
    }
}
