package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.model.Skill;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Map;

public interface SkillsParser {
    Map<Skill, Integer> parse(D2BinaryReader reader, CharacterClass clazz);
}
