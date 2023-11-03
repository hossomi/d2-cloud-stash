package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.ActMap;
import br.com.yomigae.cloudstash.core.model.Difficulty;
import br.com.yomigae.cloudstash.core.model.DifficultyMap;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.progression.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collector;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;

public abstract class CharacterParser {

    public static final int SIGNATURE = 0xaa55aa55;

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
        reader.setByteIndex(0);
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

        int actualChecksum = reader.checksum(0x0c);
        if (checksum != actualChecksum) {
            throw new D2ParserException(format("Invalid checksum: %d (expected %d)", checksum, actualChecksum));
        }

        checkVersion(version);

        CharacterBuilder character = Character.builder();
        parseHeader(reader, character);
        parseProgression(reader, character);
        parseAttributes(reader, character);
        return character.build();
    }

    protected abstract void checkVersion(int version);

    protected abstract void parseHeader(D2BinaryReader reader, CharacterBuilder character);

    protected void parseProgression(D2BinaryReader reader, CharacterBuilder character) {
        ActMap<ActProgression.Builder<?, ?, ?>> actProgression = new ActMap<>();
        Difficulty.all().forEach(difficulty -> {
            actProgression.put(difficulty, 0, Act1Progression.builder().difficulty(difficulty));
            actProgression.put(difficulty, 1, Act2Progression.builder().difficulty(difficulty));
            actProgression.put(difficulty, 2, Act3Progression.builder().difficulty(difficulty));
            actProgression.put(difficulty, 3, Act4Progression.builder().difficulty(difficulty));
            actProgression.put(difficulty, 4, Act5Progression.builder().difficulty(difficulty));
        });

        parseQuests(reader, actProgression);
        parseWaypoints(reader, actProgression);

        character.progression(actProgression.values().stream()
                .map(ActProgression.Builder::build)
                .collect(groupingBy(a -> a.act().difficulty(),
                        DifficultyMap::new,
                        Collector.of(Character.Progression::builder,
                                Character.Progression.Builder::set,
                                (a, b) -> {throw new RuntimeException();},
                                Character.Progression.Builder::build))));
    }

    protected abstract void parseQuests(D2BinaryReader reader, ActMap<ActProgression.Builder<?, ?, ?>> progressions);

    protected abstract void parseWaypoints(D2BinaryReader reader, ActMap<ActProgression.Builder<?, ?, ?>> progressions);

    protected abstract void parseAttributes(D2BinaryReader reader, CharacterBuilder character);
}
