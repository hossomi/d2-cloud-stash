package br.com.yomigae.cloudstash.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.base.Strings.padStart;
import static com.google.common.collect.Maps.filterValues;
import static com.google.common.collect.Maps.transformValues;
import static com.google.common.collect.Streams.stream;
import static java.util.Comparator.comparing;
import static java.util.Map.entry;
import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.*;

@AllArgsConstructor
public class Huffman {

    sealed private interface Node extends Comparable<Node> {
        Comparator<Node> COMPARATOR = comparing(Node::weight).thenComparing(Node::label);
        Empty EMPTY = new Empty();

        int weight();

        String label();

        @Override
        default int compareTo(Node other) {
            return COMPARATOR.compare(this, other);
        }
    }

    private record Mid(int weight, Node left, Node right) implements Node {

        @Override
        public String label() {
            return left.label() + right.label();
        }

        @Override
        public String toString() {
            return "[%s, %s]".formatted(left, right);
        }
    }

    private record Leaf(int weight, char symbol) implements Node {

        @Override
        public String label() {
            return String.valueOf(symbol);
        }

        @Override
        public String toString() {
            return String.valueOf(symbol);
        }
    }

    public record Empty() implements Node {

        @Override
        public int weight() {
            return 0;
        }

        @Override
        public String label() {
            return "";
        }
    }

    public record Code(long value, int length) {
        public String toBinaryString() {
            return padStart(Long.toBinaryString(value), length, '0');
        }

        @Override
        public String toString() {
            return "%d:%s".formatted(length, toBinaryString());
        }
    }

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<Map<Character, Code>> CODES_MAP = new TypeReference<>() { };

    private final Node root;
    @Getter
    private final Map<Character, Code> codes;

    public static Huffman fromString(String string) {
        Map<Character, Integer> values = IntStream.range(0, string.length())
                .mapToObj(string::charAt)
                .collect(groupingBy(c -> c, reducing(0, c -> 1, Integer::sum)));
        return fromWeights(values);
    }

    public static Huffman fromWeights(Map<Character, Integer> values) {
        PriorityQueue<Node> queue = values.entrySet().stream().collect(
                PriorityQueue::new,
                (q, e) -> q.add(new Leaf(e.getValue(), e.getKey())),
                PriorityQueue::addAll);

        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            queue.add(new Mid(left.weight() + right.weight(), left, right));
        }
        return new Huffman(queue.poll());
    }

    public static Huffman fromCodes(InputStream input) throws IOException {
        return fromCodes(JSON.readValue(input, CODES_MAP));
    }

    public static Huffman fromCodes(Map<Character, Code> codes) {
        return new Huffman(codes);
    }

    private Huffman(Node root) {
        this.root = requireNonNullElse(root, Node.EMPTY);
        this.codes = buildCodes(this.root, 0, 0).stream().collect(toMap(
                Map.Entry::getKey,
                Map.Entry::getValue));
    }

    private Huffman(Map<Character, Code> codes) {
        this.codes = codes;
        this.root = buildTree(transformValues(codes, Code::toBinaryString));
    }

    private static Set<Map.Entry<Character, Code>> buildCodes(Node node, long value, int length) {
        return switch (node) {
            case Leaf(int weight, char symbol) -> Set.of(entry(symbol, new Code(value, length)));
            case Mid(int weight, Node left, Node right) -> Sets.union(
                    buildCodes(left, value << 1, length + 1),
                    buildCodes(right, (value << 1) + 1, length + 1));
            default -> Set.of();
        };
    }

    private static Node buildTree(Map<Character, String> codes) {
        return codes.size() > 1
                ? new Mid(0,
                buildTree(transformValues(filterValues(codes, v -> v.charAt(0) == '0'), v -> v.substring(1))),
                buildTree(transformValues(filterValues(codes, v1 -> v1.charAt(0) == '1'), v1 -> v1.substring(1))))
                : new Leaf(0, codes.keySet().iterator().next());
    }

    public Code encode(String source) {
        return IntStream.range(0, source.length())
                .mapToObj(source::charAt)
                .map(c -> Optional.ofNullable(codes.get(c))
                        .orElseThrow(() -> new IllegalArgumentException("Unknown symbol: " + c)))
                .reduce(new Code(0, 0), (out, c) -> new Code(
                        (out.value << c.length) + c.value,
                        out.length + c.length));
    }

    public String decode(long value, int length) {
        if (length == 0 || root instanceof Empty) {
            return "";
        }
        if (root instanceof Leaf l) {
            return String.valueOf(l.symbol).repeat(length);
        }

        Mid node = (Mid) root;
        StringBuilder decoded = new StringBuilder();
        for (long bit = 0x1L << length - 1; bit != 0; bit >>= 1) {
            Node next = (value & bit) == 0 ? node.left : node.right;
            switch (next) {
                case Leaf l -> {
                    decoded.append(l.symbol);
                    node = (Mid) root;
                }
                case Mid m -> node = m;
                default -> { }
            }
        }
        return decoded.toString();
    }

    public Stream<Character> decode(LongStream bits) {
        return decode(bits.iterator());
    }

    public Stream<Character> decode(PrimitiveIterator.OfLong bits) {
        return stream(new HuffmanDecodingIterator(root, bits));
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @AllArgsConstructor
    private static class HuffmanDecodingIterator implements Iterator<Character> {

        private final Node root;
        private final PrimitiveIterator.OfLong bits;

        @Override
        public boolean hasNext() {
            return !(root instanceof Empty) && bits.hasNext();
        }

        @Override
        public Character next() {
            Node node = root;
            while (node instanceof Mid m && bits.hasNext()) {
                node = bits.next() == 0 ? m.left : m.right;
            }

            return node instanceof Leaf l
                    ? l.symbol
                    : '\0';
        }
    }
}
