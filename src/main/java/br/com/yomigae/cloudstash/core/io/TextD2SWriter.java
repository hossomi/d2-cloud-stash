package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.d2s.model.Attribute;
import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.d2s.model.Skill;
import br.com.yomigae.cloudstash.core.d2s.model.Tuples;
import br.com.yomigae.cloudstash.core.d2s.model.hireling.Hireling;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import static br.com.yomigae.cloudstash.core.d2s.model.Tuples.Alternate.Selection.PRIMARY;
import static br.com.yomigae.cloudstash.core.d2s.model.Tuples.Alternate.Selection.SECONDARY;
import static br.com.yomigae.cloudstash.core.io.Summary.Columns.Width.flex;
import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;
import static java.lang.Math.floorDiv;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;

public class TextD2SWriter implements D2SWriter {

    @Override
    public void write(D2S d2s, OutputStream output) throws IOException {
        Summary summary = new Summary(new OutputStreamWriter(output), 60).h1();

        writeHeader(d2s, summary);
        writeControls(d2s, summary);
        writeSkills(d2s, summary);
        writeAttributes(d2s, summary);
        writeHireling(d2s, summary);
        writeActs(d2s, summary);

        summary.flush();
    }

    private void writeHeader(D2S d2s, Summary summary) {
        try (var cols = summary.columns(flex(3), flex(1))) {
            cols.get(0)
                    .line(d2s.name().toUpperCase())
                    .line(format("Level %d %s", d2s.level(), d2s.type()))
                    .line(format("Last played: %s", d2s.lastPlayed()));
            cols.get(1)
                    .line(checkbox("Expansion", d2s.expansion()))
                    .line(checkbox("Ladder", d2s.ladder()));
        }


        if (d2s.hardcore() || d2s.dead()) { summary.line(); }
        if (d2s.hardcore()) { summary.w1(" HARDCORE "); }
        if (d2s.dead()) { summary.w2("   DEAD   "); }
        summary.h1().line();
    }

    private void writeControls(D2S d2s, Summary summary) {
        Tuples.Alternate<Tuples.Dual<Skill>> mouse = d2s.mouseSkill();
        List<D2S.SkillHotkey> hotkeys = d2s.skillHotkeys();

        try (var cols = summary.columns(2)) {
            cols.get(0)
                    .h2(format(d2s.mouseSkill().active() == PRIMARY ? " %s " : "[ %s ]", PRIMARY))
                    .line(format("%s / %s", mouse.primary().left(), mouse.primary().right()));
            cols.get(1)
                    .h2(format(d2s.mouseSkill().active() == SECONDARY ? " %s " : "[ %s ]", SECONDARY))
                    .line(format("%s / %s", mouse.secondary().left(), mouse.secondary().right()));
        }

        summary.line().h2(" Skill hotkeys ");
        try (var cols = summary.columns(2)) {
            range(0, 8).forEach(i -> {
                D2S.SkillHotkey hk = hotkeys.get(i);
                cols.get(0).line(hk != null
                        ? format("%02d. [%s] %s", i, hk.left() ? "L" : "R", hk.skill())
                        : "-");
            });
            range(8, 16).forEach(i -> {
                D2S.SkillHotkey hk = hotkeys.get(i);
                cols.get(1).line(hk != null
                        ? format("%02d. [%s] %s", i, hk.left() ? "L" : "R", hk.skill())
                        : "-");
            });
        }
        summary.line();
    }

    private void writeSkills(D2S d2s, Summary summary) {
        summary.h1("[ Skills ]").line();
        try (var cols = summary.columns(3)) {
            List<Skill> skills = Skill.forClass(d2s.type());
            int rows = skills.size() / cols.size();
            for (int i = 0; i < skills.size(); i++) {
                Skill skill = skills.get(i);
                cols.get(floorDiv(i, rows)).line(format("[%-2d] %s", d2s.skills().get(skill), skill));
            }
        }
        summary.line();
    }

    private void writeAttributes(D2S d2s, Summary summary) {
        summary.h1("[ Attributes ]").line();
        try (var cols = summary.columns(2)) {
            int rows = Attribute.values().length / cols.size();
            for (int i = 0; i < Attribute.values().length; i++) {
                Attribute attribute = Attribute.values()[i];
                cols.get(floorDiv(i, rows)).line(format(
                        "[%-3d] %s",
                        d2s.attributes().getOrDefault(attribute, 0),
                        attribute.label()));
            }
        }
        summary.line();
    }

    private void writeHireling(D2S d2s, Summary summary) {
        Hireling hireling = d2s.hireling();
        if (hireling == null) { return; }
        summary.h1("[ Hireling ]").line();
        try (var cols = summary.columns(flex(3), flex(1))) {
            cols.get(0)
                    .line(hireling.name().toUpperCase())
                    .line(format("Level %d %s", hireling.level(), hireling.type().klass()));
            cols.get(1)
                    .line(checkbox("Dead", hireling.dead()));
        }
        summary.line();
    }

    private void writeActs(D2S d2s, Summary summary) {
        d2s.progress().forEach((difficulty, acts) -> {
            summary.h1(format("[ %s ]", difficulty.toString()));
            acts.all().forEach(act -> {
                summary.line().h2(format("[ Act %s ]", act.act().number() + 1));
                try (var cols = summary.columns(2)) {
                    act.quests().all().forEach((quest, questStatus) -> cols.get(0)
                            .line(checkbox(quest.name(), questStatus.completed())));
                    act.waypoints().all().forEach((waypoint, active) -> cols.get(1)
                            .line(checkbox(waypoint.label(), active)));
                }
            });
            summary.line();
        });
    }
}
