package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.EntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(StructurePiece.class)
public class MixinStructurePiece {
    @Inject(at = @At("RETURN"), method = "createChest(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/MutableBoundingBox;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/block/BlockState;)Z")
    private void createChest(IServerWorld world, MutableBoundingBox p_191080_2_, Random rand, BlockPos pos, ResourceLocation loottable, @Nullable BlockState state, CallbackInfoReturnable<Boolean> callback) {
        MimicMain.LOGGER.debug("!! createChest mixin !!");
        // world.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
        if (world.getBlockState(pos.above()).isAir()){
            MimicMain.LOGGER.debug("spawn mimic at " + pos.above());
            MimicEntity e = new MimicEntity(EntityInit.MIMIC.get(), world.getLevel());
            e.setPos(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
            world.addFreshEntity(e);
        }
    }
}
