package br.com.yomigae.cloudstash.core.d2s.parser.v99;

import br.com.yomigae.cloudstash.core.d2s.model.*;
import br.com.yomigae.cloudstash.core.d2s.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.d2s.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.d2s.model.hireling.HirelingType;
import br.com.yomigae.cloudstash.core.d2s.parser.D2ParserException;
import br.com.yomigae.cloudstash.core.d2s.parser.HeaderParser;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.time.Instant;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.map;
import static java.lang.String.format;

public class V99HeaderParser implements HeaderParser {

    public static final int VERSION = 99;

    @Override
    public D2S.Builder parse(D2BinaryReader reader) {
        int signature = reader.readInt();
        int version = reader.readInt();
        int fileSize = reader.readInt();
        long checksum = reader.readInt();
        int actualChecksum = reader.checksum(0x0c);

        if (signature != SIGNATURE) {
            throw new D2ParserException(format("Invalid signature: %d (expected %d)", signature, SIGNATURE));
        }
        if (version != VERSION) {
            throw new D2ParserException(format("Cannot parse version %d (expected %d)", version, VERSION));
        }
        if (fileSize != reader.bytes()) {
            throw new D2ParserException(format("Invalid file size: %d (expected %d)", fileSize, reader.bytes()));
        }
        if (checksum != actualChecksum) {
            throw new D2ParserException(format("Invalid checksum: %d (expected %d)", checksum, actualChecksum));
        }

        Tuples.Alternate.Selection alternate = map(reader.readInt(), value -> switch (value) {
            case 0 ->Tuples.Alternate.Selection.PRIMARY;
            case 1 -> Tuples.Alternate.Selection.SECONDARY;
            default -> throw new D2ParserException(format("Invalid weapon alternate selection: %d", value));
        });

        byte flags = reader.skipBytes(16).readByte();
        D2S.Builder d2s = D2S.builder()
                .ladder((flags & 0x40) > 0)
                .expansion((flags & 0x20) > 0)
                .dead((flags & 0x08) > 0)
                .hardcore((flags & 0x04) > 0)
                .currentAct(Act.fromIndex(reader.readByte()))
                .type(CharacterClass.fromIndex(reader.skipBytes(2).readByte()))
                .level(reader.skipBytes(2).readByte())
                .lastPlayed(Instant.ofEpochSecond(reader.skipBytes(4).readInt()));

        reader.skipBytes(4);
        d2s.skillHotkeys(IntStream.range(0, 16)
                        .mapToObj(x -> {
                            int id = reader.readInt();
                            return id != 0xffff ? Skill.fromId(id) : null;
                        })
                        .toList())
                .mouseSkill(new Tuples.Alternate<>(
                        new Tuples.Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt())),
                        new Tuples.Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt())),
                        alternate))
                .name(reader.setByteIndex(0x010b).readString(16));

        parseHireling(reader, d2s, d2s.expansion());
        return d2s;
    }

    private void parseHireling(D2BinaryReader reader, D2S.Builder character, boolean expansion) {
        reader.setByteIndex(0x00b1);
        boolean dead = reader.readShort() > 0;
        int id = reader.readInt();
        if (id == 0) {
            return;
        }

        character.hireling(Hireling.builder()
                .id(id)
                .dead(dead)
                .nameId(reader.readShort())
                .type(HirelingType.fromId(reader.readShort(), expansion))
                .experience(reader.readInt())
                .build());
    }
}
