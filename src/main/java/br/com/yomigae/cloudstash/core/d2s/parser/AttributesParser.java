package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.model.Attribute;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Map;

public interface AttributesParser {
    Map<Attribute, Integer> parse(D2BinaryReader reader);
}
