package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Collection;

public interface ItemsParser {
    Collection<Object> parse(D2BinaryReader reader);
}
