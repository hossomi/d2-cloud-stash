package br.com.yomigae.cloudstash.core.model.quest;

import br.com.yomigae.cloudstash.core.model.Act;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.list;

@Getter
@AllArgsConstructor
public abstract class ActStatus {

    private final Act act;
    private final boolean visited;
    private final boolean introduced;
    private final List<QuestStatus> quests;

    public QuestStatus quest(int q) {
        return quests.get(q);
    }

    @Override
    public String toString() {
        return list(
                checkbox(act.toString().toUpperCase(), visited()),
                quests.stream().map(Object::toString).toList());
    }
}
