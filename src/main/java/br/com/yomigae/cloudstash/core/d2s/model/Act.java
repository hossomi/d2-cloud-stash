package br.com.yomigae.cloudstash.core.d2s.model;

import lombok.Getter;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.floorDiv;
import static java.lang.Math.floorMod;
import static java.lang.String.format;

@Getter
public enum Act {
    ACT1_NORMAL(Difficulty.NORMAL, 0),
    ACT2_NORMAL(Difficulty.NORMAL, 1),
    ACT3_NORMAL(Difficulty.NORMAL, 2),
    ACT4_NORMAL(Difficulty.NORMAL, 3),
    ACT5_NORMAL(Difficulty.NORMAL, 4),
    ACT1_NIGHTMARE(Difficulty.NIGHTMARE, 0),
    ACT2_NIGHTMARE(Difficulty.NIGHTMARE, 1),
    ACT3_NIGHTMARE(Difficulty.NIGHTMARE, 2),
    ACT4_NIGHTMARE(Difficulty.NIGHTMARE, 3),
    ACT5_NIGHTMARE(Difficulty.NIGHTMARE, 4),
    ACT1_HELL(Difficulty.HELL, 0),
    ACT2_HELL(Difficulty.HELL, 1),
    ACT3_HELL(Difficulty.HELL, 2),
    ACT4_HELL(Difficulty.HELL, 3),
    ACT5_HELL(Difficulty.HELL, 4);

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
        return format("Act %d %s", number + 1, difficulty);
    }

}
