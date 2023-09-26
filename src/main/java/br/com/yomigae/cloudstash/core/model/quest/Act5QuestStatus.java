

package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

import java.util.List;

@Builder
public record Act5QuestStatus(
        boolean visited,
        boolean introduced,
        QuestStatus siegeOnHarrogath,
        QuestStatus rescueOnMountainArreat,
        QuestStatus.PrisonOfIce prisonOfIce,
        QuestStatus betrayalOfHarrogath,
        QuestStatus riteOfPassage,
        QuestStatus eveOfDestruction)
        implements ActQuestStatus {

    @Override
    public List<QuestStatus> quests() {
        return List.of(
                siegeOnHarrogath, rescueOnMountainArreat, prisonOfIce,
                betrayalOfHarrogath, riteOfPassage, eveOfDestruction);
    }
}
