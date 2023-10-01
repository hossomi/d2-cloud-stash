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

import static br.com.yomigae.cloudstash.core.model.quest.Quest.*;

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
                .currentAct(Act.fromIndex(reader.readByte()))
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
        character.progression(parseProgression(reader));
    }

    private Map<Difficulty, Character.Progression> parseProgression(D2BinaryReader reader) {
        return Maps.toMap(List.of(Difficulty.values()), difficulty -> {
            Character.Progression.ProgressionBuilder quests = Character.Progression.builder()
                    .act1(Act1Status.builder()
                            .difficulty(difficulty)
                            .visited(true)
                            .introduced(reader.readShort() > 0)
                            .denOfEvil(parseGenericQuest(DEN_OF_EVIL, reader))
                            .sistersBurialGrounds(parseGenericQuest(SISTERS_BURIAL_GROUNDS, reader))
                            .toolsOfTheTrade(parseGenericQuest(TOOLS_OF_THE_TRADE, reader))
                            .theSearchForCain(parseGenericQuest(THE_SEARCH_FOR_CAIN, reader))
                            .theForgottenTower(parseGenericQuest(THE_FORGOTTEN_TOWER, reader))
                            .sistersToTheSlaughter(parseGenericQuest(SISTERS_TO_THE_SLAUGHTER, reader))
                            .build())
                    .act2(Act2Status.builder()
                            .difficulty(difficulty)
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .radamentsLair(parseGenericQuest(RADAMENTS_LAIR, reader))
                            .theHoradricStaff(parseGenericQuest(THE_HORADRIC_STAFF, reader))
                            .taintedSun(parseGenericQuest(TAINTED_SUN, reader))
                            .arcaneSanctuary(parseGenericQuest(ARCANE_SANCTUARY, reader))
                            .theSummoner(parseGenericQuest(THE_SUMMONER, reader))
                            .theSevenTombs(parseGenericQuest(THE_SEVEN_TOMBS, reader))
                            .build())
                    .act3(Act3Status.builder()
                            .difficulty(difficulty)
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .lamEsensTome(parseGenericQuest(LAM_ESENS_TOME, reader))
                            .khalimsWill(parseGenericQuest(KHALIMS_WILL, reader))
                            .bladeOfTheOldReligion(parseGenericQuest(BLADE_OF_THE_OLD_RELIGION, reader))
                            .theGoldenBird(parseGenericQuest(THE_GOLDEN_BIRD, reader))
                            .theBlackenedTemple(parseGenericQuest(THE_BLACKENED_TEMPLE, reader))
                            .theGuardian(parseGenericQuest(THE_GUARDIAN, reader))
                            .build())
                    .act4(Act4Status.builder()
                            .difficulty(difficulty)
                            .visited(reader.readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .theFallenAngel(parseGenericQuest(THE_FALLEN_ANGEL, reader))
                            .hellsForge(parseGenericQuest(HELLS_FORGE, reader))
                            .terrorsEnd(parseGenericQuest(TERRORS_END, reader))
                            .build())
                    .act5(Act5Status.builder()
                            .difficulty(difficulty)
                            .visited(reader.skipBytes(6).readShort() > 0)
                            .introduced(reader.readShort() > 0)
                            .siegeOnHarrogath(parseGenericQuest(SIEGE_ON_HARROGATH, reader.skipBytes(4)))
                            .rescueOnMountainArreat(parseGenericQuest(RESCUE_ON_MOUNTAIN_ARREAT, reader))
                            .prisonOfIce(with(reader.readShort(), q -> QuestStatus.PrisonOfIce.builder()
                                    .completed((q & 0x0001) > 0)
                                    .scrollConsumed((q & 0x0040) > 0))
                                    .build())
                            .betrayalOfHarrogath(parseGenericQuest(BETRAYAL_OF_HARROGATH, reader))
                            .riteOfPassage(parseGenericQuest(RITE_OF_PASSAGE, reader))
                            .eveOfDestruction(parseGenericQuest(EVE_OF_DESTRUCTION, reader))
                            .build());

            reader.skipBytes(14);
            return quests.build();
        });
    }

    private static QuestStatus.Generic parseGenericQuest(Quest quest, D2BinaryReader reader) {
        return new QuestStatus.Generic(quest, (reader.readShort() & 0x0001) == 1);
    }

    public static <T, R> R with(T in, Function<T, R> mapper) {
        return mapper.apply(in);
    }
}
