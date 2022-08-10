package io.github.lukegrahamlandry.mimic.blocks;

import io.github.lukegrahamlandry.mimic.MimicConfig;
import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

// this block exists because when i try to spawn a mob in handleDataMarker (mixin) it just hangs forever
// so instead i place a block and schedule a tick to turn it into a mob

public class SingleMimicSpawner extends Block {
    static Random rand = new Random();
    public SingleMimicSpawner(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        if (rand.nextInt(MimicConfig.mimicSpawnChance.get()) == 0){
            if (MimicConfig.debugMode.get()) MimicMain.LOGGER.debug("spawn worldgen mimic at " + pos);
            MimicEntity e = new MimicEntity(EntityInit.MIMIC.get(), world);
            e.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            world.addFreshEntity(e);
            e.consumeChest(pos.below());
        }
    }

    /*
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
     */
}