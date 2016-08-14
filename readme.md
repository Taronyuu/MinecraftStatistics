## MinecraftStatistics
###### Simplest plugin to sync your player details to your MySQL database
***
For a online web view of the statistics of our players I searched for a simple plugin that just sync the most basic details of every online player to the database. Too bad I was unable to find one so I created this myself. It is really small and it only syncs the player data to the database on a frequency you can edit in the config.yml. And it changes the is_online flag on join/leave.

##### In a nutshell
- Syncs all basic player stats to the database on a frequency you can set
- Only 10kb!
- Uses only 1 table, and 1 row per player
- Data can be used everywhere
-is_online flag is updated on player join and quit
-Check below for all tracked stats

##### Commands
- /statsync => Syncs statistics of all player that are currently online to the database. Use permission `minecraftstatistics.sync` to limit access to this command

##### Installation
- Download the jar file and drop it into your plugins folder
- Start the server and close it.
- Edit your MySQL details in the config.yml file.
- (optional) change the frequency of the update task
- Start your server again, and check your table. A new row will be added if it doesn't exist for a player.

##### Questions? Ideas?
If you have any questions or ideas for this plugin, don't hesitate to share it. I'm more than happy to open source this plugin or add it myself. (This plugin will become open source if people like it)

##### Available stats
```
DAMAGE_DEALT
DAMAGE_TAKEN
DEATHS
MOB_KILLS
PLAYER_KILLS
FISH_CAUGHT
ANIMALS_BRED
TREASURE_FISHED
JUNK_FISHED
LEAVE_GAME
JUMP
PLAY_ONE_TICK
WALK_ONE_CM
SWIM_ONE_CM
FALL_ONE_CM
SNEAK_TIME
CLIMB_ONE_CM
FLY_ONE_CM
MINECART_ONE_CM
BOAT_ONE_CM
PIG_ONE_CM
HORSE_ONE_CM
SPRINT_ONE_CM
CROUCH_ONE_CM
AVIATE_ONE_CM
TIME_SINCE_DEATH
TALKED_TO_VILLAGER
TRADED_WITH_VILLAGER
CAKE_SLICES_EATEN
CAULDRON_FILLED
CAULDRON_USED
ARMOR_CLEANED
BANNER_CLEANED
BREWINGSTAND_INTERACTION
BEACON_INTERACTION
DROPPER_INSPECTED
HOPPER_INSPECTED
DISPENSER_INSPECTED
NOTEBLOCK_PLAYED
NOTEBLOCK_TUNED
FLOWER_POTTED
TRAPPED_CHEST_TRIGGERED
ENDERCHEST_OPENED
ITEM_ENCHANTED
RECORD_PLAYED
FURNACE_INTERACTION
CRAFTING_TABLE_INTERACTION
CHEST_OPENED
SLEEP_IN_BED
```