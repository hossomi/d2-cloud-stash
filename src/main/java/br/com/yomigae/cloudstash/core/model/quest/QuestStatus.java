package br.com.yomigae.cloudstash.core.model.quest;

import lombok.Builder;

public interface QuestStatus {

    boolean completed();

    record Generic(boolean completed) implements QuestStatus {}

    @Builder
    record PrisonOfIce(boolean completed, boolean scrollConsumed) implements QuestStatus {}
}
