

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
public class Act3Status extends ActStatus<Act3Status.Quests, Act3Status.Waypoints> {

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActStatus.Quests {
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
    }

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActStatus.Waypoints {
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
    }

    @Builder
    public Act3Status(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 2), visited, introduced, quests, waypoints);
    }
}
