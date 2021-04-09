package io.github.lukegrahamlandry.mimic.items;

import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.RegistryObject;

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
    public EntityType<?> getType(@Nullable CompoundNBT p_208076_1_) {
        return this.toSpawn.get();
    }
}
