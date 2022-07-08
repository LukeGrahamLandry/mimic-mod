package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.FabricGeoMimicEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MimicRenderer extends GeoEntityRenderer<FabricGeoMimicEntity> {
    public MimicRenderer(EntityRendererProvider.Context manager){
        super(manager, new MimicModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FabricGeoMimicEntity mimic) {
        if (mimic.isTamed()){
            return Constants.TAME_TEXTURE_LOC;
        } else {
            return Constants.EVIL_TEXTURE_LOC;
        }
    }
}
