package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScalingBreakpointTest {

    private final Scaling.Breakpoint scaling = new Scaling.Breakpoint(new BreakpointMap<Scaling>()
            .put(5, new Scaling.Linear(10, 1))
            .put(10, new Scaling.Linear(15, 2))
            .put(20, new Scaling.Linear(50, 3))
            .put(25, new Scaling.Linear(80, 5)));

    static List<Arguments> samples() {
        return List.of(
                Arguments.of(-10, -5),
                Arguments.of(-5, 0),
                Arguments.of(-1, 4),
                Arguments.of(0, 5),
                Arguments.of(1, 6),
                Arguments.of(5, 10),
                Arguments.of(6, 11),
                Arguments.of(9, 14),
                Arguments.of(10, 15),
                Arguments.of(11, 17),
                Arguments.of(15, 25),
                Arguments.of(19, 33),
                Arguments.of(20, 50),
                Arguments.of(21, 53),
                Arguments.of(24, 62),
                Arguments.of(25, 80),
                Arguments.of(26, 85),
                Arguments.of(30, 105));
    }

    @ParameterizedTest
    @MethodSource("samples")
    void applyWorks(int x, double y) {
        assertThat(scaling.apply(x)).isEqualTo(y);
    }

    @ParameterizedTest
    @MethodSource("samples")
    void unapplyWorks(int x, double y) {
        assertThat(scaling.unapply(y)).isEqualTo(x);
    }

    @Test
    void unapplyThrowsOnInvalidValue() {
        assertThatThrownBy(() -> scaling.unapply(70)).isInstanceOf(IllegalArgumentException.class);
    }
}
