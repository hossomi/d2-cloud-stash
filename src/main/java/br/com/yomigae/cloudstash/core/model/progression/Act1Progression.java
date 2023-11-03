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
public class Act1Progression extends ActProgression<Act1Progression.Quests, Act1Progression.Waypoints> {

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActProgression.Quests {
        QuestStatus denOfEvil;
        QuestStatus sistersBurialGrounds;
        QuestStatus theSearchForCain;
        QuestStatus theForgottenTower;
        QuestStatus toolsOfTheTrade;
        QuestStatus sistersToTheSlaughter;

        public Quests(
                QuestStatus denOfEvil, QuestStatus sistersBurialGrounds, QuestStatus theSearchForCain,
                QuestStatus theForgottenTower, QuestStatus toolsOfTheTrade, QuestStatus sistersToTheSlaughter) {
            super(List.of(
                    denOfEvil, sistersBurialGrounds, theSearchForCain,
                    theForgottenTower, toolsOfTheTrade, sistersToTheSlaughter));
            this.denOfEvil = denOfEvil;
            this.sistersBurialGrounds = sistersBurialGrounds;
            this.theSearchForCain = theSearchForCain;
            this.theForgottenTower = theForgottenTower;
            this.toolsOfTheTrade = toolsOfTheTrade;
            this.sistersToTheSlaughter = sistersToTheSlaughter;
        }

        public static class Builder extends ActProgression.Quests.Builder<Act1Progression.Builder, Builder, Waypoints.Builder> {

            public Builder(Act1Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Quests.Builder> trySet(QuestStatus status) {
                return Optional.ofNullable(switch (status.quest()) {
                    case DEN_OF_EVIL -> denOfEvil(status);
                    case SISTERS_BURIAL_GROUNDS -> sistersBurialGrounds(status);
                    case THE_SEARCH_FOR_CAIN -> theSearchForCain(status);
                    case THE_FORGOTTEN_TOWER -> theForgottenTower(status);
                    case TOOLS_OF_THE_TRADE -> toolsOfTheTrade(status);
                    case SISTERS_TO_THE_SLAUGHTER -> sistersToTheSlaughter(status);
                    default -> null;
                });
            }
        }
    }

    @lombok.Builder(builderMethodName = "")
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActProgression.Waypoints {
        WaypointStatus rogueEncampment;
        WaypointStatus coldPlains;
        WaypointStatus stonyField;
        WaypointStatus darkWood;
        WaypointStatus blackMarsh;
        WaypointStatus outerCloister;
        WaypointStatus jail;
        WaypointStatus innerCloister;
        WaypointStatus catacombs;

        public Waypoints(
                WaypointStatus rogueEncampment, WaypointStatus coldPlains, WaypointStatus stonyField,
                WaypointStatus darkWood, WaypointStatus blackMarsh, WaypointStatus outerCloister,
                WaypointStatus jail, WaypointStatus innerCloister, WaypointStatus catacombs) {
            super(List.of(
                    rogueEncampment, coldPlains, stonyField,
                    darkWood, blackMarsh, outerCloister,
                    jail, innerCloister, catacombs));
            this.rogueEncampment = rogueEncampment;
            this.coldPlains = coldPlains;
            this.stonyField = stonyField;
            this.darkWood = darkWood;
            this.blackMarsh = blackMarsh;
            this.outerCloister = outerCloister;
            this.jail = jail;
            this.innerCloister = innerCloister;
            this.catacombs = catacombs;
        }

        public static class Builder extends ActProgression.Waypoints.Builder<Act1Progression.Builder, Quests.Builder, Builder> {

            public Builder(Act1Progression.Builder act) {
                super(act);
            }

            @Override
            protected Optional<Waypoints.Builder> trySet(WaypointStatus status) {
                return Optional.ofNullable(switch (status.area()) {
                    case ROGUE_ENCAMPMENT -> rogueEncampment(status);
                    case COLD_PLAINS -> coldPlains(status);
                    case STONY_FIELD -> stonyField(status);
                    case DARK_WOOD -> darkWood(status);
                    case BLACK_MARSH -> blackMarsh(status);
                    case OUTER_CLOISTER -> outerCloister(status);
                    case JAIL_LEVEL_1 -> jail(status);
                    case INNER_CLOISTER -> innerCloister(status);
                    case CATACOMBS_LEVEL_2 -> catacombs(status);
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
    public Act1Progression(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 0), visited, introduced, quests, waypoints);
    }
}
