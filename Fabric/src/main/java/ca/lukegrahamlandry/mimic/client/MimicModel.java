package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MimicModel extends AnimatedGeoModel<MimicEntity> {
    @Override
    public ResourceLocation getModelLocation(MimicEntity mimic) {
        return Constants.MODEL_LOC;
    }

    @Override
    public ResourceLocation getTextureLocation(MimicEntity mimic) {
        if (mimic.isTamed()){
            return Constants.TAME_TEXTURE_LOC;
        } else {
            return Constants.EVIL_TEXTURE_LOC;
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MimicEntity mimic) {
        return Constants.ANIM_LOC;
    }
}