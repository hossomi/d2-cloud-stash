package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.io.D2DataException;
import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.io.D2Strings;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Builder;

import java.util.List;
import java.util.Map;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.map;
import static br.com.yomigae.cloudstash.core.util.ValidationUtils.throwOnNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Multimaps.toMultimap;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Builder
public record Skill(int id, String name, CharacterClass characterClass) implements Comparable<Skill> {

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
                        .characterClass(map(row.get("charclass"), c -> isNullOrEmpty(c)
                                ? null
                                : CharacterClass.from(c)))
                        .build())
                .sorted()
                .toList();
    }

    private static final Map<Integer, Skill> SKILLS_BY_ID = uniqueIndex(SKILLS, Skill::id);
    private static final ListMultimap<CharacterClass, Skill> SKILLS_BY_CLASS = SKILLS.stream()
            .filter(s -> s.characterClass != null)
            .collect(toMultimap(
                    skill -> skill.characterClass,
                    identity(),
                    () -> ArrayListMultimap.create(CharacterClass.values().length, 30)));

    public static Skill fromId(int id) {
        return throwOnNull(SKILLS_BY_ID.get(id), () -> new D2DataException("Unknown skill ID: " + id));
    }

    public static Skill fromName(String name) {
        return SKILLS.stream()
                .filter(s -> s.name().equals(name))
                .findAny()
                .orElseThrow(() -> new D2DataException("Unknown skill name: " + name));
    }

    public static List<Skill> forClass(CharacterClass clazz) {
        return SKILLS_BY_CLASS.get(clazz);
    }

    @Override
    public int compareTo(Skill o) {
        return id - o.id;
    }

    @Override
    public String toString() {
        return name;
    }
}
