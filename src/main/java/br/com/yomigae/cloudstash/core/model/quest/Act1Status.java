package br.com.yomigae.cloudstash.core.model.quest;

import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Act1Status extends ActStatus {
    QuestStatus denOfEvil;
    QuestStatus sistersBurialGrounds;
    QuestStatus theSearchForCain;
    QuestStatus theForgottenTower;
    QuestStatus toolsOfTheTrade;
    QuestStatus sistersToTheSlaughter;

    @Builder
    public Act1Status(
            Difficulty difficulty, boolean visited, boolean introduced,
            QuestStatus denOfEvil, QuestStatus sistersBurialGrounds, QuestStatus theSearchForCain,
            QuestStatus theForgottenTower, QuestStatus toolsOfTheTrade, QuestStatus sistersToTheSlaughter) {
        super(Act.from(difficulty, 0), visited, introduced, List.of(
                denOfEvil, sistersBurialGrounds, theSearchForCain,
                theForgottenTower, toolsOfTheTrade, sistersToTheSlaughter));
        this.denOfEvil = denOfEvil;
        this.sistersBurialGrounds = sistersBurialGrounds;
        this.theSearchForCain = theSearchForCain;
        this.theForgottenTower = theForgottenTower;
        this.toolsOfTheTrade = toolsOfTheTrade;
        this.sistersToTheSlaughter = sistersToTheSlaughter;
    }
}
