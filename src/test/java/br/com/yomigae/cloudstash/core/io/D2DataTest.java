package br.com.yomigae.cloudstash.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.InputStream;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class D2DataTest {

    private static D2Data createReader(String fileName) {
        InputStream dataFile = requireNonNull(
                D2DataTest.class.getClassLoader().getResourceAsStream(fileName),
                format("Could not find test data file: %s", fileName));
        return new D2Data(dataFile);
    }

    @Test
    void getColumnsWorks() {
        try (D2Data reader = createReader("data/colors.txt")) {
            assertThat(reader.getColumns()).containsExactlyInAnyOrder("Transform Color", "Code");
        }
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            data/hireling.txt, *SubType
            data/hireling.txt, Exp/Lvl
            data/hireling.txt, Dmg-Min
            data/superuniques.txt, TC(N)
            """)
    void getColumnsIncludesColumnsWithSpecialChars(String fileName, String column) {
        try (D2Data reader = createReader(fileName)) {
            assertThat(reader.getColumns()).contains(column);
        }
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            data/hireling.txt, Hireling, 0
            data/hireling.txt, hireling, 0
            data/hireling.txt, *SubType, 1
            data/hireling.txt, *sUbTyPe, 1
            data/hireling.txt, exp/lvl, 12
            data/hireling.txt, dmg-min, 23
            data/superuniques.txt, tc(n), 18
            """)
    void getColumnIndexIsCaseInsensitive(String fileName, String column, int index) {
        try (D2Data reader = createReader(fileName)) {
            assertThat(reader.getColumnIndex(column)).isEqualTo(index);
        }
    }

    @Test
    void getColumnIndexReturnsMinusOneForUnknownColumn() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThat(reader.getColumnIndex("I am not known")).isEqualTo(-1);
        }
    }

    @Test
    void rowGetWorks() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThat(reader.readRow().get("Hireling")).isEqualTo("Rogue Scout");
        }
    }

    @Test
    void rowGetThrowsOnUnknownColumn() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThatThrownBy(() -> reader.readRow().get("Who am I")).isInstanceOf(D2ReaderException.class);
        }
    }

    @Test
    void rowGetIntWorks() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThat(reader.readRow().getInt("Gold")).isEqualTo(100);
        }
    }

    @Test
    void rowGetIntThrowsOnNonIntegerColumn() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThatThrownBy(() -> reader.readRow().getInt("Hireling")).isInstanceOf(NumberFormatException.class);
        }
    }

    @Test
    void rowGetIntThrowsOnUnknownColumn() {
        try (D2Data reader = createReader("data/hireling.txt")) {
            assertThatThrownBy(() -> reader.readRow().getInt("Who am I")).isInstanceOf(D2ReaderException.class);
        }
    }
}
