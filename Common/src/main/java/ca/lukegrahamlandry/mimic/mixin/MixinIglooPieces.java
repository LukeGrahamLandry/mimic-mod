package ca.lukegrahamlandry.mimic.mixin;

import ca.lukegrahamlandry.mimic.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.IglooPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IglooPieces.IglooPiece.class)
public class MixinIglooPieces {
    @Inject(at = @At("RETURN"), method = "handleDataMarker")
    private void handleDataMarker(String tag, BlockPos pos, ServerLevelAccessor world, RandomSource rand, BoundingBox box, CallbackInfo ci) {
        if (tag.equals("chest")){
            world.setBlock(pos, Constants.getMimicSpawnBlock().defaultBlockState(), 3);
            world.scheduleTick(pos, Constants.getMimicSpawnBlock(), 1);
        }
    }
}
