package ca.lukegrahamlandry.mimic.mixin;

import ca.lukegrahamlandry.mimic.MimicForgeConfig;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.display.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HideMimicHealthBar_torohealth {
    @Shadow public abstract LivingEntity getEntity();

    @Inject(at = @At("HEAD"), method = "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/torocraft/torohealth/config/Config;)V", cancellable = true, remap = false)
    private void draw(PoseStack matrix, Config config, CallbackInfo ci){
        if (getEntity() instanceof MimicEntity && ((MimicEntity)getEntity()).isStealth()){
            ci.cancel();
        }

        if (MimicForgeConfig.shouldHaveNoHealthbar(getEntity())){
            ci.cancel();
        }
    }
}
