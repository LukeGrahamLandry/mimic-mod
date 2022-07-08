package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.FabricGeoMimicEntity;
import ca.lukegrahamlandry.mimic.ModMain;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModMain.mimicEntityType, MimicRenderer::new);

        MenuScreens.register(ModMain.evilMimicMenuType, CustomChestScreen::createEvil);
        MenuScreens.register(ModMain.tameMimicMenuType, CustomChestScreen::createTame);
    }
}
