
package br.com.yomigae.cloudstash.core.model.progression;

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
public class Act2Status extends ActStatus<Act2Status.Quests, Act2Status.Waypoints> {

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActStatus.Quests {
        QuestStatus radamentsLair;
        QuestStatus theHoradricStaff;
        QuestStatus taintedSun;
        QuestStatus arcaneSanctuary;
        QuestStatus theSummoner;
        QuestStatus theSevenTombs;

        public Quests(
                QuestStatus radamentsLair, QuestStatus theHoradricStaff, QuestStatus taintedSun,
                QuestStatus arcaneSanctuary, QuestStatus theSummoner, QuestStatus theSevenTombs) {
            super(List.of(
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

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActStatus.Waypoints {
        WaypointStatus lutGholein;
        WaypointStatus sewers;
        WaypointStatus dryHills;
        WaypointStatus hallsOfTheDead;
        WaypointStatus farOasis;
        WaypointStatus lostCity;
        WaypointStatus pallaceCellar;
        WaypointStatus arcaneSanctuary;
        WaypointStatus canyonOfTheMagi;

        public Waypoints(
                WaypointStatus lutGholein, WaypointStatus sewers, WaypointStatus dryHills,
                WaypointStatus hallsOfTheDead, WaypointStatus farOasis, WaypointStatus lostCity,
                WaypointStatus pallaceCellar, WaypointStatus arcaneSanctuary, WaypointStatus canyonOfTheMagi) {
            super(List.of(
                    lutGholein, sewers, dryHills,
                    hallsOfTheDead, farOasis, lostCity,
                    pallaceCellar, arcaneSanctuary, canyonOfTheMagi));
            this.lutGholein = lutGholein;
            this.sewers = sewers;
            this.dryHills = dryHills;
            this.hallsOfTheDead = hallsOfTheDead;
            this.farOasis = farOasis;
            this.lostCity = lostCity;
            this.pallaceCellar = pallaceCellar;
            this.arcaneSanctuary = arcaneSanctuary;
            this.canyonOfTheMagi = canyonOfTheMagi;
        }
    }

    @Builder
    public Act2Status(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 1), visited, introduced, quests, waypoints);
    }
}
