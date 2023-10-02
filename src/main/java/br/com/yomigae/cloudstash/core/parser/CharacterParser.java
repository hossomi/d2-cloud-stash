package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

public abstract class CharacterParser {

    public static final int SIGNATURE = 0xAA55AA55;

    public Character parse(InputStream inputStream) {
        try {
            return parse(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new D2ParserException("Error reading input stream", e);
        }
    }

    public Character parse(byte[] data) {
        return parse(new D2BinaryReader(data));
    }

    public Character parse(D2BinaryReader reader) {
        reader.setBytePos(0);
        int signature = reader.readInt();
        int version = reader.readInt();
        int fileSize = reader.readInt();
        long checksum = reader.readInt();

        if (signature != SIGNATURE) {
            throw new D2ParserException(format("Invalid signature: %d (expected %d)", signature, SIGNATURE));
        }

        if (fileSize != reader.bytes()) {
            throw new D2ParserException(format("Invalid file size: %d (expected %d)", fileSize, reader.bytes()));
        }

        int actualChecksum = reader.checksum(0x0C);
        if (checksum != actualChecksum) {
            throw new D2ParserException(format("Invalid checksum: %d (expected %d)", checksum, actualChecksum));
        }

        checkVersion(version);

        CharacterBuilder character = Character.builder();
        parseHeader(reader, character);
        System.out.println("%x".formatted(reader.bytePos()));
        return character.build();
    }

    protected abstract void checkVersion(int version);

    protected abstract void parseHeader(D2BinaryReader reader, CharacterBuilder character);
}
