package br.com.yomigae.cloudstash.core.d2s.model.hireling;

import br.com.yomigae.cloudstash.core.d2s.model.Skill;
import br.com.yomigae.cloudstash.core.d2s.model.Attribute;
import br.com.yomigae.cloudstash.core.d2s.model.HasAttributes;
import br.com.yomigae.cloudstash.core.d2s.model.HasSkills;

import static br.com.yomigae.cloudstash.core.d2s.model.Attribute.EXPERIENCE_REQUIRED;
import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static br.com.yomigae.cloudstash.core.util.StringUtils.list;

public record Hireling(
        HirelingType type,
        int id,
        String name,
        boolean dead,
        int experience)
        implements HasAttributes, HasSkills {

    @lombok.Builder
    public Hireling(HirelingType type, int id, int nameId, boolean dead, int experience) {
        this(type, id, type.names().get(nameId), dead, experience);
    }

    public int level() {
        return type.attribute(EXPERIENCE_REQUIRED).unapply(experience);
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
        return new StringBuilder()
                .append(name)
                .append(" (level ").append(level()).append(" ").append(type.klass())
                .append(")\n").append(checkbox("Dead", dead))

                .append("\n\n").append(list("Skills", type.skills().keySet().stream()
                        .map(skill -> "(%d) %s".formatted(skillLevel(skill), skill))
                        .toList()))

                .append("\n\n").append(list("Attributes", type.attributes().keySet().stream()
                        .map(attribute -> "%s: %s".formatted(attribute.label(), attribute.format(attribute(attribute))))
                        .toList()))
                .toString();
    }
}
