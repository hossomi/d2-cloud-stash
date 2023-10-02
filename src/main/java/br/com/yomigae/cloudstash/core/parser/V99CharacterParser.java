package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;
import br.com.yomigae.cloudstash.core.model.progression.*;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.model.progression.Quest.*;
import static com.google.common.collect.Maps.toMap;
import static com.google.common.collect.Maps.transformValues;

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
        Map<Difficulty, Character.Progression.ProgressionBuilder> progression = toMap(
                Difficulty.all(),
                difficulty -> parseProgression(difficulty, reader));

        reader.find("WS".getBytes()).skipBytes(8);
        Difficulty.all().forEach(difficulty -> {
            reader.skipBytes(2);
            Character.Progression.ProgressionBuilder p = progression.get(difficulty);
            reader.skipBytes(17);
        });

        character.progression(Map.copyOf(transformValues(progression, Character.Progression.ProgressionBuilder::build)));
    }

    private static Character.Progression.ProgressionBuilder parseProgression(Difficulty difficulty, D2BinaryReader reader) {
        Act1Status.Act1StatusBuilder act1 = Act1Status.builder()
                .difficulty(difficulty)
                .visited(true)
                .introduced(reader.readShort() > 0)
                .quests(Act1Status.Quests.builder()
                        .set(parseGenericQuest(DEN_OF_EVIL, reader))
                        .set(parseGenericQuest(SISTERS_BURIAL_GROUNDS, reader))
                        .set(parseGenericQuest(TOOLS_OF_THE_TRADE, reader))
                        .set(parseGenericQuest(THE_SEARCH_FOR_CAIN, reader))
                        .set(parseGenericQuest(THE_FORGOTTEN_TOWER, reader))
                        .set(parseGenericQuest(SISTERS_TO_THE_SLAUGHTER, reader))
                        .build());

        Act2Status.Act2StatusBuilder act2 = Act2Status.builder()
                .difficulty(difficulty)
                .visited(reader.readShort() > 0)
                .introduced(reader.readShort() > 0)
                .quests(Act2Status.Quests.builder()
                        .radamentsLair(parseGenericQuest(RADAMENTS_LAIR, reader))
                        .theHoradricStaff(parseGenericQuest(THE_HORADRIC_STAFF, reader))
                        .taintedSun(parseGenericQuest(TAINTED_SUN, reader))
                        .arcaneSanctuary(parseGenericQuest(ARCANE_SANCTUARY, reader))
                        .theSummoner(parseGenericQuest(THE_SUMMONER, reader))
                        .theSevenTombs(parseGenericQuest(THE_SEVEN_TOMBS, reader))
                        .build());

        Act3Status.Act3StatusBuilder act3 = Act3Status.builder()
                .difficulty(difficulty)
                .visited(reader.readShort() > 0)
                .introduced(reader.readShort() > 0)
                .quests(Act3Status.Quests.builder()
                        .lamEsensTome(parseGenericQuest(LAM_ESENS_TOME, reader))
                        .khalimsWill(parseGenericQuest(KHALIMS_WILL, reader))
                        .bladeOfTheOldReligion(parseGenericQuest(BLADE_OF_THE_OLD_RELIGION, reader))
                        .theGoldenBird(parseGenericQuest(THE_GOLDEN_BIRD, reader))
                        .theBlackenedTemple(parseGenericQuest(THE_BLACKENED_TEMPLE, reader))
                        .theGuardian(parseGenericQuest(THE_GUARDIAN, reader))
                        .build());

        Act4Status.Act4StatusBuilder act4 = Act4Status.builder()
                .difficulty(difficulty)
                .visited(reader.readShort() > 0)
                .introduced(reader.readShort() > 0)
                .quests(Act4Status.Quests.builder()
                        .theFallenAngel(parseGenericQuest(THE_FALLEN_ANGEL, reader))
                        .hellsForge(parseGenericQuest(HELLS_FORGE, reader))
                        .terrorsEnd(parseGenericQuest(TERRORS_END, reader))
                        .build());

        Act5Status.Act5StatusBuilder act5 = Act5Status.builder()
                .difficulty(difficulty)
                .visited(reader.skipBytes(6).readShort() > 0)
                .introduced(reader.readShort() > 0)
                .quests(Act5Status.Quests.builder()
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

        return Character.Progression.builder()
                .act1(act1.build())
                .act2(act2.build())
                .act3(act3.build())
                .act4(act4.build());
    }

    private static QuestStatus.Generic parseGenericQuest(Quest quest, D2BinaryReader reader) {
        return new QuestStatus.Generic(quest, (reader.readShort() & 0x0001) == 1);
    }

    public static <T, R> R with(T in, Function<T, R> mapper) {
        return mapper.apply(in);
    }
}
