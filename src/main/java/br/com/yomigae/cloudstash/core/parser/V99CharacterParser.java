package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;
import br.com.yomigae.cloudstash.core.model.progression.ActProgression;
import br.com.yomigae.cloudstash.core.model.progression.Quest;
import br.com.yomigae.cloudstash.core.model.progression.QuestStatus;
import br.com.yomigae.cloudstash.core.util.FunctionUtils;

import java.time.Instant;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.parser.ParserUtils.SaveFileAttribute;
import static br.com.yomigae.cloudstash.core.parser.ParserUtils.saveFileAttribute;

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
                            return id != 0xffff ? Skill.fromId(id) : null;
                        })
                        .toList())
                .mouseSkill(new Swap<>(
                        new Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt())),
                        new Dual<>(
                                Skill.fromId(reader.readInt()),
                                Skill.fromId(reader.readInt()))))
                .name(reader.setByteIndex(0x010b).readString(16));

        parseHireling(reader, character, expansion);
    }

    private void parseHireling(D2BinaryReader reader, CharacterBuilder character, boolean expansion) {
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

    @Override
    protected void parseQuests(D2BinaryReader reader, ActMap<ActProgression.Builder<?, ?, ?>> progressions) {
        reader.find("Woo!".getBytes()).skipBytes(10);
        for (Difficulty difficulty : Difficulty.all()) {
            progressions.get(difficulty, 0)
                    .visited(true)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(reader, Quest.DEN_OF_EVIL))
                    .set(parseGenericQuest(reader, Quest.SISTERS_BURIAL_GROUNDS))
                    .set(parseGenericQuest(reader, Quest.TOOLS_OF_THE_TRADE))
                    .set(parseGenericQuest(reader, Quest.THE_SEARCH_FOR_CAIN))
                    .set(parseGenericQuest(reader, Quest.THE_FORGOTTEN_TOWER))
                    .set(parseGenericQuest(reader, Quest.SISTERS_TO_THE_SLAUGHTER))
                    .done();

            progressions.get(difficulty, 1)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(reader, Quest.RADAMENTS_LAIR))
                    .set(parseGenericQuest(reader, Quest.THE_HORADRIC_STAFF))
                    .set(parseGenericQuest(reader, Quest.TAINTED_SUN))
                    .set(parseGenericQuest(reader, Quest.ARCANE_SANCTUARY))
                    .set(parseGenericQuest(reader, Quest.THE_SUMMONER))
                    .set(parseGenericQuest(reader, Quest.THE_SEVEN_TOMBS))
                    .done();

            progressions.get(difficulty, 2)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(reader, Quest.LAM_ESENS_TOME))
                    .set(parseGenericQuest(reader, Quest.KHALIMS_WILL))
                    .set(parseGenericQuest(reader, Quest.BLADE_OF_THE_OLD_RELIGION))
                    .set(parseGenericQuest(reader, Quest.THE_GOLDEN_BIRD))
                    .set(parseGenericQuest(reader, Quest.THE_BLACKENED_TEMPLE))
                    .set(parseGenericQuest(reader, Quest.THE_GUARDIAN))
                    .done();

            progressions.get(difficulty, 3)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(reader, Quest.THE_FALLEN_ANGEL))
                    .set(parseGenericQuest(reader, Quest.HELLS_FORGE))
                    .set(parseGenericQuest(reader, Quest.TERRORS_END))
                    .done();

            progressions.get(difficulty, 4)
                    // TODO: Act 5 visited/introduced is always zero?
                    .visited(reader.skipBytes(6).readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(reader.skipBytes(4), Quest.SIEGE_ON_HARROGATH))
                    .set(parseGenericQuest(reader, Quest.RESCUE_ON_MOUNTAIN_ARREAT))
                    .set(FunctionUtils.with(reader.readShort(), q -> QuestStatus.PrisonOfIce.builder()
                                    .completed((q & 0x0001) > 0)
                                    // TODO: Figure out the actual bit
                                    .scrollConsumed((q & 0x0040) > 0))
                            .build())
                    .set(parseGenericQuest(reader, Quest.BETRAYAL_OF_HARROGATH))
                    .set(parseGenericQuest(reader, Quest.RITE_OF_PASSAGE))
                    .set(parseGenericQuest(reader, Quest.EVE_OF_DESTRUCTION))
                    .done();
            reader.skipBytes(14);
        }
    }

    @Override
    protected void parseWaypoints(D2BinaryReader reader, ActMap<ActProgression.Builder<?, ?, ?>> progressions) {
        reader.find("WS".getBytes());
        reader.skipBytes(8);
        for (Difficulty difficulty : Difficulty.all()) {
            reader.skipBytes(2);
            long data = reader.read(40);
            progressions.get(difficulty, 0).waypoints()
                    .set(Area.ROGUE_ENCAMPMENT, (data & 0x0000000001L) != 0)
                    .set(Area.COLD_PLAINS, (data & 0x0000000002L) != 0)
                    .set(Area.STONY_FIELD, (data & 0x0000000004L) != 0)
                    .set(Area.DARK_WOOD, (data & 0x0000000008L) != 0)
                    .set(Area.BLACK_MARSH, (data & 0x0000000010L) != 0)
                    .set(Area.OUTER_CLOISTER, (data & 0x0000000020L) != 0)
                    .set(Area.JAIL_LEVEL_1, (data & 0x0000000040L) != 0)
                    .set(Area.INNER_CLOISTER, (data & 0x0000000080L) != 0)
                    .set(Area.CATACOMBS_LEVEL_2, (data & 0x0000000100L) != 0)
                    .done();
            progressions.get(difficulty, 1).waypoints()
                    .set(Area.LUT_GHOLEIN, (data & 0x0000000200L) != 0)
                    .set(Area.LUT_GHOLEIN_SEWERS_LEVEL_2, (data & 0x0000000400L) != 0)
                    .set(Area.DRY_HILLS, (data & 0x0000000800L) != 0)
                    .set(Area.HALLS_OF_THE_DEAD_LEVEL_2, (data & 0x0000001000L) != 0)
                    .set(Area.FAR_OASIS, (data & 0x0000002000L) != 0)
                    .set(Area.LOST_CITY, (data & 0x0000004000L) != 0)
                    .set(Area.PALACE_CELLAR_LEVEL_1, (data & 0x0000008000L) != 0)
                    .set(Area.ARCANE_SANCTUARY, (data & 0x0000010000L) != 0)
                    .set(Area.CANYON_OF_THE_MAGI, (data & 0x0000020000L) != 0)
                    .done();
            progressions.get(difficulty, 2).waypoints()
                    .set(Area.KURAST_DOCKS, (data & 0x0000040000L) != 0)
                    .set(Area.SPIDER_FOREST, (data & 0x0000080000L) != 0)
                    .set(Area.GREAT_MARSH, (data & 0x0000100000L) != 0)
                    .set(Area.FLAYER_JUNGLE, (data & 0x0000200000L) != 0)
                    .set(Area.LOWER_KURAST, (data & 0x0000400000L) != 0)
                    .set(Area.KURAST_BAZAAR, (data & 0x0000800000L) != 0)
                    .set(Area.UPPER_KURAST, (data & 0x0001000000L) != 0)
                    .set(Area.TRAVINCAL, (data & 0x0002000000L) != 0)
                    .set(Area.DURANCE_OF_HATE_LEVEL_2, (data & 0x0004000000L) != 0)
                    .done();
            progressions.get(difficulty, 3).waypoints()
                    .set(Area.THE_PANDEMONIUM_FORTRESS, (data & 0x0008000000L) != 0)
                    .set(Area.CITY_OF_THE_DAMNED, (data & 0x0010000000L) != 0)
                    .set(Area.RIVER_OF_FLAME, (data & 0x0020000000L) != 0)
                    .done();
            progressions.get(difficulty, 4).waypoints()
                    .set(Area.HARROGATH, (data & 0x0040000000L) != 0)
                    .set(Area.FRIGID_HIGHLANDS, (data & 0x0080000000L) != 0)
                    .set(Area.ARREAT_PLATEAU, (data & 0x0100000000L) != 0)
                    .set(Area.CRYSTALLINE_PASSAGE, (data & 0x0200000000L) != 0)
                    .set(Area.HALLS_OF_PAIN, (data & 0x0400000000L) != 0)
                    .set(Area.GLACIAL_TRAIL, (data & 0x0800000000L) != 0)
                    .set(Area.FROZEN_TUNDRA, (data & 0x1000000000L) != 0)
                    .set(Area.THE_ANCIENTS_WAY, (data & 0x2000000000L) != 0)
                    .set(Area.WORLDSTONE_KEEP_LEVEL_2, (data & 0x4000000000L) != 0)
                    .done();
            reader.skipBytes(17);
        }
    }

    @Override
    protected void parseAttributes(D2BinaryReader reader, CharacterBuilder character) {
        reader.find("gf".getBytes()).skipBytes(2);
        for (long id = reader.read(9); id != 0x1ff; id = reader.read(9)) {
            SaveFileAttribute key = saveFileAttribute((int) id);
            int value = (int) reader.read(key.bits());
            if (key.attribute() != null) {
                character.attribute(key.attribute(), value / key.factor());
            }
        }
    }

    private static QuestStatus.Generic parseGenericQuest(D2BinaryReader reader, Quest quest) {
        return new QuestStatus.Generic(quest, (reader.readShort() & 0x0001) == 1);
    }
}
