package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.util.StringUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.io.Summary.Columns.Width.*;
import static com.google.common.base.Strings.padEnd;
import static java.lang.Math.floorDiv;
import static java.lang.String.format;
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
        if (!text.isEmpty() && text.charAt(text.length() - 1) == '\n') {
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
        return line(new StringUtils.Padder(label.length() <= width ? label : "")
                .both('=')
                .align(width, 0.1));
    }

    public Summary h2(String label) {
        return line(new StringUtils.Padder(label.length() <= width ? label : "")
                .both('-')
                .align(width, 0.1));
    }

    public Summary w1(String label) {
        return line(new StringUtils.Padder(label.length() <= width ? label : "")
                .left('/')
                .right('\\').align(width, 0.5));
    }

    public Summary w2(String label) {
        return line(new StringUtils.Padder(label.length() <= width ? label : "")
                .left('\\')
                .right('/').align(width, 0.5));
    }

    public Summary flush() throws IOException {
        for (var row = poll(); row.isPresent(); row = poll()) {
            writer.write(row.get());
            writer.write('\n');
        }
        writer.flush();
        return this;
    }

    private Optional<String> poll() {
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

    public Columns columns(Columns.Width... widths) {
        return columns(List.of(widths));
    }

    public Columns columns(List<Columns.Width> widths) {
        int totalSeparatorWidth = separator.length() * (widths.size() - 1);
        int totalWidth = width - totalSeparatorWidth;

        int totalFlexWidth = totalWidth - widths.stream()
                .mapToInt(w -> w.of(width))
                .sum();
        List<Columns.Width> flexColumns = widths.stream()
                .filter(w -> w instanceof Columns.Flexible)
                .toList();
        int flexSum = flexColumns.stream()
                .mapToInt(Columns.Width::value)
                .sum();

        // Keep track of flex columns to adjust the last one
        int countFlex = 0;
        int remainingFlexWidth = totalFlexWidth;

        List<Integer> actualWidths = new ArrayList<>(widths.size());
        for (Columns.Width width : widths) {
            if (width instanceof Columns.Flexible flexible) {
                if (++countFlex < flexColumns.size()) {
                    // Not last flex, calculate and track
                    int flexWidth = floorDiv(totalFlexWidth * flexible.value(), flexSum);
                    actualWidths.add(flexWidth);
                    remainingFlexWidth -= flexWidth;
                } else {
                    // Last flex, add all remaining width
                    actualWidths.add(remainingFlexWidth);
                    remainingFlexWidth = 0;
                }
            } else {
                actualWidths.add(width.of(totalWidth));
            }
        }

        if (actualWidths.stream().mapToInt(Integer::intValue).sum() > totalWidth) {
            throw new IllegalArgumentException(format("""
                            Invalid column widths!
                            Available width: %d - %d (separators) = %d
                            Columns: %s""",
                    width, totalSeparatorWidth, totalWidth, IntStream.range(0, widths.size())
                            .mapToObj(i -> format("%s(%d) = %d",
                                    Columns.Width.name(widths.get(i)),
                                    widths.get(i).value(),
                                    actualWidths.get(i)))
                            .collect(joining(", "))));
        }

        return new Columns(actualWidths);
    }

    public class Columns implements AutoCloseable, Iterable<Summary> {
        private final List<Summary> columns;

        public Columns(List<Integer> widths) {
            this.columns = widths.stream()
                    .map(width -> (Summary) new Summary(writer, width, separator) {
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
                String line = columns.stream()
                        .map(col -> {
                            String cell = col.poll().orElse(" ".repeat(col.width));
                            return cell;
                        })
                        .collect(joining(separator));
                Summary.this.line(line);
            }
        }

        @Override
        public void close() throws Exception {
            flush();
        }

        @Override
        public Iterator<Summary> iterator() {
            return columns.iterator();
        }

        public sealed interface Width {
            int value();

            int of(int total);

            static Fixed fix(int value) { return new Fixed(value); }

            static Relative rel(int value) { return new Relative(value); }

            static Flexible flex(int value) { return new Flexible(value); }

            static String name(Width width) {
                return switch (width) {
                    case Fixed x -> "fix";
                    case Relative x -> "rel";
                    case Flexible x -> "flex";
                };
            }
        }

        public record Fixed(int value) implements Width {
            public int of(int total) {
                return value;
            }
        }

        public record Relative(int value) implements Width {
            public int of(int total) {
                return floorDiv(total * value, 100);
            }
        }

        public record Flexible(int value) implements Width {
            public int of(int total) {
                return 0;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        var writer = new Summary(new OutputStreamWriter(System.out), 100);
        writer.h1(" Hello world ");
        writer.write("This is an incomplete line before columns");
        try (var parent = writer.columns(fix(20), flex(1), flex(2), rel(50))) {
            parent.columns.forEach(col -> col.h2(String.valueOf(col.width)));
            try (var child = parent.get(0).columns(flex(1), flex(1))) {
                child.columns.forEach(col -> col.h2(String.valueOf(col.width)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        writer.flush();
    }
}
