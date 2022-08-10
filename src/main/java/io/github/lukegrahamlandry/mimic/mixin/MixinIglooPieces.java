package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.EndCityPieces;
import net.minecraft.world.gen.feature.structure.IglooPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(IglooPieces.Piece.class)
public class MixinIglooPieces {
    @Inject(at = @At("RETURN"), method = "handleDataMarker(Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IServerWorld;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;)V")
    private void handleDataMarker(String tag, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox p_186175_5_, CallbackInfo ci) {
        if (tag.equals("chest")){
            world.setBlock(pos, BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
            world.getBlockTicks().scheduleTick(pos, BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
        }
    }
}
