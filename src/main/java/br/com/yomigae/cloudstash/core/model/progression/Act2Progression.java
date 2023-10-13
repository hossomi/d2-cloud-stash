
package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Optional;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Act2Progression extends ActProgression<Act2Progression.Quests, Act2Progression.Waypoints> {

    @lombok.Builder(builderMethodName = "", builderClassName = "Builder")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActProgression.Quests {
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

        public static class Builder extends ActProgression.Quests.Builder<Act2Progression.Builder, Builder, Waypoints.Builder> {

            public Builder(Act2Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Quests.Builder> trySet(QuestStatus status) {
                return Optional.ofNullable(switch (status.quest()) {
                    case RADAMENTS_LAIR -> radamentsLair(status);
                    case THE_HORADRIC_STAFF -> theHoradricStaff(status);
                    case TAINTED_SUN -> taintedSun(status);
                    case ARCANE_SANCTUARY -> arcaneSanctuary(status);
                    case THE_SUMMONER -> theSummoner(status);
                    case THE_SEVEN_TOMBS -> theSevenTombs(status);
                    default -> null;
                });
            }
        }
    }

    @lombok.Builder(builderMethodName = "", builderClassName = "Builder")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActProgression.Waypoints {
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

        public static class Builder extends ActProgression.Waypoints.Builder<Act2Progression.Builder, Quests.Builder, Builder> {

            public Builder(Act2Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Waypoints.Builder> trySet(WaypointStatus status) {
                return Optional.ofNullable(switch (status.area()) {
                    case LUT_GHOLEIN -> lutGholein(status);
                    case LUT_GHOLEIN_SEWERS_LEVEL_2 -> sewers(status);
                    case DRY_HILLS -> dryHills(status);
                    case HALLS_OF_THE_DEAD_LEVEL_2 -> hallsOfTheDead(status);
                    case FAR_OASIS -> farOasis(status);
                    case LOST_CITY -> lostCity(status);
                    case PALACE_CELLAR_LEVEL_1 -> pallaceCellar(status);
                    case ARCANE_SANCTUARY -> arcaneSanctuary(status);
                    case CANYON_OF_THE_MAGI -> canyonOfTheMagi(status);
                    default -> null;
                });
            }
        }
    }

    public static class Builder extends ActProgression.Builder<Builder, Quests.Builder, Waypoints.Builder> {

        @Override
        public Quests.Builder quests() {
            return new Quests.Builder(this);
        }

        @Override
        public Builder quests(Quests.Builder quests) {
            this.quests = quests.build();
            return this;
        }

        @Override
        public Waypoints.Builder waypoints() {
            return new Waypoints.Builder(this);
        }

        @Override
        public Builder waypoints(Waypoints.Builder waypoints) {
            this.waypoints = waypoints.build();
            return this;
        }
    }

    @lombok.Builder(builderClassName = "Builder")
    public Act2Progression(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 1), visited, introduced, quests, waypoints);
    }
}
