package br.com.yomigae.cloudstash.core.model.progression.quest;

import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import br.com.yomigae.cloudstash.core.model.progression.quest.QuestStatus.Generic;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

import static java.lang.String.format;

@Getter
@Builder
@AllArgsConstructor
public abstract class ActStatus<
        Q extends ActStatus.Quests> {

    private final Act act;
    private final boolean visited;
    private final boolean introduced;
    private final Q quests;

    @Getter
    @AllArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public abstract static class Quests {

        public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
            return tryGet(quest).orElseThrow(() -> new IllegalArgumentException(format(
                    "Invalid quest %s for %s",
                    quest, getClass().getDeclaringClass().getSimpleName())));
        }

        protected abstract <Q extends Quest<S>, S extends QuestStatus> Optional<S> tryGet(Q quest);

        @AllArgsConstructor
        public abstract static class Builder<B extends Builder<B>> {

            @SuppressWarnings("unchecked")
            protected B self() {
                return (B) this;
            }

            public <Q extends Quest<S>, S extends QuestStatus> B set(Q quest, S status) {
                return trySet(quest, status).orElseThrow(() -> new IllegalArgumentException(format(
                        "Invalid quest %s for %s",
                        quest, getClass().getDeclaringClass().getSimpleName())));
            }

            protected abstract <Q extends Quest<S>, S extends QuestStatus> Optional<B> trySet(Q quest, S status);
        }
    }
}

class Act1 extends ActStatus<Act1.Quests> {

    public Act1(Difficulty difficulty, boolean visited, boolean introduced, Act1.Quests quests) {
        super(Act.from(difficulty, 0), visited, introduced, quests);
    }

    @lombok.Builder
    public static class Quests extends ActStatus.Quests {

        Generic denOfEvil;
        Generic sistersBurialGrounds;
        Generic theSearchForCain;
        Generic theForgottenTower;
        Generic toolsOfTheTrade;
        Generic sistersToTheSlaughter;

        public static class Builder extends ActStatus.Quests.Builder<Act1.Quests.Builder> {
            @Override
            @SuppressWarnings("unused")
            protected <Q extends Quest<S>, S extends QuestStatus> Optional<Builder> trySet(Q quest, S status) {
                return Optional.ofNullable(switch (quest) {
                    case Quest.Act1.DenOfEvil q -> denOfEvil((Generic) status);
                    case Quest.Act1.SistersBurialGrounds q -> sistersBurialGrounds((Generic) status);
                    case Quest.Act1.TheSearchForCain q -> theSearchForCain((Generic) status);
                    case Quest.Act1.TheForgottenTower q -> theForgottenTower((Generic) status);
                    case Quest.Act1.ToolsOfTheTrade q -> toolsOfTheTrade((Generic) status);
                    case Quest.Act1.SistersToTheSlaughter q -> sistersToTheSlaughter((Generic) status);
                    default -> null;
                });
            }
        }

        @Override
        @SuppressWarnings({ "unchecked", "unused" })
        public <Q extends Quest<S>, S extends QuestStatus> Optional<S> tryGet(Q quest) {
            return Optional.ofNullable(switch (quest) {
                case Quest.Act1.DenOfEvil q -> (S) denOfEvil;
                case Quest.Act1.SistersBurialGrounds q -> (S) sistersBurialGrounds;
                case Quest.Act1.TheSearchForCain q -> (S) theSearchForCain;
                case Quest.Act1.TheForgottenTower q -> (S) theForgottenTower;
                case Quest.Act1.ToolsOfTheTrade q -> (S) toolsOfTheTrade;
                case Quest.Act1.SistersToTheSlaughter q -> (S) sistersToTheSlaughter;
                default -> null;
            });
        }
    }
}

