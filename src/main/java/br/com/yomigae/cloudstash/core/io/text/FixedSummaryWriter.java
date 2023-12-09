package br.com.yomigae.cloudstash.core.io.text;

import br.com.yomigae.cloudstash.core.util.StringUtils;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.padEnd;
import static java.lang.Math.floorDiv;

@AllArgsConstructor
public abstract class FixedSummaryWriter<S extends FixedSummaryWriter<S>> implements SummaryWriter<S> {

    private final int width;

    public S h1(String label) throws IOException {
        return line(new StringUtils.Padder(label).both('=').align(width, 0.1));
    }

    public S h2(String label) throws IOException {
        return line(new StringUtils.Padder(label).both('-').align(width, 0.1));
    }

    public S w1(String label) throws IOException {
        return line(new StringUtils.Padder(label).left('/').right('\\').align(width, 0.5));
    }

    public S w2(String label) throws IOException {
        return line(new StringUtils.Padder(label).left('\\').right('/').align(width, 0.5));
    }

    public S line(String text) throws IOException {
        return write(text + '\n');
    }

    public Columns columns(int... ratios) {
        int sum = IntStream.of(ratios).sum();
        int width = this.width - 2 * (ratios.length - 1);
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

        private final int[] widths;
        private final List<Column> columns;

        public Columns(int[] widths) {
            this.widths = widths;
            this.columns = Arrays.stream(widths).mapToObj(Column::new).toList();
        }

        public Column get(int col) throws IOException {
            return columns.get(col);
        }

        public void flush(boolean force) throws IOException {
            while (force
                    ? columns.stream().anyMatch(c -> !c.isEmpty())
                    : columns.stream().noneMatch(Column::isEmpty)) {

                for (int i = 0; i < columns.size(); i++) {
                    String cell = columns.get(i).pop();
                    if (cell == null) {
                        cell = " ".repeat(widths[i]);
                    } else if (cell.length() > widths[i]) {
                        cell = cell.substring(0, widths[i] - 3) + "...";
                    } else {
                        cell = padEnd(cell, widths[i], ' ');
                    }
                    FixedSummaryWriter.this.write(cell);

                    if (i < columns.size() - 1) {
                        FixedSummaryWriter.this.write("  ");
                    }
                }
                FixedSummaryWriter.this.write("\n");
            }
        }

        @Override
        public void close() throws Exception {
            flush(true);
        }
    }

    public static class Column extends FixedSummaryWriter<Column> {
        private final Queue<String> rows = new LinkedList<>();
        private String current = "";

        public Column(int width) {
            super(width);
        }

        public boolean isEmpty() {
            return rows.isEmpty() && isNullOrEmpty(current);
        }

        public String pop() {
            if (rows.isEmpty()) {
                String value = current;
                current = "";
                return value;
            }
            return rows.poll();
        }

        @Override
        public Column write(String text) throws IOException {
            text = current + text;
            List<String> textRows = text.lines().toList();

            if (text.charAt(text.length() - 1) == '\n') {
                rows.addAll(textRows);
                current = "";
            } else {
                int lastIndex = textRows.size() - 1;
                rows.addAll(textRows.subList(0, lastIndex));
                current = textRows.get(lastIndex);
            }
            return this;
        }

        @Override
        public Column line(String text) throws IOException {
            write(text + "\n");
            return this;
        }
    }

    public static class OutWriter extends FixedSummaryWriter<OutWriter> {
        public OutWriter(int width) {
            super(width);
        }

        @Override
        public OutWriter write(String text) throws IOException {
            System.out.print(text);
            return this;
        }
    }

    public static void main(String[] args) throws IOException {
        var writer = new OutWriter(100);
        writer.h1(" Hello world ");
        try (var parent = writer.columns(1, 1)) {
            try (var child = parent.get(0).columns(1, 1)) {
                child.get(0).w1(" HELLO ");
                child.get(0).w2(" WORLD ");
                child.get(1).w1(" HELLO ");
                child.get(1).w2(" WORLD ");
            }
            parent.get(1).h2("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
