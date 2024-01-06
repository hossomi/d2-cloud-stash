package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

public interface HeaderParser {

    int SIGNATURE = 0xaa55aa55;

    D2S.Builder parse(D2BinaryReader reader);
}
