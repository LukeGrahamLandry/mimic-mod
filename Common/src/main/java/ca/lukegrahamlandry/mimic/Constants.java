package ca.lukegrahamlandry.mimic;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "mimic";
	public static final String MOD_NAME = "Multi Loader Template";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final ResourceLocation MIMIC_ENTITY_ID = new ResourceLocation(MOD_ID, "mimic");
	public static final ResourceLocation MIMIC_KEY_ID = new ResourceLocation(MOD_ID, "mimic_key");
	public static final ResourceLocation MIMIC_LOCK_ID = new ResourceLocation(MOD_ID, "mimic_lock");
	public static final ResourceLocation SINGLE_MIMIC_SPAWN_BLOCK_ID = new ResourceLocation(MOD_ID, "mimic_lock");

	public static Block getMimicSpawnBlock() {
		return Registry.BLOCK.get(SINGLE_MIMIC_SPAWN_BLOCK_ID);
	}
}