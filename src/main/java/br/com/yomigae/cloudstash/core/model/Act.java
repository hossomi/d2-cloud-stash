package br.com.yomigae.cloudstash.core.model;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.*;

public record Act(Difficulty difficulty, int act) {

    public static final int ACTS_PER_DIFFICULTY = 5;

    public Act(Difficulty difficulty, int act) {
        this.difficulty = checkNotNull(difficulty, "Act difficulty is required");
        this.act = checkElementIndex(act, ACTS_PER_DIFFICULTY, "Act number");
    }

    public static Act fromAbsoluteAct(int act) {
        return new Act(
                Difficulty.fromIndex(floorDiv(act, ACTS_PER_DIFFICULTY)),
                floorMod(act, ACTS_PER_DIFFICULTY));
    }
}
