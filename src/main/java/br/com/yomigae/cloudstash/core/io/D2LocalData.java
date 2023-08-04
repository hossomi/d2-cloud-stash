package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.parser.D2DataException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

public record D2LocalData(
        Map<Integer, String> byId,
        Map<String, String> byKey) {

    public D2LocalData() {
        this(new HashMap<>(), new HashMap<>());
    }

    public void put(int id, String key, String value) {
        byId.put(id, value);
        byKey.put(key, value);
    }

    public String get(int id) {
        String value = byId.get(id);
        if (value == null) {
            throw new D2DataException("Unknown data ID: " + id);
        }
        return value;
    }

    public String get(String key) {
        String value = byKey.get(key);
        if (value == null) {
            throw new D2DataException("Unknown data key: " + key);
        }
        return value;
    }
}
