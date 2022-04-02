package ca.lukegrahamlandry.mimic.events;

import ca.lukegrahamlandry.mimic.MimicMain;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import ca.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickHandler {
    public static List<MimicSpawnData> spawns = new ArrayList<>();

    @SubscribeEvent
    public static void loadBiome(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START){
            for (MimicSpawnData spawn : spawns){
                spawn.time--;
                if (spawn.time <= 0){
                    spawnMimic(spawn);
                    spawns.remove(spawn);
                    MimicMain.LOGGER.debug(spawns);
                    return;
                }
            }
        }
    }

    private static void spawnMimic(MimicSpawnData spawn) {
        MimicMain.LOGGER.debug("try spawn mimic");

        BlockState state = spawn.world.getBlockState(spawn.pos);
        if (state.is(Blocks.CHEST)){
            ChestBlockEntity chest = (ChestBlockEntity) spawn.world.getBlockEntity(spawn.pos);
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
        public final Level world;

        public MimicSpawnData(Level level, BlockPos position){
            this.pos = position;
            this.time = 40;
            this.world = level;
        }
    }
}
