package br.com.yomigae.cloudstash.core.d2s.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Getter
@AllArgsConstructor
public enum Difficulty {
    NORMAL(0),
    NIGHTMARE(1),
    HELL(2);

    private final int index;

    public static Collection<Difficulty> all() {
        return List.of(values());
    }

    public static Difficulty fromIndex(int index) {
        return all().stream()
                .filter(difficulty -> difficulty.index() == index)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format(
                        "Invalid %s index: %d",
                        Difficulty.class.getSimpleName(), index)));
    }
}
