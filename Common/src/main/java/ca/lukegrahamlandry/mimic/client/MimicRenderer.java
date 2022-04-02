package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MimicRenderer extends GeoEntityRenderer<MimicEntity> {
    public MimicRenderer(EntityRendererProvider.Context manager){
        super(manager, new MimicModel());
    }
}
