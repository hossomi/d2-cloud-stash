

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
public class Act4Progression extends ActProgression<Act4Progression.Quests, Act4Progression.Waypoints> {

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActProgression.Quests {
        QuestStatus theFallenAngel;
        QuestStatus hellsForge;
        QuestStatus terrorsEnd;

        public Quests(QuestStatus theFallenAngel, QuestStatus hellsForge, QuestStatus terrorsEnd) {
            super(List.of(theFallenAngel, hellsForge, terrorsEnd));
            this.theFallenAngel = theFallenAngel;
            this.hellsForge = hellsForge;
            this.terrorsEnd = terrorsEnd;
        }

        public static class Builder extends ActProgression.Quests.Builder<Act4Progression.Builder, Builder, Waypoints.Builder> {

            public Builder(Act4Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Quests.Builder> trySet(QuestStatus status) {
                return Optional.ofNullable(switch (status.quest()) {
                    case THE_FALLEN_ANGEL -> theFallenAngel(status);
                    case HELLS_FORGE -> hellsForge(status);
                    case TERRORS_END -> terrorsEnd(status);
                    default -> null;
                });
            }
        }
    }

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActProgression.Waypoints {
        WaypointStatus pandemoniumFortress;
        WaypointStatus cityOfTheDamned;
        WaypointStatus riverOfFlame;

        public Waypoints(WaypointStatus pandemoniumFortress, WaypointStatus cityOfTheDamned, WaypointStatus riverOfFlame) {
            super(List.of(pandemoniumFortress, cityOfTheDamned, riverOfFlame));
            this.pandemoniumFortress = pandemoniumFortress;
            this.cityOfTheDamned = cityOfTheDamned;
            this.riverOfFlame = riverOfFlame;
        }

        public static class Builder extends ActProgression.Waypoints.Builder<Act4Progression.Builder, Quests.Builder, Builder> {

            public Builder(Act4Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Waypoints.Builder> trySet(WaypointStatus status) {
                return Optional.ofNullable(switch (status.area()) {
                    case THE_PANDEMONIUM_FORTRESS -> pandemoniumFortress(status);
                    case CITY_OF_THE_DAMNED -> cityOfTheDamned(status);
                    case RIVER_OF_FLAME -> riverOfFlame(status);
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
    public Act4Progression(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 3), visited, introduced, quests, waypoints);
    }
}
