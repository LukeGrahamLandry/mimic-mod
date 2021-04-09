package io.github.lukegrahamlandry.mimic;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("mimic")
public class MimicMain {
    public static final Logger LOGGER = LogManager.getLogger();

    public MimicMain() {
        GeckoLib.initialize();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
