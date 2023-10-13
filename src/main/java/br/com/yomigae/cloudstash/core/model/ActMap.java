package br.com.yomigae.cloudstash.core.model;

import java.util.EnumMap;
import java.util.List;

import static br.com.yomigae.cloudstash.core.model.Difficulty.*;
import static com.google.common.collect.Maps.filterKeys;

public class ActMap<V> extends EnumMap<Act, V> {

    public ActMap() {
        super(Act.class);
    }

    public List<V> difficulty(Difficulty difficulty) {
        return List.copyOf(filterKeys(this, a -> a.difficulty() == difficulty).values());
    }

    public List<V> normal() {
        return difficulty(NORMAL);
    }

    public List<V> nightmare() {
        return difficulty(NIGHTMARE);
    }

    public List<V> hell() {
        return difficulty(HELL);
    }
}
