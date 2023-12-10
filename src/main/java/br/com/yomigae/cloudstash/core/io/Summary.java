package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.util.StringUtils;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.padEnd;
import static java.lang.Math.floorDiv;
import static java.util.stream.Collectors.joining;

public class Summary {

    private final Writer writer;
    private final int width;
    private final String separator;

    private final Queue<String> rows = new LinkedList<>();
    private String tail = null;

    protected Summary(Writer writer, int width) {
        this(writer, width, "  ");
    }

    protected Summary(Writer writer, int width, String separator) {
        this.writer = writer;
        this.width = width;
        this.separator = separator;
    }

    Summary line() {
        return write("\n");
    }

    Summary line(String text) {
        return write(text).line();
    }

    Summary write(String text) {
        if (tail != null) {
            text = tail + text;
        }
        List<String> newRows = text.lines().toList();

        // String#lines will chop trailing newlines.
        // If the text ended with newline, all rows are completed, so add all of them.
        if (text.charAt(text.length() - 1) == '\n') {
            rows.addAll(newRows);
            tail = null;

        }
        // The final line of the text will be the next tail.
        else {
            int lastIndex = newRows.size() - 1;
            rows.addAll(newRows.subList(0, lastIndex));
            tail = newRows.get(lastIndex);
        }

        return this;
    }

    public Summary h1(String label) {
        return line(new StringUtils.Padder(label).both('=').align(width, 0.1));
    }

    public Summary h2(String label) {
        return line(new StringUtils.Padder(label).both('-').align(width, 0.1));
    }

    public Summary w1(String label) {
        return line(new StringUtils.Padder(label).left('/').right('\\').align(width, 0.5));
    }

    public Summary w2(String label) {
        return line(new StringUtils.Padder(label).left('\\').right('/').align(width, 0.5));
    }

    public Optional<String> poll() {
        if (rows.isEmpty() && tail != null) {
            rows.add(tail);
            tail = null;
        }
        return Optional.ofNullable(rows.poll()).map(value -> {
            if (value.length() > width) {
                return value.substring(0, width - 3) + "...";
            } else if (value.length() < width) {
                return padEnd(value, width, ' ');
            }
            return value;
        });
    }

    public Summary flush() throws IOException {
        for (var row = poll(); row.isPresent(); row = poll()) {
            writer.write(row.get());
            writer.write('\n');
        }
        writer.flush();
        return this;
    }

    public Columns columns(int... ratios) {
        int sum = IntStream.of(ratios).sum();
        int width = this.width - separator.length() * (ratios.length - 1);
        int remainingWidth = width;

        int[] widths = new int[ratios.length];
        for (int i = 0; i < ratios.length - 1; i++) {
            widths[i] = floorDiv(width * ratios[i], sum);
            remainingWidth -= widths[i];
        }
        widths[widths.length - 1] = remainingWidth;
        return new Columns(widths);
    }

    @AllArgsConstructor
    public class Columns implements AutoCloseable {

        private final List<Summary> columns;

        public Columns(int[] widths) {
            this.columns = Arrays.stream(widths)
                    .mapToObj(width -> (Summary) new Summary(writer, width, separator) {
                        @Override
                        public Summary flush() {
                            // Don't flush individual columns
                            return this;
                        }
                    })
                    .toList();
        }

        public Summary get(int col) throws IOException {
            return columns.get(col);
        }

        public void flush() {
            if (Summary.this.tail != null) {
                // If there's an incomplete line, complete it.
                Summary.this.line();
            }

            // Flush all columns row by row
            while (columns.stream().anyMatch(c -> !c.rows.isEmpty())) {
                Summary.this.line(columns.stream()
                        .map(col -> col.poll().orElse(" ".repeat(col.width)))
                        .collect(joining(separator)));
            }
        }

        @Override
        public void close() throws Exception {
            flush();
        }
    }

    public static void main(String[] args) throws IOException {
        var writer = new Summary(new OutputStreamWriter(System.out), 100);
        writer.h1(" Hello world ");
        writer.line("First line");
        try (var parent = writer.columns(1, 1)) {
            try (var child = parent.get(0).columns(1, 1)) {
                child.get(0).line("Line 1");
                child.get(0).h2("Line 2");
                child.get(1).w1("Line 3");
                child.get(1).line("Line 4");
            }
            parent.get(1).h2("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        writer.flush();
    }
}
