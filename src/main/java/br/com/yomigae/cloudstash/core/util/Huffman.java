package br.com.yomigae.cloudstash.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.padStart;
import static java.lang.Long.toBinaryString;
import static java.util.Comparator.comparing;
import static java.util.Map.entry;
import static java.util.stream.Collectors.*;

public class Huffman {

    sealed private interface Node extends Comparable<Node> {
        Comparator<Node> COMPARATOR = comparing(Node::weight).thenComparing(Node::label);

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
        @Override
        public String toString() {
            return "%d:%s".formatted(length, padStart(toBinaryString(value), length, '0'));
        }
    }

    private static final ObjectMapper JSON = new ObjectMapper();
    private final Node root;
    @Getter
    private final Map<Character, Code> codes;

    public static Huffman from(Map<Character, Integer> values) {
        PriorityQueue<Node> queue = values.entrySet().stream().collect(
                PriorityQueue::new,
                (q, e) -> q.add(new Leaf(e.getValue(), e.getKey())),
                PriorityQueue::addAll);

        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            System.out.printf("Adding: %s + %s\n", left, right);
            queue.add(new Mid(left.weight() + right.weight(), left, right));
        }
        return new Huffman(queue.poll());
    }

    public static Huffman from(String string) {
        Map<Character, Integer> values = IntStream.range(0, string.length())
                .mapToObj(string::charAt)
                .collect(groupingBy(c -> c, reducing(0, c -> 1, Integer::sum)));
        return from(values);
    }

    public static Huffman fromJson(InputStream input) throws IOException {
        return new Huffman(fromJsonNode(JSON.readTree(input), 0));
    }

    private static Node fromJsonNode(JsonNode node, int level) {
        if (node instanceof ArrayNode array) {
            if (array.isEmpty()) {
                return new Empty();
            }
            if (array.size() == 2) {
                return new Mid(
                        0,
                        fromJsonNode(array.get(0), level + 1),
                        fromJsonNode(array.get(1), level + 1));
            }
            throw new IllegalArgumentException(String.format(
                    "Invalid Huffman tree at level %d: array of size %d (expected 0 or 2)",
                    level, array.size()));
        } else if (node instanceof TextNode text) {
            if (text.textValue().length() == 1) {
                return new Leaf(0, text.textValue().charAt(0));
            }
            throw new IllegalArgumentException(String.format(
                    "Invalid Huffman tree at level %d: %s (expected single character)",
                    level, text.textValue()));
        }
        throw new IllegalArgumentException(String.format(
                "Invalid Huffman tree at level %d: %s (expected array or string)",
                level, node.getNodeType()));
    }

    private Huffman(Node root) {
        this.root = root;
        this.codes = getCodes(root, 0, 0).stream().collect(toMap(
                Map.Entry::getKey,
                Map.Entry::getValue));
    }

    private Set<Map.Entry<Character, Code>> getCodes(Node node, long value, int length) {
        if (node == null) {
            return Set.of();
        }
        return switch (node) {
            case Leaf(int weight, char symbol) -> Set.of(entry(symbol, new Code(value, length)));
            case Mid(int weight, Node left, Node right) -> Sets.union(
                    getCodes(left, value << 1, length + 1),
                    getCodes(right, (value << 1) + 1, length + 1));
            default -> Set.of();
        };
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
        Mid node = (Mid) root;
        String out = "";
        long bit = 0x1L << length - 1;
        while (bit != 0) {
            Node next = (value & bit) == 0 ? node.left() : node.right();
            switch (next) {
                case Leaf l -> {
                    out += l.symbol;
                    node = (Mid) root;
                }
                case Mid m -> node = m;
                case null, default -> {
                }
            }
            bit >>= 1;
        }
        return out;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
