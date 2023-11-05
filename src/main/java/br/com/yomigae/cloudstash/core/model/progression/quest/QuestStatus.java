package br.com.yomigae.cloudstash.core.model.progression.quest;

public interface QuestStatus<Q extends Quest<Q, S>, S extends QuestStatus<Q, S>> {
    Q quest();
    boolean completed();
}
