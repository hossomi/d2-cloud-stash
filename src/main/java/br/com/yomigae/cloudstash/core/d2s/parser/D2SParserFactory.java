package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.parser.v99.*;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class D2SParserFactory {

    private final static D2SParserFactory INSTANCE = new D2SParserFactory();

    public static D2SParserFactory instance() {
        return INSTANCE;
    }

    public D2SParser createParser(D2BinaryReader reader) {
        int version = reader.setByteIndex(0x04).readInt();
        reader.setByteIndex(0);
        return switch (version) {
            case 99 -> D2SParser.builder()
                    .headerParser(new V99HeaderParser())
                    .actsParser(new V99ActsParser())
                    .attributesParser(new V99AttributesParser())
                    .skillsParser(new V99SkillsParser())
                    .itemsParser(new V99ItemsParser())
                    .build();
            default -> throw new D2ParserException("Unknown version: " + version);
        };
    }
}
