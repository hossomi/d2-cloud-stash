package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalingLinearTest {

    private final Scaling.Linear scaling = new Scaling.Linear(10, 5);

    static List<Arguments> samples() {
        return List.of(
                Arguments.of(-3, -5),
                Arguments.of(-2, 0),
                Arguments.of(-1, 5),
                Arguments.of(0, 10),
                Arguments.of(1, 15),
                Arguments.of(2, 20));
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
}
