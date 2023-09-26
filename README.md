# D2 Cloud Stash

## Save file format (.d2s)

The save file contains all the character current state.

### Header

The header is a fixed-size section with file metadata and basic character state.

| Address  | Type                    | Description                                                                                                                                     |
|----------|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `0x0000` | `int`                   | Signature (0x55AA55AA)                                                                                                                          | 
| `0x0004` | `int`                   | Version (99)                                                                                                                                    | 
| `0x0008` | `int`                   | File size                                                                                                                                       | 
| `0x000C` | `int`                   | Checksum                                                                                                                                        | 
| `0x0010` | `int`                   | **Active equipment:**<br/>`0` Main<br/>`1` Swap                                                                                                 |
| `0x0014` | 16                      | ?                                                                                                                                               |
| `0x0024` | `byte`                  | **Character flags:**<br/>`0x40` Ladder<br/>`0x20` Expansion<br/>`0x08` Dead<br/>`0x04` Hardcore                                                 | 
| `0x0025` | `byte`                  | **Character progression:** incremented for each act boss killed; determines the character title                                                 |
| `0x0026` | 2                       | ?                                                                                                                                               |                                                                                                                                                              
| `0x0028` | `byte`                  | **Character class:**<br/>`0` Amazon<br/>`1` Sorceress<br/>`2` Necromancer<br/>`3` Paladin<br/>`4` Barbarian<br/>`5` Druid<br/>`6` Assassin<br/> |
| `0x0029` | 2                       | ?                                                                                                                                               |                                                                                                                                                              
| `0x002B` | `byte`                  | Character level                                                                                                                                 |
| `0x002C` | 4                       | ?                                                                                                                                               | 
| `0x0030` | `int`                   | Last played (epoch seconds)                                                                                                                     | 
| `0x0034` | 4                       | ?                                                                                                                                               | 
| `0x0038` | `int[16]`               | Skills assigned to the "Skill #" hotkeys from the game control settings; if no skill is assigned, the corresponding value is `-1`               | 
| `0x0078` | `int`                   | Left mouse button skill (main equipment)                                                                                                        | 
| `0x007C` | `int`                   | Right mouse button skill (main equipment)                                                                                                       | 
| `0x0080` | `int`                   | Left mouse button skill (swap equipment)                                                                                                        | 
| `0x0084` | `int`                   | Right mouse button skill (swap equipment)                                                                                                       | 
| `0x0088` | 32                      | Character menu appearance (?)                                                                                                                   |
| `0x00A8` | `byte[3]`               | **Difficulty status** (Normal, Nightmare and Hell):<br/>`0x80` Unlocked<br/>`0x78` ?<br/>`0x07` Current act (0-4)                               |
| `0x00AB` | `int`                   | Map ID (?)                                                                                                                                      |
| `0x00AF` | 2                       | ?                                                                                                                                               |
| `0x00B1` | `short`                 | Mercenary dead                                                                                                                                  |
| `0x00B3` | `int`                   | Mercenary seed (?)                                                                                                                              |
| `0x00B7` | `short`                 | Mercenary name ID                                                                                                                               |
| `0x00B9` | `short`                 | Mercenary type                                                                                                                                  |
| `0x00BB` | `int`                   | Mercenary experience                                                                                                                            |
| `0x00BF` | 74                      | ?                                                                                                                                               |
| `0x010B` | `char[16]`              | Character name                                                                                                                                  |
| `0x011B` | 51                      | ?                                                                                                                                               |
| `0x014F` | [Quests](#quests) (298) | Quests status                                                                                                                                   |
| `0x0279` | Waypoints (81)          | Waypoints status                                                                                                                                |
| `0x02CA` | NPCs (51)               | NPCs status                                                                                                                                     |
| `0x02FD` | Attributes              | Character attributes                                                                                                                            |
|          | Skills                  | Character skills                                                                                                                                |
|          | Items                   | Character items                                                                                                                                 |

### Quests

Describes the status of all quests in the game, as well as some NPC introductions and act travelling.

| Address  | Type                                        | Description                                  |
|----------|---------------------------------------------|----------------------------------------------|
| `0x014F` | `byte[4]`                                   | `Woo!`                                       |
| `0x0153` | 6                                           | ?                                            |
| `0x0159` | [Difficulty Quests](#difficulty-quests) x 3 | Quests status for Normal, Nightmare and Hell |

#### Difficulty Quests

Each quest data is contained in a `short`. Each bit represents different milestones for each quest, which depends on the
quests themselves.

| Address | Type       | Description                        |
|---------|------------|------------------------------------|
| `0x00`  | `short`    | `1` if introduced to Warriv        |
| `0x02`  | `short[6]` | Act 1 quests                       |
| `0x0E`  | `short`    | `1` if travelled to Act 2          |
| `0x10`  | `short`    | `1` if introduced to Jerhyn        |
| `0x12`  | `short[6]` | Act 2 quests                       |
| `0x1E`  | `short`    | `1` if travelled to Act 3          |
| `0x20`  | `short`    | `1` if introduced to Hratli        |
| `0x22`  | `short[6]` | Act 3 quests                       |
| `0x2E`  | `short`    | `1` if travelled to Act 4          |
| `0x30`  | `short`    | `1` if travelled to Act 4 (again?) |
| `0x32`  | `short[3]` | Act 4 quests (last 3 are empty)    |
| `0x38`  | 6          | Empty slots for act 4 quests       |
| `0x4E`  | `short`    | `1` if travelled to Act 5          |
| `0x50`  | `short`    | `1` if introduced to Cain in Act 5 |
| `0x52`  | 4          | ?                                  |
| `0x56`  | `short[6]` | Act 5 quests                       |
| `0x62`  | 14         | ?                                  |

#### Quest Bits

For all quests, the first bit marks its completion. All other bits depends on the quest. For now, the only important
quest is Prison of Ice, which increases the character resistances if the Scroll of Resistance was consumed.

| Quest         | Description               |
|---------------|---------------------------|
| All           | `0x1000` `0` if completed |
| Prison of Ice | `0x0200` Scroll consumed  |
