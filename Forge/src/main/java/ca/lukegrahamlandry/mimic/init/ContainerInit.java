package ca.lukegrahamlandry.mimic.init;

import ca.lukegrahamlandry.mimic.MimicMain;
import ca.lukegrahamlandry.mimic.client.MimicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

    public static final RegistryObject<MenuType<MimicContainer>> EVIL_MIMIC = CONTAINER_TYPES
            .register("evil_mimic", () -> IForgeMenuType.create(MimicContainer::create));

    public static final RegistryObject<MenuType<MimicContainer>> TAME_MIMIC = CONTAINER_TYPES
            .register("tame_mimic", () -> IForgeMenuType.create(MimicContainer::create));
    }
