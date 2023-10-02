# D2 Cloud Stash

## Save file format (.d2s)

The save file contains all the character current state.

### Header

The header is a fixed-size section with file metadata and basic character state.

| Address  | Type                         | Description                                                                                                                                     |
|----------|------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `0x0000` | `int`                        | Signature (0x55AA55AA)                                                                                                                          | 
| `0x0004` | `int`                        | Version (99)                                                                                                                                    | 
| `0x0008` | `int`                        | File size                                                                                                                                       | 
| `0x000C` | `int`                        | Checksum                                                                                                                                        | 
| `0x0010` | `int`                        | **Active equipment:**<br/>`0` Main<br/>`1` Swap                                                                                                 |
| `0x0014` | 16                           | ?                                                                                                                                               |
| `0x0024` | `byte`                       | **Character flags:**<br/>`0x40` Ladder<br/>`0x20` Expansion<br/>`0x08` Dead<br/>`0x04` Hardcore                                                 | 
| `0x0025` | `byte`                       | **Character progression:** incremented for each act boss killed; determines the character title                                                 |
| `0x0026` | 2                            | ?                                                                                                                                               |                                                                                                                                                              
| `0x0028` | `byte`                       | **Character class:**<br/>`0` Amazon<br/>`1` Sorceress<br/>`2` Necromancer<br/>`3` Paladin<br/>`4` Barbarian<br/>`5` Druid<br/>`6` Assassin<br/> |
| `0x0029` | 2                            | ?                                                                                                                                               |                                                                                                                                                              
| `0x002B` | `byte`                       | Character level                                                                                                                                 |
| `0x002C` | 4                            | ?                                                                                                                                               | 
| `0x0030` | `int`                        | Last played (epoch seconds)                                                                                                                     | 
| `0x0034` | 4                            | ?                                                                                                                                               | 
| `0x0038` | `int[16]`                    | Skills assigned to the "Skill #" hotkeys from the game control settings; if no skill is assigned, the corresponding value is `-1`               | 
| `0x0078` | `int`                        | Left mouse button skill (main equipment)                                                                                                        | 
| `0x007C` | `int`                        | Right mouse button skill (main equipment)                                                                                                       | 
| `0x0080` | `int`                        | Left mouse button skill (swap equipment)                                                                                                        | 
| `0x0084` | `int`                        | Right mouse button skill (swap equipment)                                                                                                       | 
| `0x0088` | 32                           | Character menu appearance (?)                                                                                                                   |
| `0x00A8` | `byte[3]`                    | **Difficulty status** (Normal, Nightmare and Hell):<br/>`0x80` Unlocked<br/>`0x78` ?<br/>`0x07` Current act (0-4)                               |
| `0x00AB` | `int`                        | Map ID (?)                                                                                                                                      |
| `0x00AF` | 2                            | ?                                                                                                                                               |
| `0x00B1` | `short`                      | Mercenary dead                                                                                                                                  |
| `0x00B3` | `int`                        | Mercenary seed (?)                                                                                                                              |
| `0x00B7` | `short`                      | Mercenary name ID                                                                                                                               |
| `0x00B9` | `short`                      | Mercenary type                                                                                                                                  |
| `0x00BB` | `int`                        | Mercenary experience                                                                                                                            |
| `0x00BF` | 74                           | ?                                                                                                                                               |
| `0x010B` | `char[16]`                   | Character name                                                                                                                                  |
| `0x011B` | 51                           | ?                                                                                                                                               |
| `0x014F` | [Quests](#quests) (298)      | Quests status                                                                                                                                   |
| `0x0279` | [Waypoints](#waypoints) (81) | Waypoints status                                                                                                                                |
| `0x02CA` | NPCs (51)                    | NPCs status                                                                                                                                     |
| `0x02FD` | Attributes                   | Character attributes                                                                                                                            |
|          | Skills                       | Character skills                                                                                                                                |
|          | Items                        | Character items                                                                                                                                 |

### Quests

Describes the status of all quests in the game, as well as some NPC introductions and act travelling.

| Address | Type                                        | Description                                  |
|---------|---------------------------------------------|----------------------------------------------|
| `0x00`  | `byte[4]`                                   | `Woo!`                                       |
| `0x04`  | 6                                           | ?                                            |
| `0x0A`  | [Difficulty Quests](#difficulty-quests) x 3 | Quests status for Normal, Nightmare and Hell |

#### Difficulty Quests

For each act, this structure describes if the player has travelled to the act, introduced to the main NPC and each quest
status. Note that the quest order may not be the same as in game.

Each quest data is contained in a `short`, individual bits representing milestones that are specific to the quest. The
only common milestone is the bit `0x0001` which marks the quest completion. See further below for details.

| Address | Type       | Description                                          |
|---------|------------|------------------------------------------------------|
| `0x00`  | `short`    | `1` if introduced to Warriv                          |
| `0x02`  | `short`    | Act 1.1: Den of Evil                                 |
| `0x04`  | `short`    | Act 1.2: Sister's Burial Grounds                     |
| `0x06`  | `short`    | Act 1.5: Tools of the Trade                          |
| `0x08`  | `short`    | Act 1.3: The Search for Cain                         |
| `0x0A`  | `short`    | Act 1.4: The Forgotten Tower                         |
| `0x0C`  | `short`    | Act 1.6: Sisters to the Slaughter                    |
| `0x0E`  | `short`    | `1` if travelled to Act 2                            |
| `0x10`  | `short`    | `1` if introduced to Jerhyn                          |
| `0x12`  | `short`    | Act 2.1: Radament's Lair                             |
| `0x14`  | `short`    | Act 2.2: The Hodradric Staff                         |
| `0x16`  | `short`    | Act 2.3: Tainted Sun                                 |
| `0x18`  | `short`    | Act 2.4: Arcane Sanctuary                            |
| `0x1A`  | `short`    | Act 2.5: The Summoner                                |
| `0x1C`  | `short`    | Act 2.6: The Seven Tombs                             |
| `0x1E`  | `short`    | `1` if travelled to Act 3                            |
| `0x20`  | `short`    | `1` if introduced to Hratli                          |
| `0x22`  | `short`    | Act 3.4: Lam Esen's Tome                             |
| `0x24`  | `short`    | Act 3.3: Khalim's Will                               |
| `0x26`  | `short`    | Act 3.2: Blade of the Old Religion                   |
| `0x28`  | `short`    | Act 3.1: The Golden Bird                             |
| `0x2A`  | `short`    | Act 3.5: The Blackened Temple                        |
| `0x2C`  | `short`    | Act 3.6: The Guardian                                |
| `0x2E`  | `short`    | `1` if travelled to Act 4                            |
| `0x30`  | `short`    | `1` if introduced to Act 4 (true if travelled)       |
| `0x32`  | `short`    | Act 4.1: The Fallen Angel                            |
| `0x34`  | `short`    | Act 4.2: Hell's Forge                                |
| `0x36`  | `short`    | Act 4.3: Terror's End                                |
| `0x38`  | `short[3]` | Empty (Act 4 has only 3 quests)                      |
| `0x3E`  | `short`    | `1` if travelled to Act 5                            |
| `0x40`  | `short`    | `1` if introduced to Cain in Act 5                   |
| `0x42`  | 4          | ?                                                    |
| `0x46`  | `short`    | Act 5.1: Siege on Harrogath                          |
| `0x48`  | `short`    | Act 5.2: Rescue on Mountain Arreat                   |
| `0x4A`  | `short`    | Act 5.3: Prison of Ice:<br/>`0x0080` Scroll consumed |
| `0x4C`  | `short`    | Act 5.4: Betrayal of Harrogath                       |
| `0x4E`  | `short`    | Act 5.5: Rite of Passage                             |
| `0x50`  | `short`    | Act 5.6: Eve of Destruction                          |
| `0x52`  | 14         | ?                                                    |

### Waypoints

Describes the status of all waypoints in the game.

| Address | Type                                              | Description                                    |
|---------|---------------------------------------------------|------------------------------------------------|
| `0x00`  | `byte[2]`                                         | `WS`                                           |
| `0x02`  | 6                                                 | ?                                              |
| `0x08`  | [Difficulty Waypoints](#difficulty-waypoints) x 3 | Waypoint status for Normal, Nightmare and Hell |

#### Difficulty Waypoints

For each act, this structure describes if the player has activated each waypoint, represented by individual bits in the
same order as in game. For example, `0x0000000001` is the status for the Rogue Encampment waypoint.

| Address | Type | Description         |
|---------|------|---------------------|
| `0x00`  | 2    | ?                   |
| `0x02`  | 5    | Waypoints activated |
| `0x07`  | 17   | ?                   |
