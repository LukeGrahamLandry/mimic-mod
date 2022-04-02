package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.init.ContainerInit;
import ca.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.MIMIC.get(), MimicRenderer::new);

        MenuScreens.register(ContainerInit.EVIL_MIMIC.get(), CustomChestScreen::createEvil);

        MenuScreens.register(ContainerInit.TAME_MIMIC.get(), CustomChestScreen::createTame);
    }
}