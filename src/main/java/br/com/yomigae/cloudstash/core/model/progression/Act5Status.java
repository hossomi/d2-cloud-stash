

package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import br.com.yomigae.cloudstash.core.model.progression.QuestStatus.PrisonOfIce;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Act5Status extends ActStatus<Act5Status.Quests, Act5Status.Waypoints> {

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActStatus.Quests {
        QuestStatus siegeOnHarrogath;
        QuestStatus rescueOnMountainArreat;
        PrisonOfIce prisonOfIce;
        QuestStatus betrayalOfHarrogath;
        QuestStatus riteOfPassage;
        QuestStatus eveOfDestruction;

        public Quests(
                QuestStatus siegeOnHarrogath, QuestStatus rescueOnMountainArreat, PrisonOfIce prisonOfIce,
                QuestStatus betrayalOfHarrogath, QuestStatus riteOfPassage, QuestStatus eveOfDestruction) {
            super(List.of(
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

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActStatus.Waypoints {
        WaypointStatus harrogath;
        WaypointStatus frigidHighlands;
        WaypointStatus arreatPlateau;
        WaypointStatus crystallinePassage;
        WaypointStatus hallsOfPain;
        WaypointStatus glacialTrail;
        WaypointStatus frozenTundra;
        WaypointStatus theAncientsWay;
        WaypointStatus worldstoneKeep;

        public Waypoints(
                WaypointStatus harrogath, WaypointStatus frigidHighlands, WaypointStatus arreatPlateau,
                WaypointStatus crystallinePassage, WaypointStatus hallsOfPain, WaypointStatus glacialTrail,
                WaypointStatus frozenTundra, WaypointStatus theAncientsWay, WaypointStatus worldstoneKeep) {
            super(List.of(
                    harrogath, frigidHighlands, arreatPlateau,
                    crystallinePassage, hallsOfPain, glacialTrail,
                    frozenTundra, theAncientsWay, worldstoneKeep));
            this.harrogath = harrogath;
            this.frigidHighlands = frigidHighlands;
            this.arreatPlateau = arreatPlateau;
            this.crystallinePassage = crystallinePassage;
            this.hallsOfPain = hallsOfPain;
            this.glacialTrail = glacialTrail;
            this.frozenTundra = frozenTundra;
            this.theAncientsWay = theAncientsWay;
            this.worldstoneKeep = worldstoneKeep;
        }
    }

    @Builder
    public Act5Status(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 4), visited, introduced, quests, waypoints);
    }
}
