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
public class Act1Status extends ActStatus<Act1Status.Quests, Act1Status.Waypoints> {

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActStatus.Quests {
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

        public static class QuestsBuilder extends ActStatus.Quests.QuestsBuilder<QuestsBuilder> {

            @Override
            public QuestsBuilder set(QuestStatus status) {
                switch (status.quest()) {
                    case DEN_OF_EVIL -> this.denOfEvil = status;
                    case SISTERS_BURIAL_GROUNDS -> this.sistersBurialGrounds = status;
                    case THE_SEARCH_FOR_CAIN -> this.theSearchForCain = status;
                    case THE_FORGOTTEN_TOWER -> this.theForgottenTower = status;
                    case TOOLS_OF_THE_TRADE -> this.toolsOfTheTrade = status;
                    case SISTERS_TO_THE_SLAUGHTER -> this.sistersToTheSlaughter = status;
                    default -> throw new IllegalArgumentException("Unknown quest");
                }
                return self();
            }
        }
    }

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActStatus.Waypoints {
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

    }

    @Builder
    public Act1Status(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 0), visited, introduced, quests, waypoints);
    }
}
