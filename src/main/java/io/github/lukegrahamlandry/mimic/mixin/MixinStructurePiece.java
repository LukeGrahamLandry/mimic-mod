package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(StructurePiece.class)
public abstract class MixinStructurePiece {
    @Inject(at = @At("RETURN"), method = "createChest(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Ljava/util/Random;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Z")
    private void createChest(ServerLevelAccessor world, BoundingBox p_191080_2_, Random rand, BlockPos pos, ResourceLocation loottable, @Nullable BlockState state, CallbackInfoReturnable<Boolean> callback) {
        MimicMain.LOGGER.debug("!! createChest mixin !!");

        BlockState topState = world.getBlockState(pos.above());
        if (topState.getBlock() == Blocks.STRUCTURE_BLOCK || topState.getBlock() == Blocks.VINE || topState.isAir()){
            world.setBlock(pos.above(), BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
            world.getBlockTicks().scheduleTick(pos.above(), BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
        }
    }
}
