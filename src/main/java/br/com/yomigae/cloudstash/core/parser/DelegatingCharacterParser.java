package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.Character;
import br.com.yomigae.cloudstash.core.model.Character.CharacterBuilder;

import java.util.Map;

public class DelegatingCharacterParser extends CharacterParser {

    private final Map<Integer, CharacterParser> versionParsers = Map.of(
            99, new V99CharacterParser()
    );

    public Character parse(byte[] data) {
        D2BinaryReader reader = new D2BinaryReader(data);

        int version = reader.setBytePos(0x04).readInt();
        CharacterParser parser = versionParsers.get(version);
        if (parser == null) {
            throw new D2ParserException("Unknown version: " + version);
        }

        return parser.parse(data);
    }

    @Override
    protected void checkVersion(int version) {
        // Do nothing
    }

    @Override
    protected void parseHeader(D2BinaryReader reader, CharacterBuilder character) {
        throw new UnsupportedOperationException();
    }
}
