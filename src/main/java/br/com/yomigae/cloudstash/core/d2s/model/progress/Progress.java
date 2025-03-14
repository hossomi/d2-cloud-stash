package br.com.yomigae.cloudstash.core.d2s.model.progress;

import br.com.yomigae.cloudstash.core.d2s.model.Act;
import br.com.yomigae.cloudstash.core.d2s.model.Area;
import br.com.yomigae.cloudstash.core.d2s.model.Difficulty;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import static br.com.yomigae.cloudstash.core.d2s.model.ModelException.invalidQuest;
import static br.com.yomigae.cloudstash.core.d2s.model.ModelException.invalidWaypoint;

@Data
@AllArgsConstructor
public abstract sealed class Progress<
        Q extends Progress.Quests,
        W extends Progress.Waypoints>
        permits Progress.Act1, Progress.Act2, Progress.Act3, Progress.Act4, Progress.Act5 {

    private final Act act;
    private boolean visited;
    private boolean introduced;
    private Q quests;
    private W waypoints;

    public sealed interface Quests permits Act1.Quests, Act2.Quests, Act3.Quests, Act4.Quests, Act5.Quests {

        <Q extends Quest<S>, S extends QuestStatus> S get(Q quest);

        Map<Quest<?>, QuestStatus> all();

        sealed interface Builder<
                B extends Builder<B>>
                permits Act1.Quests.Builder, Act2.Quests.Builder, Act3.Quests.Builder, Act4.Quests.Builder, Act5.Quests.Builder {

            <Q extends Quest<S>, S extends QuestStatus> B set(Q quest, S status);
        }
    }

    public sealed interface Waypoints permits Act1.Waypoints, Act2.Waypoints, Act3.Waypoints, Act4.Waypoints, Act5.Waypoints {

        boolean get(Area area);

        Map<Area, Boolean> all();

        sealed interface Builder<
                B extends Builder<B>>
                permits Act1.Waypoints.Builder, Act2.Waypoints.Builder, Act3.Waypoints.Builder, Act4.Waypoints.Builder, Act5.Waypoints.Builder {

            B set(Area area, boolean active);
        }
    }

    public abstract sealed static class Builder<
            Q extends Quests.Builder<Q>,
            W extends Waypoints.Builder<W>,
            B extends Builder<Q, W, B>>
            permits Act1.Builder, Act2.Builder, Act3.Builder, Act4.Builder, Act5.Builder {
    }

    public static final class Act1 extends Progress<Act1.Quests, Act1.Waypoints> {

        @lombok.Builder
        public Act1(Difficulty difficulty, boolean visited, boolean introduced,
                Quests quests, Waypoints waypoints) {
            super(Act.from(difficulty, 0), visited, introduced, quests, waypoints);
        }

        @Data
        @lombok.Builder
        public static final class Quests implements Progress.Quests {

            private final QuestStatus.Generic denOfEvil;
            private final QuestStatus.Generic sistersBurialGrounds;
            private final QuestStatus.Generic theSearchForCain;
            private final QuestStatus.Generic theForgottenTower;
            private final QuestStatus.Generic toolsOfTheTrade;
            private final QuestStatus.Generic sistersToTheSlaughter;

            @Override
            @SuppressWarnings({ "unchecked", "unused" })
            public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
                return switch (quest) {
                    case Quest.Act1.DenOfEvil q -> (S) denOfEvil();
                    case Quest.Act1.SistersBurialGrounds q -> (S) sistersBurialGrounds();
                    case Quest.Act1.TheSearchForCain q -> (S) theSearchForCain();
                    case Quest.Act1.TheForgottenTower q -> (S) theForgottenTower();
                    case Quest.Act1.ToolsOfTheTrade q -> (S) toolsOfTheTrade();
                    case Quest.Act1.SistersToTheSlaughter q -> (S) sistersToTheSlaughter();
                    default -> throw invalidQuest(this, quest);
                };
            }

            @Override
            public Map<Quest<?>, QuestStatus> all() {
                return new ImmutableMap.Builder<Quest<?>, QuestStatus>()
                        .put(Quest.DEN_OF_EVIL, denOfEvil())
                        .put(Quest.SISTERS_BURIAL_GROUNDS, sistersBurialGrounds())
                        .put(Quest.THE_SEARCH_FOR_CAIN, theSearchForCain())
                        .put(Quest.THE_FORGOTTEN_TOWER, theForgottenTower())
                        .put(Quest.TOOLS_OF_THE_TRADE, toolsOfTheTrade())
                        .put(Quest.SISTERS_TO_THE_SLAUGHTER, sistersToTheSlaughter())
                        .build();
            }

            public static final class Builder implements Progress.Quests.Builder<Builder> {
                @Override
                @SuppressWarnings("unused")
                public <Q extends Quest<S>, S extends QuestStatus> Builder set(Q quest, S status) {
                    return switch (quest) {
                        case Quest.Act1.DenOfEvil q -> denOfEvil((QuestStatus.Generic) status);
                        case Quest.Act1.SistersBurialGrounds q -> sistersBurialGrounds((QuestStatus.Generic) status);
                        case Quest.Act1.TheSearchForCain q -> theSearchForCain((QuestStatus.Generic) status);
                        case Quest.Act1.TheForgottenTower q -> theForgottenTower((QuestStatus.Generic) status);
                        case Quest.Act1.ToolsOfTheTrade q -> toolsOfTheTrade((QuestStatus.Generic) status);
                        case Quest.Act1.SistersToTheSlaughter q -> sistersToTheSlaughter((QuestStatus.Generic) status);
                        default -> throw invalidQuest(this, quest);
                    };
                }
            }
        }

        @Data
        @lombok.Builder
        public static final class Waypoints implements Progress.Waypoints {

            private boolean rogueEncampment;
            private boolean coldPlains;
            private boolean stonyField;
            private boolean darkWood;
            private boolean blackMarsh;
            private boolean outerCloister;
            private boolean jail;
            private boolean innerCloister;
            private boolean catacombs;

            @Override
            public boolean get(Area area) {
                return switch (area) {
                    case ROGUE_ENCAMPMENT -> rogueEncampment();
                    case COLD_PLAINS -> coldPlains();
                    case STONY_FIELD -> stonyField();
                    case DARK_WOOD -> darkWood();
                    case BLACK_MARSH -> blackMarsh();
                    case OUTER_CLOISTER -> outerCloister();
                    case JAIL_LEVEL_1 -> jail();
                    case INNER_CLOISTER -> innerCloister();
                    case CATACOMBS_LEVEL_2 -> catacombs();
                    default -> throw invalidWaypoint(this, area);
                };
            }

            @Override
            public Map<Area, Boolean> all() {
                return new ImmutableMap.Builder<Area, Boolean>()
                        .put(Area.ROGUE_ENCAMPMENT, rogueEncampment())
                        .put(Area.COLD_PLAINS, coldPlains())
                        .put(Area.STONY_FIELD, stonyField())
                        .put(Area.DARK_WOOD, darkWood())
                        .put(Area.BLACK_MARSH, blackMarsh())
                        .put(Area.OUTER_CLOISTER, outerCloister())
                        .put(Area.JAIL_LEVEL_1, jail())
                        .put(Area.INNER_CLOISTER, innerCloister())
                        .put(Area.CATACOMBS_LEVEL_2, catacombs())
                        .build();
            }

            public static final class Builder implements Progress.Waypoints.Builder<Builder> {
                @Override
                public Builder set(Area area, boolean active) {
                    return switch (area) {
                        case ROGUE_ENCAMPMENT -> rogueEncampment(active);
                        case COLD_PLAINS -> coldPlains(active);
                        case STONY_FIELD -> stonyField(active);
                        case DARK_WOOD -> darkWood(active);
                        case BLACK_MARSH -> blackMarsh(active);
                        case OUTER_CLOISTER -> outerCloister(active);
                        case JAIL_LEVEL_1 -> jail(active);
                        case INNER_CLOISTER -> innerCloister(active);
                        case CATACOMBS_LEVEL_2 -> catacombs(active);
                        default -> throw invalidWaypoint(this, area);
                    };
                }
            }
        }

        public static final class Builder extends Progress.Builder<Quests.Builder, Waypoints.Builder, Builder> { }
    }

    public static final class Act2 extends Progress<Act2.Quests, Act2.Waypoints> {

        @lombok.Builder
        public Act2(Difficulty difficulty, boolean visited, boolean introduced,
                Quests quests, Waypoints waypoints) {
            super(Act.from(difficulty, 1), visited, introduced, quests, waypoints);
        }

        @Data
        @lombok.Builder
        public static final class Quests implements Progress.Quests {

            private final QuestStatus.Generic radamentsLair;
            private final QuestStatus.Generic theHoradricStaff;
            private final QuestStatus.Generic taintedSun;
            private final QuestStatus.Generic arcaneSanctuary;
            private final QuestStatus.Generic theSummoner;
            private final QuestStatus.Generic theSevenTombs;

            @Override
            @SuppressWarnings({ "unchecked", "unused" })
            public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
                return switch (quest) {
                    case Quest.Act2.RadamentsLair q -> (S) radamentsLair();
                    case Quest.Act2.TheHoradricStaff q -> (S) theHoradricStaff();
                    case Quest.Act2.TaintedSun q -> (S) taintedSun();
                    case Quest.Act2.ArcaneSanctuary q -> (S) arcaneSanctuary();
                    case Quest.Act2.TheSummoner q -> (S) theSummoner();
                    case Quest.Act2.TheSevenTombs q -> (S) theSevenTombs();
                    default -> throw invalidQuest(this, quest);
                };
            }

            @Override
            public Map<Quest<?>, QuestStatus> all() {
                return new ImmutableMap.Builder<Quest<?>, QuestStatus>()
                        .put(Quest.RADAMENTS_LAIR, radamentsLair())
                        .put(Quest.THE_HORADRIC_STAFF, theHoradricStaff())
                        .put(Quest.TAINTED_SUN, taintedSun())
                        .put(Quest.ARCANE_SANCTUARY, arcaneSanctuary())
                        .put(Quest.THE_SUMMONER, theSummoner())
                        .put(Quest.THE_SEVEN_TOMBS, theSevenTombs())
                        .build();
            }

            public static final class Builder implements Progress.Quests.Builder<Builder> {
                @Override
                @SuppressWarnings("unused")
                public <Q extends Quest<S>, S extends QuestStatus> Builder set(Q quest, S status) {
                    return switch (quest) {
                        case Quest.Act2.RadamentsLair q -> radamentsLair((QuestStatus.Generic) status);
                        case Quest.Act2.TheHoradricStaff q -> theHoradricStaff((QuestStatus.Generic) status);
                        case Quest.Act2.TaintedSun q -> taintedSun((QuestStatus.Generic) status);
                        case Quest.Act2.ArcaneSanctuary q -> arcaneSanctuary((QuestStatus.Generic) status);
                        case Quest.Act2.TheSummoner q -> theSummoner((QuestStatus.Generic) status);
                        case Quest.Act2.TheSevenTombs q -> theSevenTombs((QuestStatus.Generic) status);
                        default -> throw invalidQuest(this, quest);
                    };
                }
            }
        }

        @Data
        @lombok.Builder
        public static final class Waypoints implements Progress.Waypoints {

            private boolean lutGholein;
            private boolean sewers;
            private boolean dryHills;
            private boolean hallsOfTheDead;
            private boolean farOasis;
            private boolean lostCity;
            private boolean palaceCellar;
            private boolean arcaneSanctuary;
            private boolean canyonOfTheMagi;

            @Override
            public boolean get(Area area) {
                return switch (area) {
                    case LUT_GHOLEIN -> lutGholein();
                    case LUT_GHOLEIN_SEWERS_LEVEL_2 -> sewers();
                    case DRY_HILLS -> dryHills();
                    case HALLS_OF_THE_DEAD_LEVEL_2 -> hallsOfTheDead();
                    case FAR_OASIS -> farOasis();
                    case LOST_CITY -> lostCity();
                    case PALACE_CELLAR_LEVEL_1 -> palaceCellar();
                    case ARCANE_SANCTUARY -> arcaneSanctuary();
                    case CANYON_OF_THE_MAGI -> canyonOfTheMagi();
                    default -> throw invalidWaypoint(this, area);
                };
            }

            @Override
            public Map<Area, Boolean> all() {
                return new ImmutableMap.Builder<Area, Boolean>()
                        .put(Area.LUT_GHOLEIN, lutGholein())
                        .put(Area.LUT_GHOLEIN_SEWERS_LEVEL_2, sewers())
                        .put(Area.DRY_HILLS, dryHills)
                        .put(Area.HALLS_OF_THE_DEAD_LEVEL_2, hallsOfTheDead())
                        .put(Area.FAR_OASIS, farOasis())
                        .put(Area.LOST_CITY, lostCity())
                        .put(Area.PALACE_CELLAR_LEVEL_1, palaceCellar())
                        .put(Area.ARCANE_SANCTUARY, arcaneSanctuary())
                        .put(Area.CANYON_OF_THE_MAGI, canyonOfTheMagi())
                        .build();
            }

            public static final class Builder implements Progress.Waypoints.Builder<Builder> {
                @Override
                public Builder set(Area area, boolean active) {
                    return switch (area) {
                        case LUT_GHOLEIN -> lutGholein(active);
                        case LUT_GHOLEIN_SEWERS_LEVEL_2 -> sewers(active);
                        case DRY_HILLS -> dryHills(active);
                        case HALLS_OF_THE_DEAD_LEVEL_2 -> hallsOfTheDead(active);
                        case FAR_OASIS -> farOasis(active);
                        case LOST_CITY -> lostCity(active);
                        case PALACE_CELLAR_LEVEL_1 -> palaceCellar(active);
                        case ARCANE_SANCTUARY -> arcaneSanctuary(active);
                        case CANYON_OF_THE_MAGI -> canyonOfTheMagi(active);
                        default -> throw invalidWaypoint(this, area);
                    };
                }
            }
        }

        public static final class Builder extends Progress.Builder<Quests.Builder, Waypoints.Builder, Builder> { }
    }

    public static final class Act3 extends Progress<Act3.Quests, Act3.Waypoints> {

        @lombok.Builder
        public Act3(Difficulty difficulty, boolean visited, boolean introduced,
                Quests quests, Waypoints waypoints) {
            super(Act.from(difficulty, 2), visited, introduced, quests, waypoints);
        }

        @Data
        @lombok.Builder
        public static final class Quests implements Progress.Quests {

            private final QuestStatus.Generic theGoldenBird;
            private final QuestStatus.Generic bladeOfTheOldReligion;
            private final QuestStatus.Generic khalimsWill;
            private final QuestStatus.Generic lamEsensTome;
            private final QuestStatus.Generic theBlackenedTemple;
            private final QuestStatus.Generic theGuardian;

            @Override
            @SuppressWarnings({ "unchecked", "unused" })
            public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
                return switch (quest) {
                    case Quest.Act3.TheGoldenBird q -> (S) theGoldenBird();
                    case Quest.Act3.BladeOfTheOldReligion q -> (S) bladeOfTheOldReligion();
                    case Quest.Act3.KhalimsWill q -> (S) khalimsWill();
                    case Quest.Act3.LamEsensTome q -> (S) lamEsensTome();
                    case Quest.Act3.TheBlackenedTemple q -> (S) theBlackenedTemple();
                    case Quest.Act3.TheGuardian q -> (S) theGuardian();
                    default -> throw invalidQuest(this, quest);
                };
            }

            @Override
            public Map<Quest<?>, QuestStatus> all() {
                return new ImmutableMap.Builder<Quest<?>, QuestStatus>()
                        .put(Quest.THE_GOLDEN_BIRD, theGoldenBird())
                        .put(Quest.BLADE_OF_THE_OLD_RELIGION, bladeOfTheOldReligion())
                        .put(Quest.KHALIMS_WILL, khalimsWill())
                        .put(Quest.LAM_ESENS_TOME, lamEsensTome())
                        .put(Quest.THE_BLACKENED_TEMPLE, theBlackenedTemple())
                        .put(Quest.THE_GUARDIAN, theGuardian())
                        .build();
            }

            public static final class Builder implements Progress.Quests.Builder<Builder> {
                @Override
                @SuppressWarnings("unused")
                public <Q extends Quest<S>, S extends QuestStatus> Builder set(Q quest, S status) {
                    return switch (quest) {
                        case Quest.Act3.TheGoldenBird q -> theGoldenBird((QuestStatus.Generic) status);
                        case Quest.Act3.BladeOfTheOldReligion q -> bladeOfTheOldReligion((QuestStatus.Generic) status);
                        case Quest.Act3.KhalimsWill q -> khalimsWill((QuestStatus.Generic) status);
                        case Quest.Act3.LamEsensTome q -> lamEsensTome((QuestStatus.Generic) status);
                        case Quest.Act3.TheBlackenedTemple q -> theBlackenedTemple((QuestStatus.Generic) status);
                        case Quest.Act3.TheGuardian q -> theGuardian((QuestStatus.Generic) status);
                        default -> throw invalidQuest(this, quest);
                    };
                }
            }
        }

        @Data
        @lombok.Builder
        public static final class Waypoints implements Progress.Waypoints {

            private boolean kurastDocks;
            private boolean spiderForest;
            private boolean greatMarsh;
            private boolean flayerJungle;
            private boolean lowerKurast;
            private boolean kurastBazaar;
            private boolean upperKurast;
            private boolean travincal;
            private boolean duranceOfHate;

            @Override
            public boolean get(Area area) {
                return switch (area) {
                    case KURAST_DOCKS -> kurastDocks();
                    case SPIDER_FOREST -> spiderForest();
                    case GREAT_MARSH -> greatMarsh();
                    case FLAYER_JUNGLE -> flayerJungle();
                    case LOWER_KURAST -> lowerKurast();
                    case KURAST_BAZAAR -> kurastBazaar();
                    case UPPER_KURAST -> upperKurast();
                    case TRAVINCAL -> travincal();
                    case DURANCE_OF_HATE_LEVEL_2 -> duranceOfHate();
                    default -> throw invalidWaypoint(this, area);
                };
            }

            @Override
            public Map<Area, Boolean> all() {
                return new ImmutableMap.Builder<Area, Boolean>()
                        .put(Area.KURAST_DOCKS, kurastDocks())
                        .put(Area.SPIDER_FOREST, spiderForest())
                        .put(Area.GREAT_MARSH, greatMarsh())
                        .put(Area.FLAYER_JUNGLE, flayerJungle())
                        .put(Area.LOWER_KURAST, lowerKurast())
                        .put(Area.KURAST_BAZAAR, kurastBazaar())
                        .put(Area.UPPER_KURAST, upperKurast())
                        .put(Area.TRAVINCAL, travincal())
                        .put(Area.DURANCE_OF_HATE_LEVEL_2, duranceOfHate())
                        .build();
            }

            public static final class Builder implements Progress.Waypoints.Builder<Builder> {
                @Override
                public Builder set(Area area, boolean active) {
                    return switch (area) {
                        case KURAST_DOCKS -> kurastDocks(active);
                        case SPIDER_FOREST -> spiderForest(active);
                        case GREAT_MARSH -> greatMarsh(active);
                        case FLAYER_JUNGLE -> flayerJungle(active);
                        case LOWER_KURAST -> lowerKurast(active);
                        case KURAST_BAZAAR -> kurastBazaar(active);
                        case UPPER_KURAST -> upperKurast(active);
                        case TRAVINCAL -> travincal(active);
                        case DURANCE_OF_HATE_LEVEL_2 -> duranceOfHate(active);
                        default -> throw invalidWaypoint(this, area);
                    };
                }
            }
        }

        public static final class Builder extends Progress.Builder<Quests.Builder, Waypoints.Builder, Builder> { }
    }

    public static final class Act4 extends Progress<Act4.Quests, Act4.Waypoints> {

        @lombok.Builder
        public Act4(Difficulty difficulty, boolean visited, boolean introduced,
                Quests quests, Waypoints waypoints) {
            super(Act.from(difficulty, 3), visited, introduced, quests, waypoints);
        }

        @Data
        @lombok.Builder
        public static final class Quests implements Progress.Quests {

            private final QuestStatus.Generic theFallenAngel;
            private final QuestStatus.Generic hellsForge;
            private final QuestStatus.Generic terrorsEnd;

            @Override
            @SuppressWarnings({ "unchecked", "unused" })
            public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
                return switch (quest) {
                    case Quest.Act4.TheFallenAngel q -> (S) theFallenAngel();
                    case Quest.Act4.HellsForge q -> (S) hellsForge();
                    case Quest.Act4.TerrorsEnd q -> (S) terrorsEnd();
                    default -> throw invalidQuest(this, quest);
                };
            }

            @Override
            public Map<Quest<?>, QuestStatus> all() {
                return new ImmutableMap.Builder<Quest<?>, QuestStatus>()
                        .put(Quest.THE_FALLEN_ANGEL, theFallenAngel())
                        .put(Quest.HELLS_FORGE, hellsForge())
                        .put(Quest.TERRORS_END, terrorsEnd())
                        .build();
            }

            public static final class Builder implements Progress.Quests.Builder<Builder> {
                @Override
                @SuppressWarnings("unused")
                public <Q extends Quest<S>, S extends QuestStatus> Builder set(Q quest, S status) {
                    return switch (quest) {
                        case Quest.Act4.TheFallenAngel q -> theFallenAngel((QuestStatus.Generic) status);
                        case Quest.Act4.HellsForge q -> hellsForge((QuestStatus.Generic) status);
                        case Quest.Act4.TerrorsEnd q -> terrorsEnd((QuestStatus.Generic) status);
                        default -> null;
                    };
                }
            }
        }

        @Data
        @lombok.Builder
        public static final class Waypoints implements Progress.Waypoints {

            private boolean thePandemoniumFortress;
            private boolean cityOfTheDamned;
            private boolean riverOfFlame;

            @Override
            public boolean get(Area area) {
                return switch (area) {
                    case THE_PANDEMONIUM_FORTRESS -> thePandemoniumFortress();
                    case CITY_OF_THE_DAMNED -> cityOfTheDamned();
                    case RIVER_OF_FLAME -> riverOfFlame();
                    default -> throw invalidWaypoint(this, area);
                };
            }

            @Override
            public Map<Area, Boolean> all() {
                return new ImmutableMap.Builder<Area, Boolean>()
                        .put(Area.THE_PANDEMONIUM_FORTRESS, thePandemoniumFortress())
                        .put(Area.CITY_OF_THE_DAMNED, cityOfTheDamned())
                        .put(Area.RIVER_OF_FLAME, riverOfFlame())
                        .build();
            }

            public static final class Builder implements Progress.Waypoints.Builder<Builder> {
                @Override
                public Builder set(Area area, boolean active) {
                    return switch (area) {
                        case THE_PANDEMONIUM_FORTRESS -> thePandemoniumFortress(active);
                        case CITY_OF_THE_DAMNED -> cityOfTheDamned(active);
                        case RIVER_OF_FLAME -> riverOfFlame(active);
                        default -> throw invalidWaypoint(this, area);
                    };
                }
            }
        }

        public static final class Builder extends Progress.Builder<Quests.Builder, Waypoints.Builder, Builder> { }
    }

    public static final class Act5 extends Progress<Act5.Quests, Act5.Waypoints> {

        @lombok.Builder
        public Act5(Difficulty difficulty, boolean visited, boolean introduced,
                Quests quests, Waypoints waypoints) {
            super(Act.from(difficulty, 4), visited, introduced, quests, waypoints);
        }

        @Data
        @lombok.Builder
        public static final class Quests implements Progress.Quests {

            private final QuestStatus.Generic siegeOnHarrogath;
            private final QuestStatus.Generic rescueOnMountainArreat;
            private final Quest.Act5.PrisonOfIce.Status prisonOfIce;
            private final QuestStatus.Generic betrayalOfHarrogath;
            private final QuestStatus.Generic riteOfPassage;
            private final QuestStatus.Generic eveOfDestruction;

            @Override
            @SuppressWarnings({ "unchecked", "unused" })
            public <Q extends Quest<S>, S extends QuestStatus> S get(Q quest) {
                return switch (quest) {
                    case Quest.Act5.SiegeOnHarrogath q -> (S) siegeOnHarrogath();
                    case Quest.Act5.RescueOnMountainArreat q -> (S) rescueOnMountainArreat();
                    case Quest.Act5.PrisonOfIce q -> (S) prisonOfIce();
                    case Quest.Act5.BetrayalOfHarrogath q -> (S) betrayalOfHarrogath();
                    case Quest.Act5.RiteOfPassage q -> (S) riteOfPassage();
                    case Quest.Act5.EveOfDestruction q -> (S) eveOfDestruction();
                    default -> throw invalidQuest(this, quest);
                };
            }

            @Override
            public Map<Quest<?>, QuestStatus> all() {
                return new ImmutableMap.Builder<Quest<?>, QuestStatus>()
                        .put(Quest.SIEGE_ON_HARROGATH, siegeOnHarrogath())
                        .put(Quest.RESCUE_ON_MOUNTAIN_ARREAT, rescueOnMountainArreat())
                        .put(Quest.PRISON_OF_ICE, prisonOfIce())
                        .put(Quest.BETRAYAL_OF_HARROGATH, betrayalOfHarrogath())
                        .put(Quest.RITE_OF_PASSAGE, riteOfPassage())
                        .put(Quest.EVE_OF_DESTRUCTION, eveOfDestruction())
                        .build();
            }

            public static final class Builder implements Progress.Quests.Builder<Builder> {
                @Override
                @SuppressWarnings("unused")
                public <Q extends Quest<S>, S extends QuestStatus> Builder set(Q quest, S status) {
                    return switch (quest) {
                        case Quest.Act5.SiegeOnHarrogath q -> siegeOnHarrogath((QuestStatus.Generic) status);
                        case Quest.Act5.RescueOnMountainArreat q ->
                                rescueOnMountainArreat((QuestStatus.Generic) status);
                        case Quest.Act5.PrisonOfIce q -> prisonOfIce((Quest.Act5.PrisonOfIce.Status) status);
                        case Quest.Act5.BetrayalOfHarrogath q -> betrayalOfHarrogath((QuestStatus.Generic) status);
                        case Quest.Act5.RiteOfPassage q -> riteOfPassage((QuestStatus.Generic) status);
                        case Quest.Act5.EveOfDestruction q -> eveOfDestruction((QuestStatus.Generic) status);
                        default -> throw invalidQuest(this, quest);
                    };
                }
            }
        }

        @Data
        @lombok.Builder
        public static final class Waypoints implements Progress.Waypoints {

            private boolean harrogath;
            private boolean frigidHighlands;
            private boolean arreatPlateau;
            private boolean crystallinePassage;
            private boolean hallsOfPain;
            private boolean glacialTrail;
            private boolean frozenTundra;
            private boolean theAncientsWay;
            private boolean worldstoneKeep;

            @Override
            public boolean get(Area area) {
                return switch (area) {
                    case HARROGATH -> harrogath();
                    case FRIGID_HIGHLANDS -> frigidHighlands();
                    case ARREAT_PLATEAU -> arreatPlateau();
                    case CRYSTALLINE_PASSAGE -> crystallinePassage();
                    case HALLS_OF_PAIN -> hallsOfPain();
                    case GLACIAL_TRAIL -> glacialTrail();
                    case FROZEN_TUNDRA -> frozenTundra();
                    case THE_ANCIENTS_WAY -> theAncientsWay();
                    case WORLDSTONE_KEEP_LEVEL_2 -> worldstoneKeep();
                    default -> throw invalidWaypoint(this, area);
                };
            }

            @Override
            public Map<Area, Boolean> all() {
                return new ImmutableMap.Builder<Area, Boolean>()
                        .put(Area.HARROGATH, harrogath())
                        .put(Area.FRIGID_HIGHLANDS, frigidHighlands())
                        .put(Area.ARREAT_PLATEAU, arreatPlateau())
                        .put(Area.CRYSTALLINE_PASSAGE, crystallinePassage())
                        .put(Area.HALLS_OF_PAIN, hallsOfPain())
                        .put(Area.GLACIAL_TRAIL, glacialTrail())
                        .put(Area.FROZEN_TUNDRA, frozenTundra())
                        .put(Area.THE_ANCIENTS_WAY, theAncientsWay())
                        .put(Area.WORLDSTONE_KEEP_LEVEL_2, worldstoneKeep())
                        .build();
            }

            public static final class Builder implements Progress.Waypoints.Builder<Builder> {
                @Override
                public Builder set(Area area, boolean active) {
                    return switch (area) {
                        case HARROGATH -> harrogath(active);
                        case FRIGID_HIGHLANDS -> frigidHighlands(active);
                        case ARREAT_PLATEAU -> arreatPlateau(active);
                        case CRYSTALLINE_PASSAGE -> crystallinePassage(active);
                        case HALLS_OF_PAIN -> hallsOfPain(active);
                        case GLACIAL_TRAIL -> glacialTrail(active);
                        case FROZEN_TUNDRA -> frozenTundra(active);
                        case THE_ANCIENTS_WAY -> theAncientsWay(active);
                        case WORLDSTONE_KEEP_LEVEL_2 -> worldstoneKeep(active);
                        default -> throw invalidWaypoint(this, area);
                    };
                }
            }
        }

        public static final class Builder extends Progress.Builder<Quests.Builder, Waypoints.Builder, Builder> { }
    }
}
