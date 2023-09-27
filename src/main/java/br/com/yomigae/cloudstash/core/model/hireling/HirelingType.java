package br.com.yomigae.cloudstash.core.model.hireling;

import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.io.D2Data.Row;
import br.com.yomigae.cloudstash.core.io.D2Strings;
import br.com.yomigae.cloudstash.core.model.Act;
import br.com.yomigae.cloudstash.core.model.Attribute;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import br.com.yomigae.cloudstash.core.model.Skill;
import br.com.yomigae.cloudstash.core.parser.D2DataException;
import br.com.yomigae.cloudstash.core.util.BreakpointMap;
import br.com.yomigae.cloudstash.core.util.Scaling;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static br.com.yomigae.cloudstash.core.model.Attribute.*;
import static br.com.yomigae.cloudstash.core.util.ValidationUtil.throwOnNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@Builder
public record HirelingType(
        int id,
        HirelingClass klass,
        Act act,
        boolean expansion,
        List<String> names, // Order is important: the character file contains the name index
        Map<Skill, Scaling> skills,
        @Singular Map<Attribute, Scaling> attributes) {

    private static final List<HirelingType> HIRELINGS;

    static {
        HIRELINGS = D2Data.readTableFile("/data/hireling.txt")
                // Expansion hirelings may have the same ID. Use version as well to group.
                .collect(groupingBy(r -> r.getInt("Id") + "/" + r.getInt("Version")))
                .values().stream()
                .map(rows -> {
                    Row sample = rows.get(0);
                    return HirelingType.builder()
                            .id(sample.getInt("Id"))
                            .klass(HirelingClass.fromName(sample.get("Hireling")))
                            .act(Act.from(
                                    Difficulty.fromIndex(sample.getInt("Difficulty") - 1),
                                    sample.getInt("Act") - 1))
                            .expansion(sample.getInt("Version") > 0)
                            .names(range(D2Strings.id(sample.get("NameFirst")), D2Strings.id(sample.get("NameLast")) + 1)
                                    .mapToObj(D2Strings::get)
                                    .toList())

                            .skills(range(1, 6)
                                    .filter(i -> !isNullOrEmpty(sample.get("Skill" + i)))
                                    .boxed()
                                    .collect(toMap(
                                            i -> Skill.fromName(sample.get("Skill" + i)),
                                            i -> toBreakpointScaling(rows, row -> new Scaling.Linear(
                                                    row.getInt("Level" + i),
                                                    row.getDouble("LvlPerLvl" + i) / 32)))))

                            .attribute(LIFE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("HP"),
                                    row.getInt("HP/Lvl"))))
                            .attribute(DEFENSE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("Defense"),
                                    row.getInt("Def/Lvl"))))
                            .attribute(STRENGTH, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("Str"),
                                    row.getInt("Str/Lvl"))))
                            .attribute(DEXTERITY, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("Dex"),
                                    row.getInt("Dex/Lvl"))))
                            .attribute(ATTACK_RATING, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("AR"),
                                    row.getInt("AR/Lvl"))))
                            .attribute(MIN_DAMAGE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("Dmg-Min"),
                                    row.getDouble("Dmg/Lvl") / 8)))
                            .attribute(MAX_DAMAGE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("Dmg-Max"),
                                    row.getDouble("Dmg/Lvl") / 8)))
                            .attribute(FIRE_RESISTANCE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("ResistFire"),
                                    row.getDouble("ResistFire/Lvl") / 8)))
                            .attribute(COLD_RESISTANCE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("ResistCold"),
                                    row.getDouble("ResistCold/Lvl") / 8)))
                            .attribute(LIGHTNING_RESISTANCE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("ResistLightning"),
                                    row.getDouble("ResistLightning/Lvl") / 8)))
                            .attribute(POISON_RESISTANCE, toBreakpointScaling(rows, row -> new Scaling.Linear(
                                    row.getInt("ResistPoison"),
                                    row.getDouble("ResistPoison/Lvl") / 8)))
                            .attribute(EXPERIENCE_NEXT_LEVEL, new Scaling.Breakpoint(toBreakpointMap(rows,
                                    row -> new Scaling.Experience(row.getInt("Level"), row.getInt("Exp/Lvl")))))
                            .build();
                })
                .toList();
    }

    private static <S extends Scaling> Scaling.Breakpoint toBreakpointScaling(List<Row> rows, Function<Row, S> scaling) {
        return new Scaling.Breakpoint(toBreakpointMap(rows, scaling));
    }

    private static <S extends Scaling> BreakpointMap<Scaling> toBreakpointMap(List<Row> rows, Function<Row, S> scaling) {
        return rows.stream().collect(
                BreakpointMap::new,
                (bp, row) -> bp.put(row.getInt("Level"), scaling.apply(row)),
                BreakpointMap::putAll);
    }

    public static HirelingType fromId(int id, boolean expansion) {
        return HIRELINGS.stream()
                .filter(hireling -> hireling.expansion == expansion)
                .filter(hireling -> hireling.id == id)
                .findAny()
                .orElseThrow(() -> new D2DataException(String.format(
                        "Unknown hireling ID: %d (%s)",
                        id, expansion ? "expansion" : "normal")));
    }

    public Scaling attribute(Attribute attribute) {
        return throwOnNull(attributes.get(attribute), () -> new D2DataException("Invalid attribute: " + attribute));
    }

    public Scaling skill(Skill skill) {
        return throwOnNull(skills.get(skill), () -> new D2DataException("Invalid skill: " + skill));
    }
}
