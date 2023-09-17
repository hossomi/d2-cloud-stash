package br.com.yomigae.cloudstash.core.util;

import lombok.Builder;
import lombok.Singular;

import java.util.Map;
import java.util.TreeMap;

@Builder
public record BreakpointMap<V>(@Singular Map<Integer, V> breakpoints) {

    public BreakpointMap(Map<Integer, V> breakpoints) {
        this.breakpoints = new TreeMap<>(Integer::compareTo);
        this.breakpoints.putAll(breakpoints);
    }

    public BreakpointMap() {
        this(Map.of());
    }

    public BreakpointMap<V> put(int bp, V value) {
        this.breakpoints.put(bp, value);
        return this;
    }

    public BreakpointMap<V> putAll(BreakpointMap<V> other) {
        breakpoints.putAll(other.breakpoints);
        return this;
    }

    public V get(int k) {
        return breakpoints.get(breakpoint(k));
    }

    /**
     * Get the highest breakpoint lower than given key.
     */
    public int breakpoint(int k) {
        return breakpoints.keySet().stream()
                // First condition selects the higher breakpoint lower than k
                // Second condition filters out too-high breakpoints if it started too high
                .reduce((result, bp) -> (bp > result && bp <= k) || (result > k && bp < result) ? bp : result)
                .orElseThrow(() -> new IllegalStateException("No breakpoint registered"));
    }
}
