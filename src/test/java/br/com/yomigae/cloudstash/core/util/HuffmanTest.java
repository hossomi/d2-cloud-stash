package br.com.yomigae.cloudstash.core.util;

import br.com.yomigae.cloudstash.core.ExtendedSoftAssertions;
import br.com.yomigae.cloudstash.core.util.Huffman.Code;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.Streams.stream;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.StringUtils.isBlank;

class HuffmanTest {

    @ParameterizedTest
    @MethodSource("cases")
    void fromStringWorks(String file) {
        Case testCase = Case.from(file);
        testCase.test(Huffman.fromString(testCase.source));
    }

    @ParameterizedTest
    @MethodSource("cases")
    void fromWeightsWorks(String file) {
        Case testCase = Case.from(file);
        testCase.test(Huffman.fromWeights(testCase.weights));
    }

    @ParameterizedTest
    @MethodSource("cases")
    void fromCodesWorks(String file) {
        Case testCase = Case.from(file);
        testCase.test(Huffman.fromCodes(testCase.codes));
    }

    @Test
    void singleSymbolWorks() {
        Huffman tree = Huffman.fromString("a");
        assertThat(tree.encode("a")).isEqualTo(new Code(0, 0));
        assertThat(tree.decode(0, 5)).isEqualTo("aaaaa");
    }

    @Test
    void noSymbolWorks() {
        Huffman tree = Huffman.fromString("");
        assertThat(tree.encode("")).isEqualTo(new Code(0, 0));
        assertThat(tree.decode(0, 5)).isEqualTo("");
    }

    static Stream<String> cases() throws URISyntaxException {
        URL resource = requireNonNull(HuffmanTest.class.getResource("/huffman"), "Huffman sample folder not found");
        File root = new File(resource.toURI());
        if (!root.isDirectory()) {
            throw new IllegalArgumentException("Huffman sample folder is not a folder");
        }
        return Stream.of(requireNonNull(root.list()))
                .sorted((a, b) -> a.length() - b.length() != 0 ? a.length() - b.length() : a.compareTo(b))
                .map("/huffman/%s"::formatted);
    }

    private record Case(
            String source,
            Map<Character, Integer> weights,
            Map<Character, Code> codes,
            Map<String, String> samples) {

        public Case(String source) {
            this(source, new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        public static Case from(String file) {
            InputStream input = requireNonNull(
                    HuffmanTest.class.getResourceAsStream(file),
                    "File %s not found".formatted(file));
            Iterator<String> lines = new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .filter(line -> !isBlank(line))
                    .skip(1) // Case description
                    .iterator();

            Case test = new Case(lines.next());
            Set<Character> symbols = test.source.chars()
                    .mapToObj(i -> (char) i)
                    .collect(toSet());

            stream(lines)
                    .takeWhile(line -> !line.startsWith("#")) // Samples separator
                    .forEach(line -> {
                        var tokens = line.split(" +");
                        if (tokens.length != 3) {
                            throw new IllegalArgumentException(String.format(
                                    "Invalid test case: symbol table row must have 3 columns; got %s",
                                    line));
                        }

                        String symbolColumn = tokens[0];
                        if (symbolColumn.length() != 1) {
                            throw new IllegalArgumentException(String.format(
                                    "Invalid test case: symbol must be a single char; got %s",
                                    symbolColumn));
                        }

                        char symbol = symbolColumn.charAt(0);
                        String weight = tokens[1];
                        String code = tokens[2];
                        test.weights.put(symbol, parseInt(weight));
                        test.codes.put(symbol, new Code(parseLong(code, 2), code.length()));
                    });

            if (!test.weights.keySet().equals(symbols)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid test case: source string and symbol table don't match.\nSource symbols: %s\nTable symbols:  %s",
                        symbols, test.weights.keySet()));
            }

            stream(lines)
                    .map(line -> line.split(" +"))
                    .forEach(tokens -> test.samples.put(tokens[0], tokens[1]));

            return test;
        }

        public void test(Huffman tree) {
            assertThat(tree.codes()).as("Huffman codes").isEqualTo(codes);
            ExtendedSoftAssertions a = new ExtendedSoftAssertions();
            samples.forEach((decoded, encodedString) -> {
                Code code = tree.encode(decoded);
                long encoded = parseLong(encodedString, 2);
                a.assertThatBinary(code.value())
                        .as("%s code value", decoded)
                        .isEqualTo(encoded);
                a.assertThat(code.length())
                        .as("%s code length", decoded)
                        .isEqualTo(encodedString.length());
                a.assertThat(tree.decode(encoded, encodedString.length()))
                        .as("Decoding %s", encodedString)
                        .isEqualTo(decoded);
            });
            a.assertAll();
        }
    }
}
