package br.com.yomigae.cloudstash.core.d2s.model.acts;

public interface QuestStatus {
    boolean completed();

    record Generic(boolean completed) implements QuestStatus { }
}
