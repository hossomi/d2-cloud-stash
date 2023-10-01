

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
public class Act3Status extends ActStatus {

    QuestStatus theGoldenBird;
    QuestStatus bladeOfTheOldReligion;
    QuestStatus khalimsWill;
    QuestStatus lamEsensTome;
    QuestStatus theBlackenedTemple;
    QuestStatus theGuardian;

    @Builder
    public Act3Status(
            Difficulty difficulty, boolean visited, boolean introduced,
            QuestStatus theGoldenBird, QuestStatus bladeOfTheOldReligion, QuestStatus khalimsWill,
            QuestStatus lamEsensTome, QuestStatus theBlackenedTemple, QuestStatus theGuardian) {
        super(Act.from(difficulty, 2), visited, introduced, List.of(
                theGoldenBird, bladeOfTheOldReligion, khalimsWill,
                lamEsensTome, theBlackenedTemple, theGuardian));
        this.theGoldenBird = theGoldenBird;
        this.bladeOfTheOldReligion = bladeOfTheOldReligion;
        this.khalimsWill = khalimsWill;
        this.lamEsensTome = lamEsensTome;
        this.theBlackenedTemple = theBlackenedTemple;
        this.theGuardian = theGuardian;
    }
}
