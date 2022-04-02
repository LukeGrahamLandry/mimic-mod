package io.github.lukegrahamlandry.mimic.items;

import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;

import javax.annotation.Nullable;

public class ModSpawnEgg extends SpawnEggItem {
    // need this class because items are registered before entities
    // yes i should have used a more generic thing here than MimicEntity
    // go fuck yourself
    // this will be a pain to change :)
    private final RegistryObject<EntityType<MimicEntity>> toSpawn;

    public ModSpawnEgg(RegistryObject<EntityType<MimicEntity>> type, int color1, int color2, Properties props) {
        super(EntityType.PIG, color1, color2, props);
        this.toSpawn = type;
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag p_208076_1_) {
        return this.toSpawn.get();
    }
}
