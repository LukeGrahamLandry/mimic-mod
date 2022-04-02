package io.github.lukegrahamlandry.mimic.init;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.client.MimicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MimicMain.MOD_ID);

    public static final RegistryObject<MenuType<MimicContainer>> EVIL_MIMIC = CONTAINER_TYPES
            .register("evil_mimic", () -> IForgeContainerType.create(MimicContainer::create));

    public static final RegistryObject<MenuType<MimicContainer>> TAME_MIMIC = CONTAINER_TYPES
            .register("tame_mimic", () -> IForgeContainerType.create(MimicContainer::create));
    }
