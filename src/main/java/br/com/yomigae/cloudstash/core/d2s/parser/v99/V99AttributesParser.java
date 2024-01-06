package br.com.yomigae.cloudstash.core.d2s.parser.v99;

import br.com.yomigae.cloudstash.core.d2s.model.Attribute;
import br.com.yomigae.cloudstash.core.d2s.parser.AttributesParser;
import br.com.yomigae.cloudstash.core.d2s.parser.ParserUtils;
import br.com.yomigae.cloudstash.core.d2s.parser.ParserUtils.SaveFileAttribute;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.Map;
import java.util.stream.LongStream;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class V99AttributesParser implements AttributesParser {

    @Override
    public Map<Attribute, Integer> parse(D2BinaryReader reader) {
        reader.find("gf".getBytes()).skipBytes(2);
        return LongStream.generate(() -> reader.read(9))
                .takeWhile(id -> id != 0x1ff)
                .filter(id -> id != 0x1ff)
                .mapToInt(i -> (int) (0xff & i))
                .mapToObj(ParserUtils::saveFileAttribute)
                .map(key -> entry(key, (int) reader.read(key.bits())))
                .filter(key -> key.getKey().attribute() != null)
                .collect(toMap(
                        e -> e.getKey().attribute(),
                        e -> e.getValue() / e.getKey().factor()));
    }
}
