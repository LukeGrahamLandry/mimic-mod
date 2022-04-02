package ca.lukegrahamlandry.mimic.init;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.items.FakeChestItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> MIMIC_KEY = ITEMS.register(Constants.MIMIC_KEY_ID.getPath(), () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MIMIC_LOCK = ITEMS.register(Constants.MIMIC_LOCK_ID.getPath(), () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MIMIC_SPAWN_EGG = ITEMS.register("mimic_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.MIMIC, 0xad7110, 0x0, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> FAKE_CHEST = ITEMS.register("fake_chest", () -> new FakeChestItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
