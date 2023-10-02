package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Act;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.list;

@Getter
@AllArgsConstructor
public abstract class ActStatus<Q extends ActStatus.Quests, W extends ActStatus.Waypoints> {

    private final Act act;
    private final boolean visited;
    private final boolean introduced;
    private final Q quests;
    private final W waypoints;

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Quests {
        List<QuestStatus> all;

        public abstract static class QuestsBuilder<B extends QuestsBuilder<B>> {

            @SuppressWarnings("unchecked")
            protected B self() {
                return (B) this;
            }

            public abstract B set(QuestStatus status);
        }
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Waypoints {
        List<WaypointStatus> all;
    }

    @Override
    public String toString() {
        return list(
                checkbox(act.toString().toUpperCase(), visited()),
                List.of(list("Quests", quests.all().stream().map(Object::toString).toList())));
    }
}
