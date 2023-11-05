package br.com.yomigae.cloudstash.core.model.progression.quest;

import br.com.yomigae.cloudstash.core.io.D2Strings.D2String;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static br.com.yomigae.cloudstash.core.io.D2Strings.D2String.d2String;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public sealed abstract class Quest<Q extends Quest<Q, S>, S extends QuestStatus<Q, S>> {

    // @formatter:off
    public static final Act1.DenOfEvil              DEN_OF_EVIL               = new Act1.DenOfEvil();
    public static final Generic                     SISTERS_BURIAL_GROUNDS    = new Generic(d2String("qstsa1q2"), 0);
    public static final Generic                     THE_SEARCH_FOR_CAIN       = new Generic(d2String("qstsa1q4"), 0);
    public static final Generic                     THE_FORGOTTEN_TOWER       = new Generic(d2String("qstsa1q5"), 0);
    public static final Generic                     TOOLS_OF_THE_TRADE        = new Generic(d2String("qstsa1q3"), 0);
    public static final Generic                     SISTERS_TO_THE_SLAUGHTER  = new Generic(d2String("qstsa1q6"), 0);
    public static final Generic                     RADAMENTS_LAIR            = new Generic(d2String("qstsa2q1"), 1);
    public static final Generic                     THE_HORADRIC_STAFF        = new Generic(d2String("qstsa2q2"), 1);
    public static final Generic                     TAINTED_SUN               = new Generic(d2String("qstsa2q3"), 1);
    public static final Generic                     ARCANE_SANCTUARY          = new Generic(d2String("qstsa2q4"), 1);
    public static final Generic                     THE_SUMMONER              = new Generic(d2String("qstsa2q5"), 1);
    public static final Generic                     THE_SEVEN_TOMBS           = new Generic(d2String("qstsa2q6"), 1);
    public static final Generic                     THE_GOLDEN_BIRD           = new Generic(d2String("qstsa3q4"), 2);
    public static final Generic                     BLADE_OF_THE_OLD_RELIGION = new Generic(d2String("qstsa3q3"), 2);
    public static final Generic                     KHALIMS_WILL              = new Generic(d2String("qstsa3q2"), 2);
    public static final Generic                     LAM_ESENS_TOME            = new Generic(d2String("qstsa3q1"), 2);
    public static final Generic                     THE_BLACKENED_TEMPLE      = new Generic(d2String("qstsa3q5"), 2);
    public static final Generic                     THE_GUARDIAN              = new Generic(d2String("qstsa3q6"), 2);
    public static final Generic                     THE_FALLEN_ANGEL          = new Generic(d2String("qstsa4q1"), 3);
    public static final Generic                     HELLS_FORGE               = new Generic(d2String("qstsa4q3"), 3);
    public static final Generic                     TERRORS_END               = new Generic(d2String("qstsa4q2"), 3);
    public static final Generic                     SIEGE_ON_HARROGATH        = new Generic(d2String("qstsa5q1"), 4);
    public static final Act5.RescueOnMountainArreat RESCUE_ON_MOUNTAIN_ARREAT = new Act5.RescueOnMountainArreat();
    public static final Act5.PrisonOfIce            PRISON_OF_ICE             = new Act5.PrisonOfIce();
    public static final Generic                     BETRAYAL_OF_HARROGATH     = new Generic(d2String("qstsa5q4"), 4);
    public static final Generic                     RITE_OF_PASSAGE           = new Generic(d2String("qstsa5q5"), 4);
    public static final Generic                     EVE_OF_DESTRUCTION        = new Generic(d2String("qstsa5q6"), 4);
    // @formatter:on

    String name;
    int act;

    @Override
    public String toString() {
        return name;
    }

    static final class Generic extends Quest<Generic, Generic.Status> {
        public Generic(D2String name, int act) {
            this(name.toString(), act);
        }

        public Generic(String name, int act) {
            super(name, act);
        }

        public record Status(Generic quest, boolean completed) implements QuestStatus<Generic, Status> { }
    }

    static sealed abstract class Act1<Q extends Act1<Q, S>, S extends QuestStatus<Q, S>> extends Quest<Q, S> {
        public Act1(D2String name) {
            this(name.toString());
        }

        public Act1(String name) {
            super(name, 0);
        }

        static final class DenOfEvil extends Act1<DenOfEvil, DenOfEvil.Status> {

            public DenOfEvil() {
                super(d2String("qstsa1q1"));
            }

            public record Status(
                    DenOfEvil quest,
                    boolean completed) implements QuestStatus<DenOfEvil, Status> { }
        }
    }

    static sealed abstract class Act5<Q extends Act5<Q, S>, S extends QuestStatus<Q, S>> extends Quest<Q, S> {

        public Act5(D2String name) {
            this(name.toString());
        }

        public Act5(String name) {
            super(name, 4);
        }

        static final class PrisonOfIce extends Act5<PrisonOfIce, PrisonOfIce.Status> {

            public PrisonOfIce() {
                super(d2String("qstsa5q3"));
            }

            public record Status(
                    PrisonOfIce quest,
                    boolean completed,
                    boolean scrollConsumed)
                    implements QuestStatus<PrisonOfIce, Status> { }
        }

        static final class RescueOnMountainArreat extends Act5<RescueOnMountainArreat, RescueOnMountainArreat.Status> {
            public RescueOnMountainArreat() {
                super(d2String("qstsa5q2"));
            }

            public record Status(
                    RescueOnMountainArreat quest,
                    boolean completed)
                    implements QuestStatus<RescueOnMountainArreat, RescueOnMountainArreat.Status> { }
        }
    }
}
