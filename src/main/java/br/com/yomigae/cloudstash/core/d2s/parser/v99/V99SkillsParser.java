package br.com.yomigae.cloudstash.core.d2s.parser.v99;

import br.com.yomigae.cloudstash.core.d2s.model.Skill;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.d2s.parser.SkillsParser;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class V99SkillsParser implements SkillsParser {

    @Override
    public Map<Skill, Integer> parse(D2BinaryReader reader, CharacterClass clazz) {
        reader.find("if".getBytes()).skipBytes(2);
        return Skill.forClass(clazz).stream().collect(
                toMap(identity(),
                        skill -> (int) reader.readByte()));
    }
}
