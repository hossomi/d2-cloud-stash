package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.CharacterBuilder;
import br.com.yomigae.cloudstash.core.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;
import br.com.yomigae.cloudstash.core.model.progression.*;
import br.com.yomigae.cloudstash.core.util.FunctionUtils;

import java.time.Instant;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.model.Area.*;
import static br.com.yomigae.cloudstash.core.model.progression.Quest.DEN_OF_EVIL;
import static br.com.yomigae.cloudstash.core.model.progression.Quest.*;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBits;
import static java.util.stream.Collectors.groupingBy;

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
                .name(reader.setByteIndex(0x010B).readString(16))
                .hireling(Hireling.builder()
                        .dead(reader.setByteIndex(0x00B1).readShort() > 0)
                        .nameId(reader.skipBytes(4).readShort())
                        .type(HirelingType.fromId(reader.readShort(), expansion))
                        .experience(reader.readInt())
                        .build());

        reader.find("Woo!".getBytes()).skipBytes(10);
        ActMap<ActProgression.Builder<?, ?, ?>> actStatus = new ActMap<>();
        for (Difficulty difficulty : Difficulty.all()) {
            actStatus.put(Act.from(difficulty, 0), Act1Progression.builder()
                    .difficulty(difficulty)
                    .visited(true)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(DEN_OF_EVIL, reader))
                    .set(parseGenericQuest(SISTERS_BURIAL_GROUNDS, reader))
                    .set(parseGenericQuest(TOOLS_OF_THE_TRADE, reader))
                    .set(parseGenericQuest(THE_SEARCH_FOR_CAIN, reader))
                    .set(parseGenericQuest(THE_FORGOTTEN_TOWER, reader))
                    .set(parseGenericQuest(SISTERS_TO_THE_SLAUGHTER, reader))
                    .done());

            actStatus.put(Act.from(difficulty, 1), Act2Progression.builder()
                    .difficulty(difficulty)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(RADAMENTS_LAIR, reader))
                    .set(parseGenericQuest(THE_HORADRIC_STAFF, reader))
                    .set(parseGenericQuest(TAINTED_SUN, reader))
                    .set(parseGenericQuest(Quest.ARCANE_SANCTUARY, reader))
                    .set(parseGenericQuest(THE_SUMMONER, reader))
                    .set(parseGenericQuest(THE_SEVEN_TOMBS, reader))
                    .done());

            actStatus.put(Act.from(difficulty, 2), Act3Progression.builder()
                    .difficulty(difficulty)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(LAM_ESENS_TOME, reader))
                    .set(parseGenericQuest(KHALIMS_WILL, reader))
                    .set(parseGenericQuest(BLADE_OF_THE_OLD_RELIGION, reader))
                    .set(parseGenericQuest(THE_GOLDEN_BIRD, reader))
                    .set(parseGenericQuest(THE_BLACKENED_TEMPLE, reader))
                    .set(parseGenericQuest(THE_GUARDIAN, reader))
                    .done());

            actStatus.put(Act.from(difficulty, 3), Act4Progression.builder()
                    .difficulty(difficulty)
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(THE_FALLEN_ANGEL, reader))
                    .set(parseGenericQuest(HELLS_FORGE, reader))
                    .set(parseGenericQuest(TERRORS_END, reader))
                    .done());

            actStatus.put(Act.from(difficulty, 4), Act5Progression.builder()
                    .difficulty(difficulty)
                    // TODO: Act 5 visited/introduced is always zero?
                    .visited(reader.skipBytes(6).readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests()
                    .set(parseGenericQuest(SIEGE_ON_HARROGATH, reader.skipBytes(4)))
                    .set(parseGenericQuest(RESCUE_ON_MOUNTAIN_ARREAT, reader))
                    .set(FunctionUtils.with(reader.readShort(), q -> QuestStatus.PrisonOfIce.builder()
                                    .completed((q & 0x0001) > 0)
                                    // TODO: Figure out the actual bit
                                    .scrollConsumed((q & 0x0040) > 0))
                            .build())
                    .set(parseGenericQuest(BETRAYAL_OF_HARROGATH, reader))
                    .set(parseGenericQuest(RITE_OF_PASSAGE, reader))
                    .set(parseGenericQuest(EVE_OF_DESTRUCTION, reader))
                    .done());
            reader.skipBytes(14);
        }

        reader.find("WS".getBytes());
        System.out.printf("Waypoints: %x (%d)\n", reader.byteIndex(), reader.byteIndex());
        reader.skipBytes(8);
        for (Difficulty difficulty : Difficulty.all()) {
            reader.skipBytes(2);
            long data = 0;
            for (int i = 0; i < 5; i++) {
                data = data << 8 | flipBits(reader.readByte(), 8) & 0xffL;
            }
            long bit = 0x10000000000L;
            actStatus.get(Act.from(difficulty, 0)).waypoints()
                    .set(new WaypointStatus(ROGUE_ENCAMPMENT, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(COLD_PLAINS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(STONY_FIELD, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(DARK_WOOD, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(BLACK_MARSH, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(OUTER_CLOISTER, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(JAIL_LEVEL_1, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(INNER_CLOISTER, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(CATACOMBS_LEVEL_2, (data & (bit >>= 1)) > 0))
                    .done();
            actStatus.get(Act.from(difficulty, 1)).waypoints()
                    .set(new WaypointStatus(LUT_GHOLEIN, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(LUT_GHOLEIN_SEWERS_LEVEL_2, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(DRY_HILLS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(HALLS_OF_THE_DEAD_LEVEL_2, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(FAR_OASIS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(LOST_CITY, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(PALACE_CELLAR_LEVEL_1, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(Area.ARCANE_SANCTUARY, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(CANYON_OF_THE_MAGI, (data & (bit >>= 1)) > 0))
                    .done();
            actStatus.get(Act.from(difficulty, 2)).waypoints()
                    .set(new WaypointStatus(KURAST_DOCKS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(SPIDER_FOREST, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(GREAT_MARSH, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(FLAYER_JUNGLE, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(LOWER_KURAST, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(KURAST_BAZAAR, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(UPPER_KURAST, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(TRAVINCAL, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(DURANCE_OF_HATE_LEVEL_2, (data & (bit >>= 1)) > 0))
                    .done();
            actStatus.get(Act.from(difficulty, 3)).waypoints()
                    .set(new WaypointStatus(THE_PANDEMONIUM_FORTRESS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(CITY_OF_THE_DAMNED, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(RIVER_OF_FLAME, (data & (bit >>= 1)) > 0))
                    .done();
            actStatus.get(Act.from(difficulty, 4)).waypoints()
                    .set(new WaypointStatus(HARROGATH, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(FRIGID_HIGHLANDS, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(ARREAT_PLATEAU, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(CRYSTALLINE_PASSAGE, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(HALLS_OF_PAIN, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(GLACIAL_TRAIL, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(FROZEN_TUNDRA, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(THE_ANCIENTS_WAY, (data & (bit >>= 1)) > 0))
                    .set(new WaypointStatus(WORLDSTONE_KEEP_LEVEL_2, (data & (bit >>= 1)) > 0))
                    .done();
            reader.skipBytes(17);
        }

        character.progression(actStatus.values().stream()
                .map(ActProgression.Builder::build)
                .collect(groupingBy(a -> a.act().difficulty(),
                        DifficultyMap::new,
                        Collector.of(Character.Progression::builder,
                                Character.Progression.Builder::set,
                                (a, b) -> {throw new RuntimeException();},
                                Character.Progression.Builder::build)
                )));
        System.out.printf("Waypoints: %x (%d)\n", reader.byteIndex(), reader.byteIndex());
    }

    @Override
    protected void parseAttributes(D2BinaryReader reader, CharacterBuilder character) {

    }

    private static QuestStatus.Generic parseGenericQuest(Quest quest, D2BinaryReader reader) {
        return new QuestStatus.Generic(quest, (reader.readShort() & 0x0001) == 1);
    }
}
