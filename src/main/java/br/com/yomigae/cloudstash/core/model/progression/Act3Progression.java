

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
public class Act3Progression extends ActProgression<Act3Progression.Quests, Act3Progression.Waypoints> {

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActProgression.Quests {
        QuestStatus theGoldenBird;
        QuestStatus bladeOfTheOldReligion;
        QuestStatus khalimsWill;
        QuestStatus lamEsensTome;
        QuestStatus theBlackenedTemple;
        QuestStatus theGuardian;

        public Quests(
                QuestStatus theGoldenBird, QuestStatus bladeOfTheOldReligion, QuestStatus khalimsWill,
                QuestStatus lamEsensTome, QuestStatus theBlackenedTemple, QuestStatus theGuardian) {
            super(List.of(
                    theGoldenBird, bladeOfTheOldReligion, khalimsWill,
                    lamEsensTome, theBlackenedTemple, theGuardian));
            this.theGoldenBird = theGoldenBird;
            this.bladeOfTheOldReligion = bladeOfTheOldReligion;
            this.khalimsWill = khalimsWill;
            this.lamEsensTome = lamEsensTome;
            this.theBlackenedTemple = theBlackenedTemple;
            this.theGuardian = theGuardian;
        }

        public static class Builder extends ActProgression.Quests.Builder<Act3Progression.Builder, Builder, Waypoints.Builder> {

            public Builder(Act3Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Quests.Builder> trySet(QuestStatus status) {
                return Optional.ofNullable(switch (status.quest()) {
                    case THE_GOLDEN_BIRD -> theGoldenBird(status);
                    case BLADE_OF_THE_OLD_RELIGION -> bladeOfTheOldReligion(status);
                    case KHALIMS_WILL -> khalimsWill(status);
                    case LAM_ESENS_TOME -> lamEsensTome(status);
                    case THE_BLACKENED_TEMPLE -> theBlackenedTemple(status);
                    case THE_GUARDIAN -> theGuardian(status);
                    default -> null;
                });
            }
        }
    }

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActProgression.Waypoints {
        WaypointStatus kurastDocks;
        WaypointStatus spiderForest;
        WaypointStatus greatMarsh;
        WaypointStatus flayerJungle;
        WaypointStatus lowerKurast;
        WaypointStatus kurastBazaar;
        WaypointStatus upperKurast;
        WaypointStatus travincal;
        WaypointStatus duranceOfHate;

        public Waypoints(
                WaypointStatus kurastDocks, WaypointStatus spiderForest, WaypointStatus greatMarsh,
                WaypointStatus flayerJungle, WaypointStatus lowerKurast, WaypointStatus kurastBazaar,
                WaypointStatus upperKurast, WaypointStatus travincal, WaypointStatus duranceOfHate) {
            super(List.of(
                    kurastDocks, spiderForest, greatMarsh,
                    flayerJungle, lowerKurast, kurastBazaar,
                    upperKurast, travincal, duranceOfHate));
            this.kurastDocks = kurastDocks;
            this.spiderForest = spiderForest;
            this.greatMarsh = greatMarsh;
            this.flayerJungle = flayerJungle;
            this.lowerKurast = lowerKurast;
            this.kurastBazaar = kurastBazaar;
            this.upperKurast = upperKurast;
            this.travincal = travincal;
            this.duranceOfHate = duranceOfHate;
        }

        public static class Builder extends ActProgression.Waypoints.Builder<Act3Progression.Builder, Quests.Builder, Builder> {

            public Builder(Act3Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Waypoints.Builder> trySet(WaypointStatus status) {
                return Optional.ofNullable(switch (status.area()) {
                    case KURAST_DOCKS -> kurastDocks(status);
                    case SPIDER_FOREST -> spiderForest(status);
                    case GREAT_MARSH -> greatMarsh(status);
                    case FLAYER_JUNGLE -> flayerJungle(status);
                    case LOWER_KURAST -> lowerKurast(status);
                    case KURAST_BAZAAR -> kurastBazaar(status);
                    case UPPER_KURAST -> upperKurast(status);
                    case TRAVINCAL -> travincal(status);
                    case DURANCE_OF_HATE_LEVEL_2 -> duranceOfHate(status);
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

    @lombok.Builder
    public Act3Progression(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 2), visited, introduced, quests, waypoints);
    }
}
