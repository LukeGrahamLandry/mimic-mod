package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.IglooPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(IglooPieces.IglooPiece.class)
public class MixinIglooPieces {
    @Inject(at = @At("RETURN"), method = "handleDataMarker(Ljava/lang/String;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/ServerLevelAccessor;Ljava/util/Random;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)V")
    private void handleDataMarker(String tag, BlockPos pos, ServerLevelAccessor world, Random rand, BoundingBox p_186175_5_, CallbackInfo ci) {
        MimicMain.LOGGER.debug("!! igloo mixin !!");

        if (tag.equals("chest")){
            world.setBlock(pos, BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
            world.scheduleTick(pos, BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
        }
    }
}
