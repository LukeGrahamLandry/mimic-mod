package io.github.lukegrahamlandry.mimic.init;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.items.MimicKeyItem;
import io.github.lukegrahamlandry.mimic.items.MimicLockItem;
import io.github.lukegrahamlandry.mimic.items.ModSpawnEgg;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MimicMain.MOD_ID);

    public static final RegistryObject<Item> MIMIC_KEY = ITEMS.register("mimic_key", () -> new MimicKeyItem(new Item.Properties()));

    public static final RegistryObject<Item> MIMIC_LOCK = ITEMS.register("mimic_lock", () -> new MimicLockItem(new Item.Properties()));

    public static final RegistryObject<Item> MIMIC_SPAWN_EGG = ITEMS.register("mimic_spawn_egg", () -> new ModSpawnEgg(EntityInit.MIMIC, 0xad7110, 0x0, new Item.Properties()));
}
