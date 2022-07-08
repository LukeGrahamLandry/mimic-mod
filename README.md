# Mimic Mod 

A Minecraft mod that adds a stealthy new mob.  

Using multiloader so it could build for fabric but in 1.18 geckolib-fabric doesnt support the official mappings so i gave up. Now it does but I havent gotten around to dealing with it yet. 

## Features 

- mimic monster that looks exactly like a chest
    - will wonder around looking for a chest to eat
    - if it cant find one after a while it will just sit down and wait
    - snaps to block to stealth when it eats something
    - stores items from eaten chests which can be removed from its gui and are dropped on death
    - if it doesn't eat a chest it generates random loot (data packs can override the mimic:default_mimic_loot loot table)
    - will attack you when you try to open it
    - takes extra damage from axes
    - spawn in: igloo, end city, woodland mansion, desert temple, jungle temple, nether fortress, stronghold, dungeon
- lock it with a Mimic Lock
    - it will not attack you 
    - has a chance to run away each time you steal an item
- tame it with a Mimic Key
    - then it will follow you and can be used as storage
    - shift right click it to make it sit and stealth
    - can only be opened while sitting
- shift right click a chest with a Mimic Heart to summon a mimic

## Dependencies 
- [Geckolib](https://www.curseforge.com/minecraft/mc-mods/geckolib)

## Credits 
- [Luke Graham Landry](https://github.com/LukeGrahamLandry) (Code)
- [Redrix](https://www.curseforge.com/members/redrixttv/projects) (Assets)
- [EpicNecromancer1](https://www.curseforge.com/members/epicnecromancer1) (Assets)

## TODO
- quark compat 
  - switch to instanceof ChestBlock instead of directly checking
  - change texture if instance of VariantChestBlock
- no spoilers
  - block Neat, ToroHealth, etc
  - make HWYLA et al think its a chest block somehow
- other mimics
  - ender chest
  - cake
- spawn in more structures including modded ones
  - repurposed structures