

package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import br.com.yomigae.cloudstash.core.model.progression.QuestStatus.PrisonOfIce;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Optional;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Act5Progression extends ActProgression<Act5Progression.Quests, Act5Progression.Waypoints> {

    @lombok.Builder(builderMethodName = "", builderClassName = "Builder")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActProgression.Quests {
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

        public static class Builder extends ActProgression.Quests.Builder<Act5Progression.Builder, Builder, Waypoints.Builder> {

            public Builder(Act5Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Quests.Builder> trySet(QuestStatus status) {
                return Optional.ofNullable(switch (status.quest()) {
                    case SIEGE_ON_HARROGATH -> siegeOnHarrogath(status);
                    case RESCUE_ON_MOUNTAIN_ARREAT -> rescueOnMountainArreat(status);
                    case PRISON_OF_ICE -> prisonOfIce((PrisonOfIce) status);
                    case BETRAYAL_OF_HARROGATH -> betrayalOfHarrogath(status);
                    case RITE_OF_PASSAGE -> riteOfPassage(status);
                    case EVE_OF_DESTRUCTION -> eveOfDestruction(status);
                    default -> null;
                });
            }
        }
    }

    @lombok.Builder(builderMethodName = "", builderClassName = "Builder")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActProgression.Waypoints {
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

        public static class Builder extends ActProgression.Waypoints.Builder<Act5Progression.Builder, Quests.Builder, Builder> {

            public Builder(Act5Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Waypoints.Builder> trySet(WaypointStatus status) {
                return Optional.ofNullable(switch (status.area()) {
                    case HARROGATH -> harrogath(status);
                    case FRIGID_HIGHLANDS -> frigidHighlands(status);
                    case ARREAT_PLATEAU -> arreatPlateau(status);
                    case CRYSTALLINE_PASSAGE -> crystallinePassage(status);
                    case HALLS_OF_PAIN -> hallsOfPain(status);
                    case GLACIAL_TRAIL -> glacialTrail(status);
                    case FROZEN_TUNDRA -> frozenTundra(status);
                    case THE_ANCIENTS_WAY -> theAncientsWay(status);
                    case WORLDSTONE_KEEP_LEVEL_2 -> worldstoneKeep(status);
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
    public Act5Progression(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 4), visited, introduced, quests, waypoints);
    }
}
