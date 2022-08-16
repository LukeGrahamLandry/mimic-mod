package ca.lukegrahamlandry.mimic;

import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import ca.lukegrahamlandry.mimic.init.BlockInit;
import ca.lukegrahamlandry.mimic.init.ContainerInit;
import ca.lukegrahamlandry.mimic.init.EntityInit;
import ca.lukegrahamlandry.mimic.init.ItemInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod("mimic")
public class MimicMain {
    public MimicMain() {
        GeckoLib.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
        ContainerInit.CONTAINER_TYPES.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);

        modEventBus.addListener(MimicMain::mobAttributes);

        MimicForgeConfig.loadConfig();
    }

    public static void mobAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.MIMIC.get(), MimicEntity.createMobAttributes().build());
    }
}
