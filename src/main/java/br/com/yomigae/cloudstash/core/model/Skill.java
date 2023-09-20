package br.com.yomigae.cloudstash.core.model;

import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.io.D2Strings;
import br.com.yomigae.cloudstash.core.parser.D2DataException;
import lombok.Builder;

import java.util.List;
import java.util.Map;

import static br.com.yomigae.cloudstash.core.util.ValidationUtil.throwOnNull;
import static com.google.common.collect.Maps.uniqueIndex;
import static java.util.stream.Collectors.toMap;

@Builder
public record Skill(int id, String name) {

    public static final List<Skill> SKILLS;

    static {
        Map<String, String> skillNames = D2Data
                .readTableFile("/data/skilldesc.txt")
                .collect(toMap(
                        row -> row.get("skilldesc"),
                        row -> row.get("str name")));

        SKILLS = D2Data.readTableFile("/data/skills.txt")
                .filter(row -> D2Strings.contains(skillNames.get(row.get("skilldesc"))))
                .map(row -> Skill.builder()
                        .id(row.getInt("*Id"))
                        .name(D2Strings.get(skillNames.get(row.get("skilldesc"))))
                        .build())
                .toList();
    }

    private static final Map<Integer, Skill> SKILLS_BY_ID = uniqueIndex(SKILLS, Skill::id);

    public static Skill fromId(int id) {
        return throwOnNull(SKILLS_BY_ID.get(id), () -> new D2DataException("Unknown skill ID: " + id));
    }

    public static Skill fromName(String name) {
        return SKILLS.stream()
                .filter(s -> s.name().equals(name))
                .findAny()
                .orElseThrow(() -> new D2DataException("Unknown skill name: " + name));
    }

    @Override
    public String toString() {
        return name;
    }
}
