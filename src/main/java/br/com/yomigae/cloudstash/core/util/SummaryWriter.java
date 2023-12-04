package br.com.yomigae.cloudstash.core.util;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.padEnd;
import static java.lang.Math.floorDiv;

@AllArgsConstructor
public class SummaryWriter {

    private final Writer writer;
    private final int width;

    public void h1(String label) throws IOException {
        writer.write(new StringUtils.Padder(label).both('=').align(width, 0.1));
        writer.write('\n');
    }

    public void h2(String label) throws IOException {
        writer.write(new StringUtils.Padder(label).both('-').align(width, 0.1));
        writer.write('\n');
    }

    public void w1(String label) throws IOException {
        writer.write(new StringUtils.Padder(label).left('/').right('\\').align(width, 0.5));
        writer.write('\n');
    }

    public void w2(String label) throws IOException {
        writer.write(new StringUtils.Padder(label).left('\\').right('/').align(width, 0.5));
        writer.write('\n');
    }

    public Columns columns(int... widths) {
        int sum = IntStream.of(widths).sum();
        int width = this.width - 2 * (widths.length - 1);
        int remainingWidth = width;

        int[] finalWidths = new int[widths.length];
        for (int i = 0; i < widths.length - 1; i++) {
            finalWidths[i] = floorDiv(width * widths[i], sum);
            remainingWidth -= finalWidths[i];
        }
        finalWidths[finalWidths.length - 1] = remainingWidth;
        return new Columns(finalWidths);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    @AllArgsConstructor
    public class Columns implements AutoCloseable {

        private final int[] width;
        private final int[] offset;
        private final List<Queue<String>> columns;

        public Columns(int[] width) {
            this.width = width;
            this.offset = new int[width.length];
            this.offset[0] = 0;
            for (int i = 1; i < this.offset.length; i++) {
                this.offset[i] = this.offset[i - 1] + this.width[i - 1] + 1;
            }

            this.columns = IntStream.range(0, width.length)
                    .mapToObj(x -> (Queue<String>) new LinkedList<String>())
                    .toList();
        }

        public void write(String text, int col) throws IOException {
            columns.get(col).add(text);
            flush(false);
        }

        public void flush(boolean force) throws IOException {
            while (force
                    ? columns.stream().anyMatch(c -> !c.isEmpty())
                    : columns.stream().noneMatch(Collection::isEmpty)) {

                for (int i = 0; i < columns.size(); i++) {
                    String cell = columns.get(i).poll();
                    if (cell == null) {
                        cell = " ".repeat(width[i]);
                    } else if (cell.length() > width[i]) {
                        cell = cell.substring(0, width[i] - 3) + "...";
                    } else {
                        cell = padEnd(cell, width[i], ' ');
                    }
                    writer.write(cell);

                    if (i < columns.size() - 1) {
                        writer.write("  ");
                    }
                }
                writer.write('\n');
            }
        }

        @Override
        public void close() throws Exception {
            flush(true);
        }
    }
}
