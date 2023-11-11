package br.com.yomigae.cloudstash.core.model.progression.quest;

public interface QuestStatus {
    boolean completed();

    record Generic(boolean completed) implements QuestStatus { }
}
