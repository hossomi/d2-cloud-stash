package br.com.yomigae.cloudstash.core.model.quest;

import br.com.yomigae.cloudstash.core.io.D2Strings;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Quest {
    DEN_OF_EVIL("qstsa1q1", 0),
    SISTERS_BURIAL_GROUNDS("qstsa1q2", 0),
    THE_SEARCH_FOR_CAIN("qstsa1q4", 0),
    THE_FORGOTTEN_TOWER("qstsa1q5", 0),
    TOOLS_OF_THE_TRADE("qstsa1q3", 0),
    SISTERS_TO_THE_SLAUGHTER("qstsa1q6", 0),

    RADAMENTS_LAIR("qstsa2q1", 1),
    THE_HORADRIC_STAFF("qstsa2q2", 1),
    TAINTED_SUN("qstsa2q3", 1),
    ARCANE_SANCTUARY("qstsa2q4", 1),
    THE_SUMMONER("qstsa2q5", 1),
    THE_SEVEN_TOMBS("qstsa2q6", 1),

    THE_GOLDEN_BIRD("qstsa3q4", 2),
    BLADE_OF_THE_OLD_RELIGION("qstsa3q3", 2),
    KHALIMS_WILL("qstsa3q2", 2),
    LAM_ESENS_TOME("qstsa3q1", 2),
    THE_BLACKENED_TEMPLE("qstsa3q5", 2),
    THE_GUARDIAN("qstsa3q6", 2),

    THE_FALLEN_ANGEL("qstsa4q1", 3),
    HELLS_FORGE("qstsa4q3", 3),
    TERRORS_END("qstsa4q2", 3),

    SIEGE_ON_HARROGATH("qstsa5q1", 4),
    RESCUE_ON_MOUNTAIN_ARREAT("qstsa5q2", 4),
    PRISON_OF_ICE("qstsa5q3", 4),
    BETRAYAL_OF_HARROGATH("qstsa5q4", 4),
    RITE_OF_PASSAGE("qstsa5q5", 4),
    EVE_OF_DESTRUCTION("qstsa5q6", 4);

    private final String label;
    private final int act;

    Quest(String nameKey, int act) {
        this.label = D2Strings.get(nameKey);
        this.act = act;
    }

    public static List<Quest> forAct(int act) {
        return Arrays.stream(values())
                .filter(q -> q.act == act)
                .toList();
    }

    public static Quest forAct(int act, int q) {
        return forAct(act).get(q);
    }
}
