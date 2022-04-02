package ca.lukegrahamlandry.mimic;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ExampleMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
    }
}
