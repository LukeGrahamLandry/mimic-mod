package io.github.lukegrahamlandry.mimic;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class MimicConfig {
    private static final ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec server_config;

    public static ForgeConfigSpec.IntValue mimicSpawnChance;
    public static ForgeConfigSpec.BooleanValue debugMode;

    static {
        mimicSpawnChance = server_builder
                .comment("When a chest is generated in a supported structure it will have a 1/x chance of being a mimic. Higher value means fewer mimics. A value of 1 means always a mimic.")
                .defineInRange("mimicSpawnChance", 5, 1, Integer.MAX_VALUE);
        debugMode = server_builder
                .comment("When true, the location of mimic spawns will be logged.")
                .define("debugMode", false);

        server_config = server_builder.build();
    }

    public static void loadConfig(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config);
        String path = FMLPaths.CONFIGDIR.get().resolve(MimicMain.MOD_ID + "-server.toml").toString();
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        server_config.setConfig(file);
    }
}