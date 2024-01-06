package br.com.yomigae.cloudstash.core.d2s.parser.v99;

import br.com.yomigae.cloudstash.core.d2s.parser.ItemsParser;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Collection;
import java.util.List;

public class V99ItemsParser implements ItemsParser {

    @Override
    public Collection<Object> parse(D2BinaryReader reader) {
        return List.of();
    }
}
