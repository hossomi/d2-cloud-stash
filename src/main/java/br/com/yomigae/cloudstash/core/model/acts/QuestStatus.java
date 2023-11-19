package br.com.yomigae.cloudstash.core.model.acts;

public interface QuestStatus {
    boolean completed();

    record Generic(boolean completed) implements QuestStatus { }
}
