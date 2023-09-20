package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalingExperienceTest {

    private final Scaling.Experience scaling = new Scaling.Experience(10, 5);

    static List<Arguments> samples() {
        return List.of(
                Arguments.of(-3, 1960),
                Arguments.of(-2, 2880),
                Arguments.of(-1, 4050),
                Arguments.of(0, 5500),
                Arguments.of(1, 7260),
                Arguments.of(2, 9360));
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
