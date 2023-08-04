package br.com.yomigae.cloudstash.core.model;

import br.com.yomigae.cloudstash.core.io.D2LocalData;
import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.parser.D2DataException;
import lombok.Builder;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.uniqueIndex;
import static java.util.stream.Collectors.toMap;

@Builder
public record Skill(int id, String name) {

    public static final List<Skill> SKILLS;

    static {
        Map<String, String> skillNames = D2Data
                .readTableFile("/data/skilldesc.txt")
                .collect(toMap(row -> row.get("skilldesc"), row -> row.get("str name")));

        D2LocalData skillNameData = D2Data.readLocalFile("/data/local/skills.json");

        SKILLS = D2Data.readTableFile("/data/skills.txt")
                .map(row -> Skill.builder()
                        .id(row.getInt("*Id"))
                        .name(skillNameData.get(skillNames.get(row.get("skilldesc"))))
                        .build())
                .toList();
    }

    private static final Map<Integer, Skill> SKILLS_BY_ID = uniqueIndex(SKILLS, Skill::id);

    public static Skill fromId(int id) {
        Skill skill = SKILLS_BY_ID.get(id);
        if (skill == null) {
            throw new D2DataException("Unknown skill ID: " + id);
        }
        return skill;
    }
}