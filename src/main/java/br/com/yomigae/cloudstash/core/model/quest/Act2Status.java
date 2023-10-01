
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
public class Act2Status extends ActStatus {

    QuestStatus radamentsLair;
    QuestStatus theHoradricStaff;
    QuestStatus taintedSun;
    QuestStatus arcaneSanctuary;
    QuestStatus theSummoner;
    QuestStatus theSevenTombs;

    @Builder
    public Act2Status(
            Difficulty difficulty, boolean visited, boolean introduced,
            QuestStatus radamentsLair, QuestStatus theHoradricStaff, QuestStatus taintedSun,
            QuestStatus arcaneSanctuary, QuestStatus theSummoner, QuestStatus theSevenTombs) {
        super(Act.from(difficulty, 1), visited, introduced, List.of(
                radamentsLair, theHoradricStaff, taintedSun,
                arcaneSanctuary, theSummoner, theSevenTombs));
        this.radamentsLair = radamentsLair;
        this.theHoradricStaff = theHoradricStaff;
        this.taintedSun = taintedSun;
        this.arcaneSanctuary = arcaneSanctuary;
        this.theSummoner = theSummoner;
        this.theSevenTombs = theSevenTombs;
    }
}
