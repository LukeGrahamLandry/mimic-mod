package io.github.lukegrahamlandry.mimic.init;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.items.FakeChestItem;
import io.github.lukegrahamlandry.mimic.items.MimicKeyItem;
import io.github.lukegrahamlandry.mimic.items.MimicLockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MimicMain.MOD_ID);

    public static final RegistryObject<Item> MIMIC_KEY = ITEMS.register("mimic_key", () -> new MimicKeyItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MIMIC_LOCK = ITEMS.register("mimic_lock", () -> new MimicLockItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MIMIC_SPAWN_EGG = ITEMS.register("mimic_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.MIMIC, 0xad7110, 0x0, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> FAKE_CHEST = ITEMS.register("fake_chest", () -> new FakeChestItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
