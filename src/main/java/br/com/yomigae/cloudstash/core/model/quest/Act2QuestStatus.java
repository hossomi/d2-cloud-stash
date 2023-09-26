
package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

import java.util.List;

@Builder
public record Act2QuestStatus(
        boolean visited,
        boolean introduced,
        QuestStatus radamentsLair,
        QuestStatus theHoradricStaff,
        QuestStatus taintedSun,
        QuestStatus arcaneSanctuary,
        QuestStatus theSummoner,
        QuestStatus theSevenTombs)
        implements ActQuestStatus {

    @Override
    public List<QuestStatus> quests() {
        return List.of(
                radamentsLair, theHoradricStaff, taintedSun,
                arcaneSanctuary, theSummoner, theSevenTombs);
    }
}
