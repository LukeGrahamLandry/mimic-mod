package io.github.lukegrahamlandry.mimic.client;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.init.ContainerInit;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MimicMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.MIMIC.get(), MimicRenderer::new);

        MenuScreens.register(ContainerInit.EVIL_MIMIC.get(), CustomChestScreen::createEvil);

        MenuScreens.register(ContainerInit.TAME_MIMIC.get(), CustomChestScreen::createTame);
    }
}