package io.github.lukegrahamlandry.mimic.init;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.SingleMimicSpawner;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MimicMain.MOD_ID);

    public static final RegistryObject<Block> SINGLE_MIMIC_SPAWN = BLOCKS.register("single_mimic_spawn", () -> new SingleMimicSpawner(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)));

}
