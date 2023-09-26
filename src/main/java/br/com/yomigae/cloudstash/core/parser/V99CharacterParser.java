package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;
import br.com.yomigae.cloudstash.core.model.quest.*;
import com.google.common.collect.Maps;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
                .name(reader.setBytePos(0x010B).readString(16))
                .hireling(Hireling.builder()
                        .dead(reader.setBytePos(0x00B1).readShort() > 0)
                        .nameId(reader.skipBytes(4).readShort())
                        .type(HirelingType.fromId(reader.readShort(), expansion))
                        .experience(reader.readInt())
                        .build());

        reader.find("Woo!".getBytes()).skipBytes(10);
        character.quests(parseQuests(reader));
    }

    private Map<Difficulty, Character.Quests> parseQuests(D2BinaryReader reader) {
        return Maps.toMap(List.of(Difficulty.values()), x -> {
            var quests = Character.Quests.builder()
                    .act1(Act1QuestStatus.builder()
                            .visited(true)
                            .introduced(reader.readShort() > 0)
                            .denOfEvil(parseGenericQuest(reader))
                            .sistersBurialGrounds(parseGenericQuest(reader))
                            .toolsOfTheTrade(parseGenericQuest(reader))
                            .theSearchForCain(parseGenericQuest(reader))
                            .theForgottenTower(parseGenericQuest(reader))
                            .sistersToTheSlaughter(parseGenericQuest(reader))
                            .build())
                    .act2(Act2QuestStatus.builder()
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .radamentsLair(parseGenericQuest(reader))
                            .theHoradricStaff(parseGenericQuest(reader))
                            .taintedSun(parseGenericQuest(reader))
                            .arcaneSanctuary(parseGenericQuest(reader))
                            .theSummoner(parseGenericQuest(reader))
                            .theSevenTombs(parseGenericQuest(reader))
                            .build())
                    .act3(Act3QuestStatus.builder()
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .lamEsensTome(parseGenericQuest(reader))
                            .khalimsWill(parseGenericQuest(reader))
                            .bladeOfTheOldReligion(parseGenericQuest(reader))
                            .theGoldenBird(parseGenericQuest(reader))
                            .theBlackenedTemple(parseGenericQuest(reader))
                            .theGuardian(parseGenericQuest(reader))
                            .build())
                    .act4(Act4QuestStatus.builder()
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .theFallenAngel(parseGenericQuest(reader))
                            .hellsForge(parseGenericQuest(reader))
                            .terrorsEnd(parseGenericQuest(reader))
                            .build())
                    .act5(Act5QuestStatus.builder()
                            .visited(reader.skipBytes(6).readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .siegeOnHarrogath(parseGenericQuest(reader.skipBytes(4)))
                            .rescueOnMountainArreat(parseGenericQuest(reader))
                            .prisonOfIce(with(reader.readShort(), q -> QuestStatus.PrisonOfIce.builder()
                                    .completed((q & 0x0001) > 0)
                                    .scrollConsumed((q & 0x0040) > 0))
                                    .build())
                            .betrayalOfHarrogath(parseGenericQuest(reader))
                            .riteOfPassage(parseGenericQuest(reader))
                            .eveOfDestruction(parseGenericQuest(reader))
                            .build());

            reader.skipBytes(14);
            return quests.build();
        });
    }

    private static QuestStatus.Generic parseGenericQuest(D2BinaryReader reader) {
        return new QuestStatus.Generic((reader.readShort() & 0x0001) == 1);
    }

    public static <T, R> R with(T in, Function<T, R> mapper) {
        return mapper.apply(in);
    }
}
