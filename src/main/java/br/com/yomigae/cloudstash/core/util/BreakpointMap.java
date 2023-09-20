package br.com.yomigae.cloudstash.core.util;

import java.util.*;

import static java.util.stream.Collectors.joining;

public record BreakpointMap<V>(NavigableMap<Integer, V> map) {

    public BreakpointMap(Map<Integer, V> map) {
        this(new TreeMap<>(Integer::compareTo));
        this.map.putAll(map);
    }

    public BreakpointMap() {
        this(Map.of());
    }

    public BreakpointMap<V> put(int bp, V value) {
        this.map.put(bp, value);
        return this;
    }

    public BreakpointMap<V> putAll(BreakpointMap<V> other) {
        map.putAll(other.map);
        return this;
    }

    public V get(int k) {
        return map.get(breakpoint(k));
    }

    /**
     * Get the highest breakpoint lower than given key.
     */
    public int breakpoint(int k) {
        return map.keySet().stream()
                // First condition selects the higher breakpoint lower than k
                // Second condition filters out too-high map if it started too high
                .reduce((result, bp) -> (bp > result && bp <= k) || (result > k && bp < result) ? bp : result)
                .orElseThrow(() -> new IllegalStateException("No breakpoints registered"));
    }

    public SortedSet<Integer> breakpoints() {
        return (SortedSet<Integer>) map.keySet();
    }

    public List<V> values() {
        return List.copyOf(map.values());
    }

    @Override
    public String toString() {
        return "[" + breakpoints().stream()
                .map(bp -> "%d: %s".formatted(bp, get(bp)))
                .collect(joining(", ")) + "]";
    }
}
