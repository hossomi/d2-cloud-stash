package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.parser.D2DataException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Streams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.noop;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class D2Strings {
    private static final Map<String, String> VALUES = new HashMap<>();
    private static final BiMap<Integer, String> KEYS = HashBiMap.create();
    private static final JsonMapper JSON = new JsonMapper();

    static {
        Streams
                .concat(
                        readStringsFile("/data/local/skills.json"),
                        readStringsFile("/data/local/item-names.json"))
                .forEach(node -> put(
                        node.get("id").intValue(),
                        node.get("Key").textValue(),
                        node.get("enUS").textValue()));
    }

    public static Stream<ObjectNode> readStringsFile(String filename) {
        try (InputStream inputStream = D2Data.class.getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new D2ReaderException("Could not find file: " + filename);
            }

            JsonNode root = JSON.readTree(inputStream);
            if (!root.isArray()) {
                throw new D2ReaderException("Expected root object to be an array");
            }

            return stream(spliteratorUnknownSize(root.elements(), ORDERED), false)
                    .filter(node -> node instanceof ObjectNode)
                    .map(node -> (ObjectNode) node);
        } catch (IOException e) {
            throw new D2ReaderException("Error reading file: " + filename, e);
        }
    }

    private static void put(int id, String key, String value) {
        VALUES.put(key, value);
        KEYS.put(id, key);
    }

    public static boolean contains(int id) {
        return KEYS.containsKey(id);
    }

    public static boolean contains(String key) {
        return VALUES.containsKey(key);
    }

    public static String get(int id) {
        String key = KEYS.get(id);
        if (key == null) {
            throw new D2DataException("Unknown string ID: " + id);
        }
        return VALUES.get(key);
    }

    public static String get(String key) {
        String value = VALUES.get(key);
        if (value == null) {
            throw new D2DataException("Unknown string key: " + key);
        }
        return value;
    }
}
