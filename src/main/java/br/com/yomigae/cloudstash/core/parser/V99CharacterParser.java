package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;

import java.time.Instant;
import java.util.stream.IntStream;

public class V99CharacterParser extends VersionCharacterParser {

    public V99CharacterParser() {
        super(99);
    }

    @Override
    protected void parseHeader(D2BinaryReader reader, CharacterBuilder character) {
        character.activeEquipmentSet(EquipmentSet.fromIndex(reader.readInt()));

        byte flags = reader.skipBytes(16).readByte();
        boolean expansion = (flags & 0x20) > 0;
        character
                .ladder((flags & 0x40) > 0)
                .expansion(expansion)
                .dead((flags & 0x08) > 0)
                .hardcore((flags & 0x04) > 0);

        character
                .currentAct(Act.fromAbsoluteAct(reader.readByte()))
                .klass(CharacterClass.fromIndex(reader.skipBytes(2).readByte()))
                .level(reader.skipBytes(2).readByte())
                .lastPlayed(Instant.ofEpochSecond(reader.skipBytes(4).readInt()));

        reader.skipBytes(4);
        character.skillHotkeys(IntStream.range(0, 16)
                        .mapToObj(x -> {
                            int id = reader.readInt();
                            return id != 0xFFFF ? Skill.fromId(id) : null;
                        })
                        .toList())
                .mouseSkill(new Swap<>(
                        new Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt())),
                        new Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt()))))
                .name(reader.setBytePos(0x010B).readString(16));

        boolean hirelingDead = reader.setBytePos(0x00B1).readShort() > 0;
        int hirelingNameId = reader.skipBytes(4).readShort();
        HirelingType hirelingType = HirelingType.fromId(reader.readShort() + (expansion ? 100 : 0));
        Hireling hireling = Hireling.builder()
                .type(hirelingType)
                .name(hirelingType.names().get(hirelingNameId))
                .dead(hirelingDead)
                .experience(reader.readInt())
                .build();
        System.out.println(hireling);
    }
}
