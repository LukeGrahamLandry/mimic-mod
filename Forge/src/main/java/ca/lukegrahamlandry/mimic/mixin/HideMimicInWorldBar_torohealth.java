package ca.lukegrahamlandry.mimic.mixin;

import ca.lukegrahamlandry.mimic.MimicForgeConfig;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HealthBarRenderer.class)
public abstract class HideMimicInWorldBar_torohealth {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true, remap = false)
    private static void render(PoseStack matrix, LivingEntity entity, double x, double y, float width, boolean inWorld, CallbackInfo ci){
        if (entity instanceof MimicEntity && ((MimicEntity)entity).isStealth()){
            ci.cancel();
        }

        if (MimicForgeConfig.shouldHaveNoHealthbar(entity)){
            ci.cancel();
        }
    }
}
