package ca.lukegrahamlandry.mimic.mixin;

import ca.lukegrahamlandry.mimic.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.EndCityPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndCityPieces.EndCityPiece.class)
public class MixinEndCityPieces {
    @Inject(at = @At("RETURN"), method = "handleDataMarker(Ljava/lang/String;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/ServerLevelAccessor;Ljava/util/Random;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)V")
    private void handleDataMarker(String tag, BlockPos pos, ServerLevelAccessor world, Random rand, BoundingBox p_186175_5_, CallbackInfo ci) {
        if (tag.startsWith("Chest")){
            world.setBlock(pos, Constants.getMimicSpawnBlock().defaultBlockState(), 3);
            world.scheduleTick(pos, Constants.getMimicSpawnBlock(), 1);
        }
    }
}
