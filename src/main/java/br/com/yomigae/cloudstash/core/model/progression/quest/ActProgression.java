package br.com.yomigae.cloudstash.core.model.progression.quest;

import br.com.yomigae.cloudstash.core.model.Act;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
public abstract class ActProgression<
        Q extends Quest<Q, ?>,
        QB extends ActProgression.Quests<Q>> {

    private final Act act;
    private final boolean visited;
    private final boolean introduced;
    private final QB quests;

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Quests<Q extends Quest<Q, ?>> {

        @AllArgsConstructor
        public abstract static class Builder<
                Q extends Quest<Q, S>,
                S extends QuestStatus<Q, S>,
                QB extends Builder<Q, S, QB>> {

            @SuppressWarnings("unchecked")
            protected QB self() {
                return (QB) this;
            }

            public abstract <Q2 extends Q, S2 extends QuestStatus<? super Q2, S2>> QB set(S2 status);
        }
    }
}

class Act1Progression extends ActProgression<Quest.Act1<?,?>, Act1Progression.Quests> {

    public Act1Progression(Act act, boolean visited, boolean introduced, Act1Progression.Quests quests) {
        super(act, visited, introduced, quests);
    }

    public static class Quests extends ActProgression.Quests<Quest.Act1<Quest.Act1<?,?>, ?>> {

    }
}
