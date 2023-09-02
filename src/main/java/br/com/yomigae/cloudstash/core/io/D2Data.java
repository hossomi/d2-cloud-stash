package br.com.yomigae.cloudstash.core.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.noop;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.format;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class D2Data {

    public static Stream<Row> readTableFile(String filename) {
        try (InputStream inputStream = D2Data.class.getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new D2ReaderException("Could not find file: " + filename);
            }
            return readTableFile(inputStream);
        } catch (IOException e) {
            throw new D2ReaderException("Error reading file: " + filename);
        }
    }

    private static Stream<Row> readTableFile(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String header = reader.readLine();
            if (header == null) {
                throw new D2ReaderException("End of file while reading table header");
            }
            List<String> headers = List.of(header.split("\t"));
            Map<String, Integer> columns = headers.stream().collect(
                    () -> new TreeMap<>(CASE_INSENSITIVE_ORDER),
                    (map, h) -> map.put(h, headers.indexOf(h)),
                    TreeMap::putAll);

            return reader.lines()
                    .map(line -> new Row(columns, line.split("\t")))
                    .filter(row -> !row.values[0].equalsIgnoreCase("Expansion"))
                    // Preemptively read all data because the reader will be closed.
                    .toList().stream();
        }
    }

    public record Row(Map<String, Integer> columns, String[] values) {

        public boolean has(String column) {
            return columns.containsKey(column);
        }

        public String get(String column) {
            Integer index = columns.get(column);
            if (index == null) {
                throw new D2ReaderException(format("Unknown column : %s", column));
            }
            return values[index];
        }

        public int getInt(String column) {
            return Integer.parseInt(get(column));
        }
    }
}
