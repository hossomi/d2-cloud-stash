package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

public class StringUtilsTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            Hello, 0, 5, Hello<<<<<
            Hello, 2, 3, >>Hello<<<
            Hello, 3, 2, >>>Hello<<
            Hello, 5, 5, >>>>>Hello<<<<<
            """)
    void padderPadWorks(String text, int left, int right, String expected) {
        assertThat(new StringUtils.Padder(text).left('>').right('<').pad(left, right)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            Hello, 10, 0.0, Hello<<<<<
            Hello, 10, 0.2, >Hello<<<<
            Hello, 10, 0.5, >>Hello<<<
            Hello, 10, 0.8, >>>>Hello<
            Hello, 10, 1.0, >>>>>Hello
            Hello, 15, 0.0, Hello<<<<<<<<<<
            Hello, 15, 0.2, >>Hello<<<<<<<<
            Hello, 15, 0.5, >>>>>Hello<<<<<
            Hello, 15, 0.8, >>>>>>>>Hello<<
            Hello, 15, 1.0, >>>>>>>>>>Hello
            """)
    void padderAlignWorks(String text, int width, double position, String expected) {
        assertThat(new StringUtils.Padder(text).left('>').right('<').align(width, position)).isEqualTo(expected);
    }
}
