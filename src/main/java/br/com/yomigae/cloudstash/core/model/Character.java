package br.com.yomigae.cloudstash.core.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record Character(
        String name,
        CharacterClass klass,
        boolean ladder,
        boolean expansion,
        boolean hardcore,

        int level,
        Act currentAct,
        Instant lastPlayed,

        boolean dead,
        EquipmentSet activeEquipmentSet,
        Swap<Dual<Skill>> mouseSkill,
        List<Skill> skillHotkeys) {
}
