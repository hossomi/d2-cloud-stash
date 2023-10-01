

package br.com.yomigae.cloudstash.core.model.quest;

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
public class Act4Status extends ActStatus {

    QuestStatus theFallenAngel;
    QuestStatus hellsForge;
    QuestStatus terrorsEnd;

    @Builder
    public Act4Status(
            Difficulty difficulty, boolean visited, boolean introduced,
            QuestStatus theFallenAngel, QuestStatus hellsForge, QuestStatus terrorsEnd) {
        super(Act.from(difficulty, 3), visited, introduced, List.of(
                theFallenAngel, hellsForge, terrorsEnd));
        this.theFallenAngel = theFallenAngel;
        this.hellsForge = hellsForge;
        this.terrorsEnd = terrorsEnd;
    }
}
