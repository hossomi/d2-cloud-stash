# D2 Cloud Stash

## Save file format (.d2s)

The save file contains all the character current state.

### Header

The header is a fixed-size section with file metadata and basic character state.

| Address  | Type           | Description                                                                                                                                                          |
|----------|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `0x0000` | `int`          | Signature (0x55AA55AA)                                                                                                                                               | 
| `0x0004` | `int`          | Version (99)                                                                                                                                                         | 
| `0x0008` | `int`          | File size                                                                                                                                                            | 
| `0x000C` | `int`          | Checksum                                                                                                                                                             | 
| `0x0010` | `int`          | **Active equipment:**<br/>`0` Main<br/>`1` Swap                                                                                                                      |
| `0x0014` | 16             | ?                                                                                                                                                                    |
| `0x0024` | `byte`         | **Character flags:**<br/>`0x40` Ladder<br/>`0x20` Expansion<br/>`0x08` Dead<br/>`0x04` Hardcore                                                                      | 
| `0x0025` | `byte`         | **Character progression:** incremented for each act boss killed; determines the character title                                                                      |
| `0x0026` | 2              | ?                                                                                                                                                                    |                                                                                                                                                              
| `0x0028` | `byte`         | **Character class:**<br/>`0` Amazon<br/>`1` Sorceress<br/>`2` Necromancer<br/>`3` Paladin<br/>`4` Barbarian<br/>`5` Druid<br/>`6` Assassin<br/>                      |
| `0x0029` | 2              | ?                                                                                                                                                                    |                                                                                                                                                              
| `0x002B` | `byte`         | Character level                                                                                                                                                      |
| `0x002C` | 4              | ?                                                                                                                                                                    | 
| `0x0030` | `int`          | Last played (epoch seconds)                                                                                                                                          | 
| `0x0034` | 4              | ?                                                                                                                                                                    | 
| `0x0038` | `int` x 16     | Skills assigned to the "Skill #" hotkeys from the game control settings; if no skill is assigned, the corresponding value is `-1`                                    | 
| `0x0078` | `int`          | Left mouse button skill (main equipment)                                                                                                                             | 
| `0x007C` | `int`          | Right mouse button skill (main equipment)                                                                                                                            | 
| `0x0080` | `int`          | Left mouse button skill (swap equipment)                                                                                                                             | 
| `0x0084` | `int`          | Right mouse button skill (swap equipment)                                                                                                                            | 
| `0x0088` | 32             | Character menu appearance (?)                                                                                                                                        |
| `0x00A8` | `byte` x 3     | **Difficulty status** (Normal, Nightmare and Hell):<br/>`0x80` Unlocked<br/>`0x78` ?<br/>`0x07` Current act (0-4)<br/>_Can be determined from character progression_ |
| `0x00AB` | `int`          | Map ID (?)                                                                                                                                                           |
| `0x00AF` | 2              | ?                                                                                                                                                                    |
| `0x00B1` | `short`        | Mercenary dead                                                                                                                                                       |
| `0x00B3` | `int`          | Mercenary ID                                                                                                                                                         |
| `0x00B7` | `short`        | Mercenary name ID                                                                                                                                                    |
| `0x00B9` | `short`        | Mercenary type                                                                                                                                                       |
| `0x00BB` | `int`          | Mercenary experience                                                                                                                                                 |
| `0x00BF` | 74             | ?                                                                                                                                                                    |
| `0x010B` | `string(16)`   | Character name                                                                                                                                                       |
| `0x011B` | 51             | ?                                                                                                                                                                    |
| `0x014F` | Quests (298)   | Quest status                                                                                                                                                         |
| `0x0279` | Waypoints (81) | Waypoint status                                                                                                                                                      |
| `0x02CA` | NPCs (51)      | NPC status                                                                                                                                                           |
| `0x02FD` | Attributes     | Character attributes                                                                                                                                                 |
|          | Skills         | Character skills                                                                                                                                                     |
|          | Items          | Character items                                                                                                                                                      |
