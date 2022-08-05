package ca.lukegrahamlandry.mimic.init;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.ForgeGeoMimicEntity;
import ca.lukegrahamlandry.mimic.MimicMain;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<ForgeGeoMimicEntity>> MIMIC = ENTITY_TYPES.register(Constants.MIMIC_ENTITY_ID.getPath(),
            () -> EntityType.Builder.of(ForgeGeoMimicEntity::new, MobCategory.MISC).sized(1f, 1f)
                    .build(Constants.MIMIC_ENTITY_ID.toString()));
}
