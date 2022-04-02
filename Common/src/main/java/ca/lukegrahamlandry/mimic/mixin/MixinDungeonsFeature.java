package ca.lukegrahamlandry.mimic.mixin;

import ca.lukegrahamlandry.mimic.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(Feature.class)
public class MixinDungeonsFeature {
    @Inject(at = @At("HEAD"), method = "safeSetBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/function/Predicate;)V")
    private void safeSetBlock(WorldGenLevel world, BlockPos blockpos1, BlockState blockstate, Predicate<BlockState> predicate, CallbackInfo ci) {
        if (predicate.test(world.getBlockState(blockpos1))) {
            if (blockstate.is(Blocks.CHEST)){
                world.setBlock(blockpos1.above(), Constants.getMimicSpawnBlock().defaultBlockState(), 3);
                world.scheduleTick(blockpos1.above(), Constants.getMimicSpawnBlock(), 1);
            }
        }
    }
}


