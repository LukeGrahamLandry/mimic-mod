package ca.lukegrahamlandry.mimic;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MimicForgeConfig {
    private static final ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec server_config;

    public static ForgeConfigSpec.IntValue mimicSpawnChance;
    public static ForgeConfigSpec.BooleanValue debugMode;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> healthBarBlacklist;

    static {
        // TODO
//        mimicSpawnChance = server_builder
//                .comment("When a chest is generated in a supported structure it will have a 1/x chance of being a mimic. Higher value means fewer mimics. A value of 1 means always a mimic.")
//                .defineInRange("mimicSpawnChance", 5, 1, Integer.MAX_VALUE);
//        debugMode = server_builder
//                .comment("When true, the location of mimic spawns will be logged.")
//                .define("debugMode", false);

        healthBarBlacklist = server_builder
                .comment("S: registry names of entities that should not have a displayed ToroHealth bar (ex. [\"minecraft:wither\", \"minecraft:villager\"]).", "Mimics are hard coded and will included while stealthed regardless of the list", "Since this is in the server config, all clients will be effected. Nothing will happen if ToroHealth is not installed")
                .defineList("healthBarBlacklist", Collections.emptyList(), i -> ((String) i).split(":").length == 2);


        server_config = server_builder.build();
    }

    public static boolean shouldHaveNoHealthbar(Entity entity){
        if (entity == null) return false;
        EntityType type = entity.getType();
        if (type == null) return false;
        ResourceLocation registryName = type.getRegistryName();
        if (registryName == null) return false;
        String name = registryName.toString();
        for (String check : healthBarBlacklist.get()){
            if (name.equals(check)) return true;
        }
        return false;
    }

    public static void loadConfig(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config);
        String path = FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-server.toml").toString();
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        server_config.setConfig(file);
    }
}