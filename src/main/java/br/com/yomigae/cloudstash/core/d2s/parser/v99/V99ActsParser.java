package br.com.yomigae.cloudstash.core.d2s.parser.v99;

import br.com.yomigae.cloudstash.core.d2s.model.Difficulty;
import br.com.yomigae.cloudstash.core.d2s.model.acts.ActStatus;
import br.com.yomigae.cloudstash.core.d2s.model.acts.Quest;
import br.com.yomigae.cloudstash.core.d2s.model.acts.QuestStatus;
import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.d2s.parser.ActsParser;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;

import java.util.EnumMap;
import java.util.Map;

import static br.com.yomigae.cloudstash.core.util.FunctionUtils.map;
import static br.com.yomigae.cloudstash.core.util.FunctionUtils.toEnumMap;
import static com.google.common.collect.Maps.transformValues;

public class V99ActsParser implements ActsParser {

    @Override
    public Map<Difficulty, D2S.Acts> parse(D2BinaryReader reader) {
        Map<Difficulty, D2S.Acts.Builder> all = toEnumMap(
                Difficulty.class,
                Difficulty.all(),
                D2S.Acts::builder);

        reader.find("Woo!".getBytes()).skipBytes(10);
        for (Difficulty difficulty : Difficulty.all()) {
            D2S.Acts.Builder acts = all.get(difficulty);
            acts.act1()
                    .visited(true)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act1.Quests.builder()
                            .denOfEvil(parseGenericQuest(reader))
                            .sistersBurialGrounds(parseGenericQuest(reader))
                            .toolsOfTheTrade(parseGenericQuest(reader))
                            .theSearchForCain(parseGenericQuest(reader))
                            .theForgottenTower(parseGenericQuest(reader))
                            .sistersToTheSlaughter(parseGenericQuest(reader))
                            .build());
            acts.act2()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act2.Quests.builder()
                            .radamentsLair(parseGenericQuest(reader))
                            .theHoradricStaff(parseGenericQuest(reader))
                            .taintedSun(parseGenericQuest(reader))
                            .arcaneSanctuary(parseGenericQuest(reader))
                            .theSummoner(parseGenericQuest(reader))
                            .theSevenTombs(parseGenericQuest(reader))
                            .build());
            acts.act3()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act3.Quests.builder()
                            .lamEsensTome(parseGenericQuest(reader))
                            .khalimsWill(parseGenericQuest(reader))
                            .bladeOfTheOldReligion(parseGenericQuest(reader))
                            .theGoldenBird(parseGenericQuest(reader))
                            .theBlackenedTemple(parseGenericQuest(reader))
                            .theGuardian(parseGenericQuest(reader))
                            .build());
            acts.act4()
                    .visited(reader.readShort() > 0)
                    .introduced(reader.readShort() > 0)
                    .quests(ActStatus.Act4.Quests.builder()
                            .theFallenAngel(parseGenericQuest(reader))
                            .hellsForge(parseGenericQuest(reader))
                            .terrorsEnd(parseGenericQuest(reader))
                            .build());
            acts.act5()
                    // TODO: Act 5 visited/introduced is always zero?
                    .visited(reader.skipBytes(6).readShort() > 0)
                    .quests(ActStatus.Act5.Quests.builder()
                            .siegeOnHarrogath(parseGenericQuest(reader.skipBytes(4)))
                            .rescueOnMountainArreat(parseGenericQuest(reader))
                            .prisonOfIce(map(reader.readShort(), q -> Quest.Act5.PrisonOfIce.Status.builder()
                                    .completed((q & 0x0001) > 0)
                                    // TODO: Figure out the actual bit
                                    .scrollConsumed((q & 0x0040) > 0))
                                    .build())
                            .betrayalOfHarrogath(parseGenericQuest(reader))
                            .riteOfPassage(parseGenericQuest(reader))
                            .eveOfDestruction(parseGenericQuest(reader))
                            .build());
            reader.skipBytes(14);
        }

        reader.find("WS".getBytes());
        reader.skipBytes(8);
        for (Difficulty difficulty : Difficulty.all()) {
            D2S.Acts.Builder acts = all.get(difficulty);
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

        return new EnumMap<>(transformValues(all, D2S.Acts.Builder::build));
    }

    private static QuestStatus.Generic parseGenericQuest(D2BinaryReader reader) {
        return new QuestStatus.Generic((reader.readShort() & 0x0001) == 1);
    }
}
