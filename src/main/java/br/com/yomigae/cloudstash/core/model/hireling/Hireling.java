package br.com.yomigae.cloudstash.core.model.hireling;

import br.com.yomigae.cloudstash.core.model.Attribute;
import br.com.yomigae.cloudstash.core.model.HasAttributes;
import br.com.yomigae.cloudstash.core.model.HasSkills;
import br.com.yomigae.cloudstash.core.model.Skill;
import lombok.Builder;

import static br.com.yomigae.cloudstash.core.model.Attribute.EXPERIENCE_NEXT_LEVEL;

@Builder
public record Hireling(
        HirelingType type,
        String name,
        boolean dead,
        int experience)
        implements HasAttributes, HasSkills {

    public Hireling(boolean dead, int nameId, HirelingType type, int experience) {
        this(type, type.names().get(nameId), dead, experience);
    }

    public int level() {
        return type.attribute(EXPERIENCE_NEXT_LEVEL).unapply(experience);
    }

    @Override
    public int attribute(Attribute attribute) {
        return (int) type.attribute(attribute).apply(level());
    }

    @Override
    public int skillLevel(Skill skill) {
        return (int) type.skill(skill).apply(level());
    }

    @Override
    public String toString() {
        return "%s (level %d %s)".formatted(name, level(), type.klass());
    }
}
