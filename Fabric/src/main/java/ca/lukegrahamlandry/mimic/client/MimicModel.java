package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.FabricGeoMimicEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MimicModel extends AnimatedGeoModel<FabricGeoMimicEntity> {
    @Override
    public ResourceLocation getModelResource(FabricGeoMimicEntity mimic) {
        return Constants.MODEL_LOC;
    }

    @Override
    public ResourceLocation getTextureResource(FabricGeoMimicEntity mimic) {
        if (mimic.isTamed()){
            return Constants.TAME_TEXTURE_LOC;
        } else {
            return Constants.EVIL_TEXTURE_LOC;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(FabricGeoMimicEntity mimic) {
        return Constants.ANIM_LOC;
    }
}