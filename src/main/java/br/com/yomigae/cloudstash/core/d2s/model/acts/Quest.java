package br.com.yomigae.cloudstash.core.d2s.model.acts;

import br.com.yomigae.cloudstash.core.io.D2Strings.D2String;
import br.com.yomigae.cloudstash.core.d2s.model.acts.QuestStatus.Generic;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static br.com.yomigae.cloudstash.core.io.D2Strings.D2String.d2String;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public sealed abstract class Quest<S extends QuestStatus> {

    // @formatter:off
    public static final Act1.DenOfEvil              DEN_OF_EVIL               = new Act1.DenOfEvil();
    public static final Act1.SistersBurialGrounds   SISTERS_BURIAL_GROUNDS    = new Act1.SistersBurialGrounds();
    public static final Act1.TheSearchForCain       THE_SEARCH_FOR_CAIN       = new Act1.TheSearchForCain();
    public static final Act1.TheForgottenTower      THE_FORGOTTEN_TOWER       = new Act1.TheForgottenTower();
    public static final Act1.ToolsOfTheTrade        TOOLS_OF_THE_TRADE        = new Act1.ToolsOfTheTrade();
    public static final Act1.SistersToTheSlaughter  SISTERS_TO_THE_SLAUGHTER  = new Act1.SistersToTheSlaughter();
    public static final Act2.RadamentsLair          RADAMENTS_LAIR            = new Act2.RadamentsLair();
    public static final Act2.TheHoradricStaff       THE_HORADRIC_STAFF        = new Act2.TheHoradricStaff();
    public static final Act2.TaintedSun             TAINTED_SUN               = new Act2.TaintedSun();
    public static final Act2.ArcaneSanctuary        ARCANE_SANCTUARY          = new Act2.ArcaneSanctuary();
    public static final Act2.TheSummoner            THE_SUMMONER              = new Act2.TheSummoner();
    public static final Act2.TheSevenTombs          THE_SEVEN_TOMBS           = new Act2.TheSevenTombs();
    public static final Act3.TheGoldenBird          THE_GOLDEN_BIRD           = new Act3.TheGoldenBird();
    public static final Act3.BladeOfTheOldReligion  BLADE_OF_THE_OLD_RELIGION = new Act3.BladeOfTheOldReligion();
    public static final Act3.KhalimsWill            KHALIMS_WILL              = new Act3.KhalimsWill();
    public static final Act3.LamEsensTome           LAM_ESENS_TOME            = new Act3.LamEsensTome();
    public static final Act3.TheBlackenedTemple     THE_BLACKENED_TEMPLE      = new Act3.TheBlackenedTemple();
    public static final Act3.TheGuardian            THE_GUARDIAN              = new Act3.TheGuardian();
    public static final Act4.TheFallenAngel         THE_FALLEN_ANGEL          = new Act4.TheFallenAngel();
    public static final Act4.HellsForge             HELLS_FORGE               = new Act4.HellsForge();
    public static final Act4.TerrorsEnd             TERRORS_END               = new Act4.TerrorsEnd();
    public static final Act5.SiegeOnHarrogath       SIEGE_ON_HARROGATH        = new Act5.SiegeOnHarrogath();
    public static final Act5.RescueOnMountainArreat RESCUE_ON_MOUNTAIN_ARREAT = new Act5.RescueOnMountainArreat();
    public static final Act5.PrisonOfIce            PRISON_OF_ICE             = new Act5.PrisonOfIce();
    public static final Act5.BetrayalOfHarrogath    BETRAYAL_OF_HARROGATH     = new Act5.BetrayalOfHarrogath();
    public static final Act5.RiteOfPassage          RITE_OF_PASSAGE           = new Act5.RiteOfPassage();
    public static final Act5.EveOfDestruction       EVE_OF_DESTRUCTION        = new Act5.EveOfDestruction();
    // @formatter:on

    String name;
    int act;

    @Override
    public String toString() {
        return name;
    }

    public static sealed abstract class Act1<S extends QuestStatus> extends Quest<S> {
        public Act1(D2String name) { super(name.toString(), 0); }

        public static final class DenOfEvil extends Act1<Generic> {
            private DenOfEvil() { super(d2String("qstsa1q1")); }
        }

        public static final class SistersBurialGrounds extends Act1<Generic> {
            private SistersBurialGrounds() { super(d2String("qstsa1q2")); }
        }

        public static final class TheSearchForCain extends Act1<Generic> {
            private TheSearchForCain() { super(d2String("qstsa1q4")); }
        }

        public static final class TheForgottenTower extends Act1<Generic> {
            private TheForgottenTower() { super(d2String("qstsa1q5")); }
        }

        public static final class ToolsOfTheTrade extends Act1<Generic> {
            private ToolsOfTheTrade() { super(d2String("qstsa1q3")); }
        }

        public static final class SistersToTheSlaughter extends Act1<Generic> {
            private SistersToTheSlaughter() { super(d2String("qstsa1q6")); }
        }
    }

    public static sealed abstract class Act2<S extends QuestStatus> extends Quest<S> {
        public Act2(D2String name) { super(name.toString(), 1); }

        public static final class RadamentsLair extends Act2<Generic> {
            private RadamentsLair() { super(d2String("qstsa2q1")); }
        }

        public static final class TheHoradricStaff extends Act2<Generic> {
            private TheHoradricStaff() { super(d2String("qstsa2q2")); }
        }

        public static final class TaintedSun extends Act2<Generic> {
            private TaintedSun() { super(d2String("qstsa2q3")); }
        }

        public static final class ArcaneSanctuary extends Act2<Generic> {
            private ArcaneSanctuary() { super(d2String("qstsa2q4")); }
        }

        public static final class TheSummoner extends Act2<Generic> {
            private TheSummoner() { super(d2String("qstsa2q5")); }
        }

        public static final class TheSevenTombs extends Act2<Generic> {
            private TheSevenTombs() { super(d2String("qstsa2q6")); }
        }
    }

    public static sealed abstract class Act3<S extends QuestStatus> extends Quest<S> {
        public Act3(D2String name) { super(name.toString(), 2); }

        public static final class TheGoldenBird extends Act3<Generic> {
            private TheGoldenBird() { super(d2String("qstsa3q4")); }
        }

        public static final class BladeOfTheOldReligion extends Act3<Generic> {
            private BladeOfTheOldReligion() { super(d2String("qstsa3q3")); }
        }

        public static final class KhalimsWill extends Act3<Generic> {
            private KhalimsWill() { super(d2String("qstsa3q2")); }
        }

        public static final class LamEsensTome extends Act3<Generic> {
            private LamEsensTome() { super(d2String("qstsa3q1")); }
        }

        public static final class TheBlackenedTemple extends Act3<Generic> {
            private TheBlackenedTemple() { super(d2String("qstsa3q5")); }
        }

        public static final class TheGuardian extends Act3<Generic> {
            private TheGuardian() { super(d2String("qstsa3q6")); }
        }
    }

    public static sealed abstract class Act4<S extends QuestStatus> extends Quest<S> {
        public Act4(D2String name) { super(name.toString(), 3); }

        public static final class TheFallenAngel extends Act4<Generic> {
            private TheFallenAngel() { super(d2String("qstsa4q1")); }
        }

        public static final class HellsForge extends Act4<Generic> {
            private HellsForge() { super(d2String("qstsa4q3")); }
        }

        public static final class TerrorsEnd extends Act4<Generic> {
            private TerrorsEnd() { super(d2String("qstsa4q2")); }
        }
    }

    public static sealed abstract class Act5<S extends QuestStatus> extends Quest<S> {
        public Act5(D2String name) { super(name.toString(), 4); }

        public static final class SiegeOnHarrogath extends Act5<Generic> {
            private SiegeOnHarrogath() { super(d2String("qstsa5q1")); }
        }

        public static final class PrisonOfIce extends Act5<PrisonOfIce.Status> {
            private PrisonOfIce() { super(d2String("qstsa5q3")); }

            @Builder
            public record Status(boolean completed, boolean scrollConsumed) implements QuestStatus { }
        }

        public static final class RescueOnMountainArreat extends Act5<Generic> {
            private RescueOnMountainArreat() { super(d2String("qstsa5q2")); }
        }

        public static final class BetrayalOfHarrogath extends Act5<Generic> {
            private BetrayalOfHarrogath() { super(d2String("qstsa5q4")); }
        }

        public static final class RiteOfPassage extends Act5<Generic> {
            private RiteOfPassage() { super(d2String("qstsa5q5")); }
        }

        public static final class EveOfDestruction extends Act5<Generic> {
            private EveOfDestruction() { super(d2String("qstsa5q6")); }
        }
    }
}
