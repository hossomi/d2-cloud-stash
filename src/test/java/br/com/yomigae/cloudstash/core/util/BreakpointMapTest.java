package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BreakpointMapTest {

    private final BreakpointMap<String> map = new BreakpointMap<>(Map.of(5, "A", 15, "C", 10, "B"));

    @ParameterizedTest
    @CsvSource(textBlock = """
            0, A
            5, A
            7, A
            10, B
            12, B
            15, C
            666, C
            """)
    void getReturnsBreakpointValue(int key, String value) {
        assertThat(map.get(key)).isEqualTo(value);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            0, 5
            5, 5
            7, 5
            10, 10
            12, 10
            15, 15
            16, 15
            666, 15
            """)
    void breakpointReturnsHighestBreakpointForKey(int key, int breakpoint) {
        assertThat(map.breakpoint(key)).isEqualTo(breakpoint);
    }

    @Test
    void breakpointsAreSorted() {
        assertThat(map.breakpoints()).containsExactly(5, 10, 15);
    }

    @Test
    void valuesAreSorted() {
        assertThat(map.values()).containsExactly("A", "B", "C");
    }
}
