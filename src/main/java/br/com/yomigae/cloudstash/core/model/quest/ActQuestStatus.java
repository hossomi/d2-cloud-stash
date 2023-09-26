package br.com.yomigae.cloudstash.core.model.quest;

import java.util.List;

public interface ActQuestStatus {
    boolean visited();

    boolean introduced();

    List<QuestStatus> quests();

    default QuestStatus quest(int q) {
        return quests().get(q);
    };
}
