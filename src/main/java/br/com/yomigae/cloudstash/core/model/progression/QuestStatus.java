package br.com.yomigae.cloudstash.core.model.progression;

import lombok.Builder;

import java.util.List;

import static br.com.yomigae.cloudstash.core.model.progression.Quest.PRISON_OF_ICE;
import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.list;

public interface QuestStatus {

    Quest quest();

    boolean completed();

    record Generic(Quest quest, boolean completed) implements QuestStatus {
        @Override
        public String toString() {
            return checkbox(quest.label(), completed);
        }
    }

    @Builder
    record PrisonOfIce(boolean completed, boolean scrollConsumed) implements QuestStatus {

        @Override
        public Quest quest() {
            return PRISON_OF_ICE;
        }

        @Override
        public String toString() {
            return list(checkbox(quest().label(), completed), List.of(
                    checkbox("Scroll consumed", scrollConsumed)));
        }
    }
}
