package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

import java.util.List;

@Builder
public record Act1QuestStatus(
        boolean visited,
        boolean introduced,
        QuestStatus denOfEvil,
        QuestStatus sistersBurialGrounds,
        QuestStatus theSearchForCain,
        QuestStatus theForgottenTower,
        QuestStatus toolsOfTheTrade,
        QuestStatus sistersToTheSlaughter)
        implements ActQuestStatus {

    @Override
    public List<QuestStatus> quests() {
        return List.of(
                denOfEvil, sistersBurialGrounds, theSearchForCain,
                theForgottenTower, toolsOfTheTrade, sistersToTheSlaughter);
    }
}
