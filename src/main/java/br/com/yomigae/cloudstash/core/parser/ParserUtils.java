package br.com.yomigae.cloudstash.core.parser;

import br.com.yomigae.cloudstash.core.io.D2Data;
import br.com.yomigae.cloudstash.core.model.Attribute;

import java.util.List;

public class ParserUtils {

    private static final List<SaveFileAttribute> SAVE_FILE_ATTRIBUTES = D2Data.readTableFile("/data/itemstatcost.txt")
            .filter(row -> row.has("CSvBits"))
            .map(row -> new SaveFileAttribute(row.get("Stat"), row.getInt("*ID"), row.getInt("CSvBits")))
            .toList();

    public static SaveFileAttribute saveFileAttribute(int id) {
        return SAVE_FILE_ATTRIBUTES.stream()
                .filter(s -> s.id == id)
                .findAny()
                .orElseThrow(() -> new D2DataException("Unknown save file attribute ID: " + id));
    }

    public record SaveFileAttribute(Attribute attribute, int id, int bits, int factor) {
        public SaveFileAttribute(Attribute attribute, int id, int bits) {
            this(attribute, id, bits, attribute == null ? 0 : switch (attribute) {
                case LIFE, MAX_LIFE, MANA, MAX_MANA, STAMINA, MAX_STAMINA -> 256;
                default -> 1;
            });
        }

        public SaveFileAttribute(String attribute, int id, int bits) {
            this(switch (attribute) {
                case "strength" -> Attribute.STRENGTH;
                case "energy" -> Attribute.ENERGY;
                case "dexterity" -> Attribute.DEXTERITY;
                case "vitality" -> Attribute.VITALITY;
                case "statpts" -> Attribute.STAT_POINTS;
                case "newskills" -> Attribute.SKILL_POINTS;
                case "hitpoints" -> Attribute.LIFE;
                case "maxhp" -> Attribute.MAX_LIFE;
                case "mana" -> Attribute.MANA;
                case "maxmana" -> Attribute.MAX_MANA;
                case "stamina" -> Attribute.STAMINA;
                case "maxstamina" -> Attribute.MAX_STAMINA;
                case "level" -> null;
                case "experience" -> Attribute.EXPERIENCE;
                case "gold" -> Attribute.GOLD;
                case "goldbank" -> Attribute.GOLD_BANK;
                default -> throw new D2DataException("Unknown save file attribute: " + attribute);
            }, id, bits);
        }
    }
}
