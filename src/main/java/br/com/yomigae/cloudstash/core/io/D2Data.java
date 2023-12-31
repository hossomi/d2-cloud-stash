package br.com.yomigae.cloudstash.core.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.format;

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
            return !isNullOrEmpty(get(column));
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

        public double getDouble(String column) {
            return Double.parseDouble(get(column));
        }
    }
}
