package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.io.D2Strings;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Area {
    ROGUE_ENCAMPMENT("Rogue Encampment", 0, true),
    BLOOD_MOOR("Blood Moor", 0),
    DEN_OF_EVIL("Den of Evil", 0),
    COLD_PLAINS("Cold Plains", 0, true),
    CAVE_LEVEL_1("Cave Level 1", 0),
    CAVE_LEVEL_2("Cave Level 2", 0),
    BURIAL_GROUNDS("Burial Grounds", 0),
    CRYPT("Crypt", 0),
    MAUSOLEUM("Mausoleum", 0),
    STONY_FIELD("Stony Field", 0, true),
    TRISTRAM("Tristram", 0),
    UNDERGROUND_PASSAGE_LEVEL_1("Underground Passage Level 1", 0),
    UNDERGROUND_PASSAGE_LEVEL_2("Underground Passage Level 2", 0),
    DARK_WOOD("Dark Wood", 0, true),
    BLACK_MARSH("Black Marsh", 0, true),
    HOLE_LEVEL_1("Hole Level 1", 0),
    HOLE_LEVEL_2("Hole Level 2", 0),
    FORGOTTEN_TOWER("Forgotten Tower", 0),
    TOWER_CELLAR_LEVEL_1("Tower Cellar Level 1", 0),
    TOWER_CELLAR_LEVEL_2("Tower Cellar Level 2", 0),
    TOWER_CELLAR_LEVEL_3("Tower Cellar Level 3", 0),
    TOWER_CELLAR_LEVEL_4("Tower Cellar Level 4", 0),
    TOWER_CELLAR_LEVEL_5("Tower Cellar Level 5", 0),
    TAMOE_HIGHLAND("Tamoe Highland", 0),
    PIT_LEVEL_1("Pit Level 1", 0),
    PIT_LEVEL_2("Pit Level 2", 0),
    MONASTERY_GATE("Monastery Gate", 0),
    OUTER_CLOISTER("Outer Cloister", 0, true),
    BARRACKS("Barracks", 0),
    JAIL_LEVEL_1("Jail Level 1", 0, true),
    JAIL_LEVEL_2("Jail Level 2", 0),
    JAIL_LEVEL_3("Jail Level 3", 0),
    INNER_CLOISTER("Inner Cloister", 0, true),
    CATHEDRAL("Cathedral", 0),
    CATACOMBS_LEVEL_1("Catacombs Level 1", 0),
    CATACOMBS_LEVEL_2("Catacombs Level 2", 0, true),
    CATACOMBS_LEVEL_3("Catacombs Level 3", 0),
    CATACOMBS_LEVEL_4("Catacombs Level 4", 0),
    COW_LEVEL("Moo Moo Farm", 0),

    LUT_GHOLEIN("Lut Gholein", 1, true),
    LUT_GHOLEIN_SEWERS_LEVEL_1("Sewers Level 1", 1),
    LUT_GHOLEIN_SEWERS_LEVEL_2("Sewers Level 2", 1, true),
    LUT_GHOLEIN_SEWERS_LEVEL_3("Sewers Level 3", 1),
    ROCKY_WASTE("Rocky Waste", 1),
    STONY_TOMB_LEVEL_1("Stony Tomb Level 1", 1),
    STONY_TOMB_LEVEL_2("Stony Tomb Level 2", 1),
    DRY_HILLS("Dry Hills", 1, true),
    HALLS_OF_THE_DEAD_LEVEL_1("Halls of the Dead Level 1", 1),
    HALLS_OF_THE_DEAD_LEVEL_2("Halls of the Dead Level 2", 1, true),
    HALLS_OF_THE_DEAD_LEVEL_3("Halls of the Dead Level 3", 1),
    FAR_OASIS("Far Oasis", 1, true),
    MAGGOT_LAIR_LEVEL_1("Maggot Lair Level 1", 1),
    MAGGOT_LAIR_LEVEL_2("Maggot Lair Level 2", 1),
    MAGGOT_LAIR_LEVEL_3("Maggot Lair Level 3", 1),
    LOST_CITY("Lost City", 1, true),
    ANCIENT_TUNNELS("Ancient Tunnels", 1),
    VALLEY_OF_SNAKES("Valley of Snakes", 1),
    CLAW_VIPER_TEMPLE_LEVEL_1("Claw Viper Temple Level 1", 1),
    CLAW_VIPER_TEMPLE_LEVEL_2("Claw Viper Temple Level 2", 1),
    HAREM_LEVEL_1("Harem Level 1", 1),
    HAREM_LEVEL_2("Harem Level 2", 1),
    PALACE_CELLAR_LEVEL_1("Palace Cellar Level 1", 1, true),
    PALACE_CELLAR_LEVEL_2("Palace Cellar Level 2", 1),
    PALACE_CELLAR_LEVEL_3("Palace Cellar Level 3", 1),
    ARCANE_SANCTUARY("Arcane Sanctuary", 1, true),
    CANYON_OF_THE_MAGI("Canyon of the Magi", 1, true),
    TAL_RASHAS_TOMB("Tal Rasha's Tomb", 1),
    DURIELS_LAIR("Duriel's Lair", 1),

    KURAST_DOCKS("Kurast Docktown", 2, true),
    SPIDER_FOREST("Spider Forest", 2, true),
    SPIDER_CAVERN("Spider Cavern", 2),
    GREAT_MARSH("Great Marsh", 2, true),
    FLAYER_JUNGLE("Flayer Jungle", 2, true),
    SWAMPY_PIT_LEVEL_1("Swampy Pit Level 1", 2),
    SWAMPY_PIT_LEVEL_2("Swampy Pit Level 2", 2),
    SWAMPY_PIT_LEVEL_3("Swampy Pit Level 3", 2),
    FLAYER_DUNGEON_LEVEL_1("Flayer Dungeon Level 1", 2),
    FLAYER_DUNGEON_LEVEL_2("Flayer Dungeon Level 2", 2),
    FLAYER_DUNGEON_LEVEL_3("Flayer Dungeon Level 3", 2),
    LOWER_KURAST("Lower Kurast", 2, true),
    KURAST_BAZAAR("Kurast Bazaar", 2, true),
    RUINED_TEMPLE("Ruined Temple", 2),
    DISUSED_FANE("Disused Fane", 2),
    KURAST_SEWERS_LEVEL_1("Sewers Level 1", 2),
    KURAST_SEWERS_LEVEL_2("Sewers Level 2", 2),
    UPPER_KURAST("Upper Kurast", 2, true),
    FORGOTTEN_RELIQUARY("Forgotten Reliquary", 2),
    FORGOTTEN_TEMPLE("Forgotten Temple", 2),
    KURAST_CAUSEWAY("Kurast Causeway", 2),
    DISUSED_RELIQUARY("Disused Reliquary", 2),
    RUINED_FANE("Ruined Fane", 2),
    TRAVINCAL("Travincal", 2, true),
    DURANCE_OF_HATE_LEVEL_1("Durance of Hate Level 1", 2),
    DURANCE_OF_HATE_LEVEL_2("Durance of Hate Level 2", 2, true),
    DURANCE_OF_HATE_LEVEL_3("Durance of Hate Level 3", 2),

    THE_PANDEMONIUM_FORTRESS("The Pandemonium Fortress", 3, true),
    OUTER_STEPPES("Outer Steppes", 3),
    PLAINS_OF_DESPAIR("Plains of Despair", 3),
    CITY_OF_THE_DAMNED("City of the Damned", 3, true),
    RIVER_OF_FLAME("River of Flame", 3, true),
    CHAOS_SANCTUM("Chaos Sanctum", 3),

    HARROGATH("Harrogath", 4, true),
    BLOODY_FOOTHILLS("Bloody Foothills", 4),
    FRIGID_HIGHLANDS("Rigid Highlands", 4, true),
    ABADDON("Hell1", 4),
    ARREAT_PLATEAU("Arreat Plateau", 4, true),
    PIT_OF_ARCHERON("Hell2", 4),
    CRYSTALLINE_PASSAGE("Crystalized Cavern Level 1", 4, true),
    FROZEN_RIVER("Cellar of Pity", 4),
    NIHLATHAKS_TEMPLE("Nihlathaks Temple", 4),
    HALLS_OF_ANGUISH("Halls of Anguish", 4),
    HALLS_OF_PAIN("Halls of Death's Calling", 4, true),
    HALLS_OF_VAUGHT("Halls of Vaught", 4),
    GLACIAL_TRAIL("Crystalized Cavern Level 2", 4, true),
    DRIFTER_CAVERN("Echo Chamber", 4),
    FROZEN_TUNDRA("Tundra Wastelands", 4, true),
    INFERNAL_PIT("Hell3", 4),
    THE_ANCIENTS_WAY("Glacial Caves Level 1", 4, true),
    ICY_CELLAR("Glacial Caves Level 2", 4),
    ARREAT_SUMMIT("Rocky Summit", 4),
    WORLDSTONE_KEEP_LEVEL_1("The Worldstone Keep Level 1", 4),
    WORLDSTONE_KEEP_LEVEL_2("The Worldstone Keep Level 2", 4, true),
    WORLDSTONE_KEEP_LEVEL_3("The Worldstone Keep Level 3", 4),
    THRONE_OF_DESTRUCTION("Throne of Destruction", 4),
    THE_WORLDSTONE_CHAMBER("The Worldstone Chamber", 4);

    private final String label;
    private final int act;
    private final boolean waypoint;

    Area(String nameKey, int act) {
        this(nameKey, act, false);
    }

    Area(String nameKey, int act, boolean waypoint) {
        this.label = D2Strings.get(nameKey);
        this.act = act;
        this.waypoint = waypoint;
    }

    @Override
    public String toString() {
        return label;
    }
}
