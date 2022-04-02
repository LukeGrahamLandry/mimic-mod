package ca.lukegrahamlandry.mimic.init;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.MimicMain;
import ca.lukegrahamlandry.mimic.blocks.SingleMimicSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    public static final RegistryObject<Block> SINGLE_MIMIC_SPAWN = BLOCKS.register(Constants.SINGLE_MIMIC_SPAWN_BLOCK_ID.getPath(), () -> new SingleMimicSpawner(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)));

}
