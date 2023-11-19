package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.model.*;
import br.com.yomigae.cloudstash.core.model.acts.ActStatus;
import br.com.yomigae.cloudstash.core.model.acts.Quest;
import br.com.yomigae.cloudstash.core.model.acts.QuestStatus;
import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.model.character.Character.Builder;
import br.com.yomigae.cloudstash.core.model.character.CharacterClass;
import br.com.yomigae.cloudstash.core.model.hireling.Hireling;
import br.com.yomigae.cloudstash.core.model.hireling.HirelingType;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.IntStream;

import static br.com.yomigae.cloudstash.core.parser.ParserUtils.SaveFileAttribute;
import static br.com.yomigae.cloudstash.core.parser.ParserUtils.saveFileAttribute;
import static br.com.yomigae.cloudstash.core.util.FunctionUtils.toEnumMap;
import static br.com.yomigae.cloudstash.core.util.FunctionUtils.with;
import static com.google.common.collect.Maps.transformValues;

public class V99CharacterParser extends VersionCharacterParser {

    public V99CharacterParser() {
        super(99);
    }

    @Override
    protected void parseHeader(D2BinaryReader reader, Builder character) {
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

    private void parseHireling(D2BinaryReader reader, Builder character, boolean expansion) {
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
    protected void parseActs(D2BinaryReader reader, Builder character) {
        Map<Difficulty, Character.Acts.Builder> all = toEnumMap(
                Difficulty.class,
                Difficulty.all(),
                Character.Acts::builder);

        reader.find("Woo!".getBytes()).skipBytes(10);
        for (Difficulty difficulty : Difficulty.all()) {
            Character.Acts.Builder acts = all.get(difficulty);
            acts.act1()
                    .visited(true)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act1.Quests.builder()
                            .denOfEvil(parseGenericQuest(reader, Quest.DEN_OF_EVIL))
                            .sistersBurialGrounds(parseGenericQuest(reader, Quest.SISTERS_BURIAL_GROUNDS))
                            .toolsOfTheTrade(parseGenericQuest(reader, Quest.TOOLS_OF_THE_TRADE))
                            .theSearchForCain(parseGenericQuest(reader, Quest.THE_SEARCH_FOR_CAIN))
                            .theForgottenTower(parseGenericQuest(reader, Quest.THE_FORGOTTEN_TOWER))
                            .sistersToTheSlaughter(parseGenericQuest(reader, Quest.SISTERS_TO_THE_SLAUGHTER))
                            .build());
            acts.act2()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act2.Quests.builder()
                            .radamentsLair(parseGenericQuest(reader, Quest.RADAMENTS_LAIR))
                            .theHoradricStaff(parseGenericQuest(reader, Quest.THE_HORADRIC_STAFF))
                            .taintedSun(parseGenericQuest(reader, Quest.TAINTED_SUN))
                            .arcaneSanctuary(parseGenericQuest(reader, Quest.ARCANE_SANCTUARY))
                            .theSummoner(parseGenericQuest(reader, Quest.THE_SUMMONER))
                            .theSevenTombs(parseGenericQuest(reader, Quest.THE_SEVEN_TOMBS))
                            .build());
            acts.act3()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act3.Quests.builder()
                            .lamEsensTome(parseGenericQuest(reader, Quest.LAM_ESENS_TOME))
                            .khalimsWill(parseGenericQuest(reader, Quest.KHALIMS_WILL))
                            .bladeOfTheOldReligion(parseGenericQuest(reader, Quest.BLADE_OF_THE_OLD_RELIGION))
                            .theGoldenBird(parseGenericQuest(reader, Quest.THE_GOLDEN_BIRD))
                            .theBlackenedTemple(parseGenericQuest(reader, Quest.THE_BLACKENED_TEMPLE))
                            .theGuardian(parseGenericQuest(reader, Quest.THE_GUARDIAN))
                            .build());
            acts.act4()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act4.Quests.builder()
                            .theFallenAngel(parseGenericQuest(reader, Quest.THE_FALLEN_ANGEL))
                            .hellsForge(parseGenericQuest(reader, Quest.HELLS_FORGE))
                            .terrorsEnd(parseGenericQuest(reader, Quest.TERRORS_END))
                            .build());
            acts.act5()
                    // TODO: Act 5 visited/introduced is always zero?
                    .visited(reader.skipBytes(6).readShort() > 0)
                    .quests(ActStatus.Act5.Quests.builder()
                            .siegeOnHarrogath(parseGenericQuest(reader.skipBytes(4), Quest.SIEGE_ON_HARROGATH))
                            .rescueOnMountainArreat(parseGenericQuest(reader, Quest.RESCUE_ON_MOUNTAIN_ARREAT))
                            .prisonOfIce(with(reader.readShort(), q -> Quest.Act5.PrisonOfIce.Status.builder()
                                    .completed((q & 0x0001) > 0)
                                    // TODO: Figure out the actual bit
                                    .scrollConsumed((q & 0x0040) > 0))
                                    .build())
                            .betrayalOfHarrogath(parseGenericQuest(reader, Quest.BETRAYAL_OF_HARROGATH))
                            .riteOfPassage(parseGenericQuest(reader, Quest.RITE_OF_PASSAGE))
                            .eveOfDestruction(parseGenericQuest(reader, Quest.EVE_OF_DESTRUCTION))
                            .build());
            reader.skipBytes(14);
        }

        reader.find("WS".getBytes());
        reader.skipBytes(8);
        for (Difficulty difficulty : Difficulty.all()) {
            Character.Acts.Builder acts = all.get(difficulty);
            reader.skipBytes(2);
            long data = reader.read(40);
            acts.act1().waypoints(ActStatus.Act1.Waypoints.builder()
                    .rogueEncampment((data & 0x0000000001L) != 0)
                    .coldPlains((data & 0x0000000002L) != 0)
                    .stonyField((data & 0x0000000004L) != 0)
                    .darkWood((data & 0x0000000008L) != 0)
                    .blackMarsh((data & 0x0000000010L) != 0)
                    .outerCloister((data & 0x0000000020L) != 0)
                    .jail((data & 0x0000000040L) != 0)
                    .innerCloister((data & 0x0000000080L) != 0)
                    .catacombs((data & 0x0000000100L) != 0)
                    .build());
            acts.act2().waypoints(ActStatus.Act2.Waypoints.builder()
                    .lutGholein((data & 0x0000000200L) != 0)
                    .sewers((data & 0x0000000400L) != 0)
                    .dryHills((data & 0x0000000800L) != 0)
                    .hallsOfTheDead((data & 0x0000001000L) != 0)
                    .farOasis((data & 0x0000002000L) != 0)
                    .lostCity((data & 0x0000004000L) != 0)
                    .palaceCellar((data & 0x0000008000L) != 0)
                    .arcaneSanctuary((data & 0x0000010000L) != 0)
                    .canyonOfTheMagi((data & 0x0000020000L) != 0)
                    .build());
            acts.act3().waypoints(ActStatus.Act3.Waypoints.builder()
                    .kurastDocks((data & 0x0000040000L) != 0)
                    .spiderForest((data & 0x0000080000L) != 0)
                    .greatMarsh((data & 0x0000100000L) != 0)
                    .flayerJungle((data & 0x0000200000L) != 0)
                    .lowerKurast((data & 0x0000400000L) != 0)
                    .kurastBazaar((data & 0x0000800000L) != 0)
                    .upperKurast((data & 0x0001000000L) != 0)
                    .travincal((data & 0x0002000000L) != 0)
                    .duranceOfHate((data & 0x0004000000L) != 0)
                    .build());
            acts.act4().waypoints(ActStatus.Act4.Waypoints.builder()
                    .thePandemoniumFortress((data & 0x0008000000L) != 0)
                    .cityOfTheDamned((data & 0x0010000000L) != 0)
                    .riverOfFlame((data & 0x0020000000L) != 0)
                    .build());
            acts.act5().waypoints(ActStatus.Act5.Waypoints.builder()
                    .harrogath((data & 0x0040000000L) != 0)
                    .frigidHighlands((data & 0x0080000000L) != 0)
                    .arreatPlateau((data & 0x0100000000L) != 0)
                    .crystallinePassage((data & 0x0200000000L) != 0)
                    .hallsOfPain((data & 0x0400000000L) != 0)
                    .glacialTrail((data & 0x0800000000L) != 0)
                    .frozenTundra((data & 0x1000000000L) != 0)
                    .theAncientsWay((data & 0x2000000000L) != 0)
                    .worldstoneKeep((data & 0x4000000000L) != 0)
                    .build());
            reader.skipBytes(17);
        }

        character.acts(new EnumMap<>(transformValues(all, Character.Acts.Builder::build)));
    }

    @Override
    protected void parseAttributes(D2BinaryReader reader, Builder character) {
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
        return new QuestStatus.Generic((reader.readShort() & 0x0001) == 1);
    }
}
