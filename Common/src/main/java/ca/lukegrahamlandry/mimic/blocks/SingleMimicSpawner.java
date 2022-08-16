package ca.lukegrahamlandry.mimic.blocks;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import ca.lukegrahamlandry.mimic.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

// this block exists because when i try to spawn a mob in handleDataMarker (mixin) it just hangs forever
// so instead i place a block and schedule a tick to turn it into a mob

public class SingleMimicSpawner extends Block {
    static Random rand = new Random();
    public SingleMimicSpawner(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        if (rand.nextInt(Services.PLATFORM.mimicSpawnChance()) == 0){
            MimicEntity e = (MimicEntity) Registry.ENTITY_TYPE.get(Constants.MIMIC_ENTITY_ID).create(world);
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