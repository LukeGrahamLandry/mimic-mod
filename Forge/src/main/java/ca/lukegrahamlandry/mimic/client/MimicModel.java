package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.ForgeGeoMimicEntity;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MimicModel extends AnimatedGeoModel<ForgeGeoMimicEntity> {
    @Override
    public ResourceLocation getModelResource(ForgeGeoMimicEntity mimic) {
        return Constants.MODEL_LOC;
    }

    @Override
    public ResourceLocation getTextureResource(ForgeGeoMimicEntity mimic) {
        if (mimic.isTamed()){
            return Constants.TAME_TEXTURE_LOC;
        } else {
            return Constants.EVIL_TEXTURE_LOC;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(ForgeGeoMimicEntity mimic) {
        return Constants.ANIM_LOC;
    }
}