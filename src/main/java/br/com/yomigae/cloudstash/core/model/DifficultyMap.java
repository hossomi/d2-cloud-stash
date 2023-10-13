package br.com.yomigae.cloudstash.core.model;

import java.util.EnumMap;
import java.util.Map;

import static br.com.yomigae.cloudstash.core.model.Difficulty.*;

public class DifficultyMap<V> extends EnumMap<Difficulty, V> {

    public DifficultyMap() {
        super(Difficulty.class);
    }

    public DifficultyMap(Map<Difficulty, V> source) {
        super(source);
    }

    public V normal() {
        return get(NORMAL);
    }

    public V nightmare() {
        return get(NIGHTMARE);
    }

    public V hell() {
        return get(HELL);
    }
}
