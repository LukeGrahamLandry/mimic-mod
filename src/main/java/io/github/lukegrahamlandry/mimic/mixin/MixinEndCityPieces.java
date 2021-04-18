package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.EndCityPieces;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(EndCityPieces.CityTemplate.class)
public class MixinEndCityPieces {
    @Inject(at = @At("RETURN"), method = "handleDataMarker(Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IServerWorld;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;)V")
    private void handleDataMarker(String tag, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox p_186175_5_, CallbackInfo ci) {
        MimicMain.LOGGER.debug("!! endcity mixin !!");

        if (tag.startsWith("Chest")){
            world.setBlock(pos, BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
            world.getBlockTicks().scheduleTick(pos, BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
        }
    }
}
