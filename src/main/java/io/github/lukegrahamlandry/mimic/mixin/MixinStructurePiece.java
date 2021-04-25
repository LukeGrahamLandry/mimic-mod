package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StrongholdPieces;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(StructurePiece.class)
public abstract class MixinStructurePiece {
    @Inject(at = @At("RETURN"), method = "createChest(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/MutableBoundingBox;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/block/BlockState;)Z")
    private void createChest(IServerWorld world, MutableBoundingBox p_191080_2_, Random rand, BlockPos pos, ResourceLocation loottable, @Nullable BlockState state, CallbackInfoReturnable<Boolean> callback) {
        MimicMain.LOGGER.debug("!! createChest mixin !!");

        BlockState topState = world.getBlockState(pos.above());
        if (topState.getBlock() == Blocks.STRUCTURE_BLOCK || topState.getBlock() == Blocks.VINE || topState.isAir()){
            world.setBlock(pos.above(), BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
            world.getBlockTicks().scheduleTick(pos.above(), BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
        }
    }
}
