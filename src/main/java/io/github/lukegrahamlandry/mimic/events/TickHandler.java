package io.github.lukegrahamlandry.mimic.events;

import io.github.lukegrahamlandry.mimic.MimicConfig;
import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MimicMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickHandler {
    public static List<MimicSpawnData> spawns = new ArrayList<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START){
            for (MimicSpawnData spawn : spawns){
                spawn.time--;
                if (spawn.time <= 0){
                    spawnMimic(spawn);
                    spawns.remove(spawn);
                    return;
                }
            }
        }
    }

    private static void spawnMimic(MimicSpawnData spawn) {
        if (MimicConfig.debugMode.get()) MimicMain.LOGGER.debug("try summon mimic at " + spawn.pos);

        BlockState state = spawn.world.getBlockState(spawn.pos);
        if (state.is(Blocks.CHEST)){
            ChestTileEntity chest = (ChestTileEntity) spawn.world.getBlockEntity(spawn.pos);
            MimicEntity mimic = new MimicEntity(EntityInit.MIMIC.get(), spawn.world);

            // take items
            for (int i=0;i<chest.getContainerSize();i++){
                mimic.addItem(chest.getItem(i));
                chest.setItem(i, ItemStack.EMPTY);
            }

            spawn.world.setBlock(spawn.pos, Blocks.AIR.defaultBlockState(), 3);
            mimic.snapToBlock(spawn.pos, state.getValue(ChestBlock.FACING));
            mimic.setStealth(true);
            spawn.world.addFreshEntity(mimic);
        }
    }

    public static class MimicSpawnData {
        public final BlockPos pos;
        public int time;
        public final World world;

        public MimicSpawnData(World level, BlockPos position){
            this.pos = position;
            this.time = 40;
            this.world = level;
        }
    }
}
