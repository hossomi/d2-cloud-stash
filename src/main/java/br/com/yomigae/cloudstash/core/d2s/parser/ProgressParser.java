package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.model.Difficulty;
import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Map;

public interface ProgressParser {
    Map<Difficulty, D2S.Progress> parse(D2BinaryReader reader);
}
