package br.com.yomigae.cloudstash.core.d2s.model.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public interface QuestStatus {

    boolean completed();

    @Data
    @Builder
    @AllArgsConstructor
    final class Generic implements QuestStatus {
        private boolean completed;
    }
}
