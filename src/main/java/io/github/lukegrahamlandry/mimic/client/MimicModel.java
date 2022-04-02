package io.github.lukegrahamlandry.mimic.client;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MimicModel extends AnimatedGeoModel<MimicEntity> {
    @Override
    public ResourceLocation getModelLocation(MimicEntity mimic) {
        return new ResourceLocation(MimicMain.MOD_ID, "geo/mimic.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MimicEntity mimic) {
        if (mimic.isTamed()){
            return new ResourceLocation(MimicMain.MOD_ID, "textures/entity/tamed_mimic.png");
        } else {
            return new ResourceLocation(MimicMain.MOD_ID, "textures/entity/evil_mimic.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MimicEntity mimic) {
        return new ResourceLocation(MimicMain.MOD_ID, "animations/mimic.animation.json");
    }
}