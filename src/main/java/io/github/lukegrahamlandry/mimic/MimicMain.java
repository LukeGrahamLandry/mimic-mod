package io.github.lukegrahamlandry.mimic;

import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.ContainerInit;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import io.github.lukegrahamlandry.mimic.init.ItemInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("mimic")
public class MimicMain {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "mimic";

    public MimicMain() {
        GeckoLib.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
        ContainerInit.CONTAINER_TYPES.register(modEventBus);

        modEventBus.addListener(MimicMain::mobAttributes);
    }

    public static void mobAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.MIMIC.get(), MimicEntity.createMobAttributes().build());
    }
}
