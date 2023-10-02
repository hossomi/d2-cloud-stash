

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
public class Act4Status extends ActStatus<Act4Status.Quests, Act4Status.Waypoints> {

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Quests extends ActStatus.Quests {
        QuestStatus theFallenAngel;
        QuestStatus hellsForge;
        QuestStatus terrorsEnd;

        public Quests(QuestStatus theFallenAngel, QuestStatus hellsForge, QuestStatus terrorsEnd) {
            super(List.of(theFallenAngel, hellsForge, terrorsEnd));
            this.theFallenAngel = theFallenAngel;
            this.hellsForge = hellsForge;
            this.terrorsEnd = terrorsEnd;
        }
    }

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class Waypoints extends ActStatus.Waypoints {
        WaypointStatus pandemoniumFortress;
        WaypointStatus cityOfTheDamned;
        WaypointStatus riverOfFlame;

        public Waypoints(WaypointStatus pandemoniumFortress, WaypointStatus cityOfTheDamned, WaypointStatus riverOfFlame) {
            super(List.of(pandemoniumFortress, cityOfTheDamned, riverOfFlame));
            this.pandemoniumFortress = pandemoniumFortress;
            this.cityOfTheDamned = cityOfTheDamned;
            this.riverOfFlame = riverOfFlame;
        }
    }

    @Builder
    public Act4Status(Difficulty difficulty, boolean visited, boolean introduced, Quests quests, Waypoints waypoints) {
        super(Act.from(difficulty, 3), visited, introduced, quests, waypoints);
    }
}
