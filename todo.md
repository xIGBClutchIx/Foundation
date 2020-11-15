# Todo - In order of importance

* Module System 
    * Supports loading/unloading/reloading in runtime.
        * Soft reloading - Does not unload the code. Will not load/unload new/old modules. Basically just the Bukkit system.
        * Hard reloading - This will do a full unload of the classes. Loads/Unloads new/old modules.
    * Command system
        * Reloads in runtime too.
        * Different override states - Player/Command Blocks/Console/Unknown
            * Any command/sub-command can specify these.
            * Permissions per state/sub-command?
    * Dependency system
        * Works like current Bukkit system.
        * Possibly go with a more Forge style approach of version checking and such.
* Data Storage System
    * Conduit data storage system inspiration. Which is inspired by Roblox.
    * Multiple types
        * Local file
            * Json
            * Serialized
        * Cache
        * Database
* Players.
    * Better scoreboard system?
    * Wither Bar?
    * Per player data storage?
        * Managers that are per session or global.
            * Global Example - Money
            * Session Example - K/D
                * Able to be easily save this information like global.
