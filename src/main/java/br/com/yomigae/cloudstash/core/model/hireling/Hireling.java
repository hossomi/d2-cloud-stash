package br.com.yomigae.cloudstash.core.model.hireling;

import lombok.Builder;

@Builder
public record Hireling(
        String name,
        HirelingType type,

        int level,
        long experience,
        boolean dead) {

}
