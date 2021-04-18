package io.github.lukegrahamlandry.mimic.events;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MimicMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AddSpawnsToBiomes {
    @SubscribeEvent
    public static void loadBiome(BiomeLoadingEvent event) {
        event.getSpawns().addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityInit.MIMIC.get(), 1, 1, 1));
    }

    @SubscribeEvent
    public static void spawnPlacement(FMLCommonSetupEvent event){
        EntitySpawnPlacementRegistry.register(EntityInit.MIMIC.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MimicEntity::checkSpawn);
    }
}
