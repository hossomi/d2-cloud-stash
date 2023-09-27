package br.com.yomigae.cloudstash.core.model;

import java.util.Arrays;

import static br.com.yomigae.cloudstash.core.model.Difficulty.*;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.floorDiv;
import static java.lang.Math.floorMod;
import static java.lang.String.format;

public enum Act {
    ACT1_NORMAL(NORMAL, 0),
    ACT2_NORMAL(NORMAL, 1),
    ACT3_NORMAL(NORMAL, 2),
    ACT4_NORMAL(NORMAL, 3),
    ACT5_NORMAL(NORMAL, 4),
    ACT1_NIGHTMARE(NIGHTMARE, 0),
    ACT2_NIGHTMARE(NIGHTMARE, 1),
    ACT3_NIGHTMARE(NIGHTMARE, 2),
    ACT4_NIGHTMARE(NIGHTMARE, 3),
    ACT5_NIGHTMARE(NIGHTMARE, 4),
    ACT1_HELL(HELL, 0),
    ACT2_HELL(HELL, 1),
    ACT3_HELL(HELL, 2),
    ACT4_HELL(HELL, 3),
    ACT5_HELL(HELL, 4);

    public static final int ACTS_PER_DIFFICULTY = 5;
    private final Difficulty difficulty;
    private final int number;

    Act(Difficulty difficulty, int act) {
        this.difficulty = checkNotNull(difficulty, "Act difficulty is required");
        this.number = checkElementIndex(act, ACTS_PER_DIFFICULTY, "Act number");
    }

    public static Act from(Difficulty difficulty, int number) {
        return Arrays.stream(values())
                .filter(act -> act.difficulty == difficulty)
                .filter(act -> act.number == number)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format(
                        "Invalid act: Act %d %s",
                        number, difficulty)));
    }

    public static Act fromIndex(int index) {
        return from(
                Difficulty.fromIndex(floorDiv(index, ACTS_PER_DIFFICULTY)),
                floorMod(index, ACTS_PER_DIFFICULTY));
    }

    @Override
    public String toString() {
        return format("Act %d %s", number, difficulty);
    }
}
