package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Act;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Optional;

import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.list;
import static java.lang.String.format;

@Getter
@AllArgsConstructor
public abstract class ActProgression<Q extends ActProgression.Quests, W extends ActProgression.Waypoints> {

    private final Act act;
    private final boolean visited;
    private final boolean introduced;
    private final Q quests;
    private final W waypoints;

    @Override
    public String toString() {
        return list(
                checkbox(act.toString().toUpperCase(), visited()),
                List.of(
                        list("Quests", quests.all().stream().map(Object::toString).toList()),
                        list("Waypoints", waypoints.all().stream().map(Object::toString).toList())
                ));
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Quests {
        List<QuestStatus> all;

        @AllArgsConstructor
        public abstract static class Builder<
                A extends ActProgression.Builder<A, Q, W>,
                Q extends Quests.Builder<A, Q, W>,
                W extends Waypoints.Builder<A, Q, W>> {

            protected final A act;

            @SuppressWarnings("unchecked")
            protected Q self() {
                return (Q) this;
            }

            public A done() {
                return act.quests(self());
            }

            public Q set(QuestStatus status) {
                return trySet(status).orElseThrow(() -> new IllegalArgumentException(format(
                        "Invalid waypoint %s for %s",
                        status.quest(), act.getClass().getDeclaringClass().getSimpleName())));
            }

            protected abstract Optional<Q> trySet(QuestStatus status);
        }
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Waypoints {
        List<WaypointStatus> all;

        @AllArgsConstructor
        public abstract static class Builder<
                A extends ActProgression.Builder<A, Q, W>,
                Q extends Quests.Builder<A, Q, W>,
                W extends Waypoints.Builder<A, Q, W>> {

            protected final A act;

            @SuppressWarnings("unchecked")
            protected W self() {
                return (W) this;
            }

            public A done() {
                return act.waypoints(self());
            }

            public W set(WaypointStatus status) {
                return trySet(status).orElseThrow(() -> new IllegalArgumentException(format(
                        "Invalid waypoint %s for %s",
                        status.area(), act.getClass().getDeclaringClass().getSimpleName())));
            }

            protected abstract Optional<W> trySet(WaypointStatus status);
        }
    }

    public abstract static class Builder<
            A extends ActProgression.Builder<A, Q, W>,
            Q extends Quests.Builder<A, Q, W>,
            W extends Waypoints.Builder<A, Q, W>> {

        public abstract A quests(Q quests);

        public abstract Q quests();

        public abstract A waypoints(W waypoints);

        public abstract W waypoints();

        public abstract ActProgression<?, ?> build();

    }
}
