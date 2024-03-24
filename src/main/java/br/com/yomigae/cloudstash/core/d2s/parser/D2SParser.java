package br.com.yomigae.cloudstash.core.d2s.parser;

import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@Builder
@RequiredArgsConstructor
public class D2SParser {

    private final HeaderParser headerParser;
    private final ProgressParser progressParser;
    private final AttributesParser attributesParser;
    private final SkillsParser skillsParser;
    private final ItemsParser itemsParser;

    public D2S parse(InputStream inputStream) {
        try {
            return parse(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new D2ParserException("Error reading input stream", e);
        }
    }

    public D2S parse(byte[] data) {
        return parse(new D2BinaryReader(data));
    }

    public D2S parse(D2BinaryReader reader) {
        D2S.Builder d2s = headerParser.parse(reader);
        return d2s
                .progress(progressParser.parse(reader))
                .attributes(attributesParser.parse(reader))
                .skills(skillsParser.parse(reader, d2s.type()))
                .items(itemsParser.parse(reader))
                .build();
    }
}
