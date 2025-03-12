package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.io.D2DataException;
import br.com.yomigae.cloudstash.core.io.D2Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.map;
import static br.com.yomigae.cloudstash.core.util.ValidationUtils.throwOnNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Multimaps.toMultimap;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Builder
public record Skill(
        int id,
        String name,
        CharacterClass characterClass,
        Meta meta,
        int requiredLevel
) implements Comparable<Skill> {

    public static final Map<String, Meta> SKILL_META;
    public static final List<Skill> SKILLS;
    private static final Comparator<Skill> COMPARATOR = Comparator
            .<Skill>comparingInt(s -> s.meta().row())
            .thenComparingInt(s -> s.meta().page())
            .thenComparingInt(s -> s.meta().col());


    static {
        SKILL_META = D2Data
                .readTableFile("/data/skilldesc.txt")
                .collect(toMap(
                        row -> row.get("skilldesc"),
                        row -> Meta.builder()
                                .nameKey(row.get("str name"))
                                .page(row.getInt("SkillPage"))
                                .row(row.getInt("SkillRow"))
                                .col(row.getInt("SkillColumn"))
                                .build()));

        SKILLS = D2Data.readTableFile("/data/skills.txt")
                .flatMap(row -> SkillRow.of(row).stream())
                .filter(row -> D2Strings.contains(row.meta().nameKey()))
                .map(row -> Skill.builder()
                        .id(row.row().getInt("*Id"))
                        .name(D2Strings.get(row.meta().nameKey()))
                        .characterClass(map(row.row().get("charclass"), c -> isNullOrEmpty(c)
                                ? null
                                : CharacterClass.from(c)))
                        .meta(row.meta())
                        .requiredLevel(row.row().getInt("reqlevel"))
                        .build())
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
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return name;
    }

    @lombok.Builder
    public record Meta(String nameKey, int page, int row, int col) {
        public int index() {
            return row * 3 + col;
        }

    }

    private record SkillRow(Meta meta, D2Data.Row row) {
        public static Optional<SkillRow> of(D2Data.Row row) {
            return Optional.ofNullable(SKILL_META.get(row.get("skilldesc")))
                    .map(meta -> new SkillRow(meta, row));
        }
    }
}
