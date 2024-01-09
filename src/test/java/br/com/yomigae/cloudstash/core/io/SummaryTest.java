package br.com.yomigae.cloudstash.core.io;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.StringWriter;

import static br.com.yomigae.cloudstash.core.io.Summary.Columns.Width.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SummaryTest {

    @Test
    void writePadsRight() throws Exception {
        assertSummary(15,
                summary -> summary
                        .write("Hello")
                        .write(" world!"),
                """
                        Hello world!___
                        """);
    }

    @Test
    void writeTruncates() throws Exception {
        assertSummary(15,
                summary -> summary
                        .write("Hello world! I am too long"),
                """
                        Hello world!...
                        """);
    }

    @Test
    void lineWorks() throws Exception {
        assertSummary(15,
                summary -> summary
                        .line("Hello")
                        .line("World"),
                """
                        Hello__________
                        World__________
                        """);
    }

    @Test
    void lineWritesInCurrentRow() throws Exception {
        assertSummary(15,
                summary -> summary
                        .write("Hello")
                        .line(" world!")
                        .write("Another line!"),
                """
                        Hello world!___
                        Another line!__
                        """);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            50, ====[ Header ]====================================
            40, ===[ Header ]===========================
            30, ==[ Header ]==================
            20, =[ Header ]=========
            """)
    void h1JustifiesToTheLeftish(int width, String expected) throws Exception {
        assertSummary(width,
                summary -> summary.h1("[ Header ]"),
                expected + '\n');
    }

    @Test
    void h1IgnoresLabelOnOverflow() throws Exception {
        assertSummary(6,
                summary -> summary.h1("[ Header ]"),
                """
                        ======
                        """);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            50, ----[ Header ]------------------------------------
            40, ---[ Header ]---------------------------
            30, --[ Header ]------------------
            20, -[ Header ]---------
            """)
    void h2JustifiesToTheLeftish(int width, String expected) throws Exception {
        assertSummary(width,
                summary -> summary.h2("[ Header ]"),
                expected + '\n');
    }

    @Test
    void h2IgnoresLabelOnOverflow() throws Exception {
        assertSummary(6,
                summary -> summary.h2("[ Header ]"),
                """
                        ------
                        """);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            50, ////////////////////[ Header ]\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            49, ///////////////////[ Header ]\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            48, ///////////////////[ Header ]\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            47, //////////////////[ Header ]\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            """)
    void w1Centralizes(int width, String expected) throws Exception {
        assertSummary(width,
                summary -> summary.w1("[ Header ]"),
                expected + '\n');
    }

    @Test
    void w1IgnoresLabelOnOverflow() throws Exception {
        assertSummary(6,
                summary -> summary.w1("[ Header ]"),
                """
                        ///\\\\\\
                        """);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            50, \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\[ Header ]////////////////////
            49, \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\[ Header ]////////////////////
            48, \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\[ Header ]///////////////////
            47, \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\[ Header ]///////////////////
            """)
    void w2Centralizes(int width, String expected) throws Exception {
        assertSummary(width,
                summary -> summary.w2("[ Header ]"),
                expected + '\n');
    }

    @Test
    void w2IgnoresLabelOnOverflow() throws Exception {
        assertSummary(6,
                summary -> summary.w2("[ Header ]"),
                """
                        \\\\\\///
                        """);
    }

    @Test
    void columnsAddsNewLineAfterWrite() throws Exception {
        assertSummary(15,
                summary -> {
                    summary.write("Hello world!   ");
                    try (var cols = summary.columns(rel(100))) {
                        cols.get(0).line("Hello again!   ");
                    }
                },
                """
                        Hello world!___
                        Hello again!___
                        """);
    }

    @Test
    void fixedColumnsCoversFullWidth() throws Exception {
        assertSummary(20,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(fix(10), fix(4), fix(2))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =======
                        ----------  ----  --
                        """);
    }

    @Test
    void fixedColumnsCoversPartialWidth() throws Exception {
        assertSummary(20,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(fix(10), fix(4))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =======
                        ----------  ----____
                        """);
    }

    @Test
    void fixedColumnsOverflowThrows() throws Exception {
        assertThrows(20, summary -> summary.columns(fix(10), fix(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("""
                        Invalid column widths!
                        Available width: 20 - 2 (separators) = 18
                        Columns: fix(10) = 10, fix(10) = 10""");
    }

    @Test
    void flexColumnsAreProportional() throws Exception {
        assertSummary(22,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(flex(1), flex(2), flex(3))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =========
                        ---  ------  ---------
                        """);
    }

    @Test
    void flexColumnsRoundsCorrectly() throws Exception {
        assertSummary(20,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(flex(1), flex(2), flex(3))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =======
                        --  -----  ---------
                        """);
    }

    @Test
    void flexColumnsRespectFixedColumns() throws Exception {
        assertSummary(20,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(flex(1), fix(10), flex(2))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =======
                        --  ----------  ----
                        """);
    }

    @Test
    void flexColumnsRespectRelativeColumns() throws Exception {
        assertSummary(20,
                summary -> {
                    summary.h1("Hello world! ");
                    try (var cols = summary.columns(flex(1), rel(50), flex(2))) {
                        cols.forEach(col -> col.h2(""));
                    }
                },
                """
                        Hello world! =======
                        --  ----------  ----
                        """);
    }

    private void assertSummary(int width, ThrowingConsumer<Summary> setup, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        Summary summary = new Summary(writer, width);

        setup.accept(summary);
        summary.flush();

        System.out.println(writer);
        assertThat(writer.toString()).isEqualTo(expected.replace('_', ' '));
    }

    private AbstractThrowableAssert<?, ? extends Throwable> assertThrows(int width, ThrowingConsumer<Summary> setup) {
        StringWriter writer = new StringWriter();
        Summary summary = new Summary(writer, width);

        return assertThatThrownBy(() -> {
            setup.accept(summary);
            summary.flush();
            System.out.println(writer);
        });

    }

    private interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}
