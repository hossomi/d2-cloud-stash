package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class BreakpointMapTest {

    // Use a LinkedHashMap to keep keys unordered and make sure it still works
    private final BreakpointMap<String> breakpoints = new BreakpointMap<>(new LinkedHashMap<>() {{
        put(5, "A");
        put(15, "C");
        put(10, "B");
    }});

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
        assertThat(breakpoints.get(key)).isEqualTo(value);
    }
}
