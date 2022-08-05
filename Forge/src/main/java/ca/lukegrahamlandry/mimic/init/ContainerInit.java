package ca.lukegrahamlandry.mimic.init;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.client.MimicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MOD_ID);

    public static final RegistryObject<MenuType<MimicContainer>> EVIL_MIMIC = CONTAINER_TYPES
            .register(Constants.EVIL_MIMIC_CONTAINER.getPath(), () -> IForgeMenuType.create((a, b, c) -> MimicContainer.create(Constants.EVIL_MIMIC_CONTAINER, a, b, c)));

    public static final RegistryObject<MenuType<MimicContainer>> TAME_MIMIC = CONTAINER_TYPES
            .register(Constants.TAME_MIMIC_CONTAINER.getPath(), () -> IForgeMenuType.create((a, b, c) -> MimicContainer.create(Constants.TAME_MIMIC_CONTAINER, a, b, c)));
    }
