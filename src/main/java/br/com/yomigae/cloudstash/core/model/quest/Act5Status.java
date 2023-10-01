

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
public class Act5Status extends ActStatus {

    QuestStatus siegeOnHarrogath;
    QuestStatus rescueOnMountainArreat;
    QuestStatus.PrisonOfIce prisonOfIce;
    QuestStatus betrayalOfHarrogath;
    QuestStatus riteOfPassage;
    QuestStatus eveOfDestruction;

    @Builder
    public Act5Status(
            Difficulty difficulty, boolean visited, boolean introduced,
            QuestStatus siegeOnHarrogath, QuestStatus rescueOnMountainArreat, QuestStatus.PrisonOfIce prisonOfIce,
            QuestStatus betrayalOfHarrogath, QuestStatus riteOfPassage, QuestStatus eveOfDestruction) {
        super(Act.from(difficulty, 4), visited, introduced, List.of(
                siegeOnHarrogath, rescueOnMountainArreat, prisonOfIce,
                betrayalOfHarrogath, riteOfPassage, eveOfDestruction));
        this.siegeOnHarrogath = siegeOnHarrogath;
        this.rescueOnMountainArreat = rescueOnMountainArreat;
        this.prisonOfIce = prisonOfIce;
        this.betrayalOfHarrogath = betrayalOfHarrogath;
        this.riteOfPassage = riteOfPassage;
        this.eveOfDestruction = eveOfDestruction;
    }
}
