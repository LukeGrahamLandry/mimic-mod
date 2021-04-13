package io.github.lukegrahamlandry.mimic.init;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.client.MimicContainer;
import io.github.lukegrahamlandry.mimic.items.MimicKeyItem;
import io.github.lukegrahamlandry.mimic.items.MimicLockItem;
import io.github.lukegrahamlandry.mimic.items.ModSpawnEgg;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MimicMain.MOD_ID);

    public static final RegistryObject<ContainerType<MimicContainer>> EVIL_MIMIC = CONTAINER_TYPES
            .register("evil_mimic", () -> IForgeContainerType.create(MimicContainer::create));

    public static final RegistryObject<ContainerType<MimicContainer>> TAME_MIMIC = CONTAINER_TYPES
            .register("tame_mimic", () -> IForgeContainerType.create(MimicContainer::create));
    }
