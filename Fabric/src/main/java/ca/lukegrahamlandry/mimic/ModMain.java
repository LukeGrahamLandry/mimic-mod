package ca.lukegrahamlandry.mimic;

import ca.lukegrahamlandry.mimic.blocks.SingleMimicSpawner;
import ca.lukegrahamlandry.mimic.client.MimicContainer;
import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import ca.lukegrahamlandry.mimic.events.TickHandler;
import ca.lukegrahamlandry.mimic.items.FakeChestItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import software.bernie.geckolib3.GeckoLib;

public class ModMain implements ModInitializer {
    public static EntityType<FabricGeoMimicEntity> mimicEntityType;
    public static MenuType<MimicContainer> evilMimicMenuType;
    public static MenuType<MimicContainer> tameMimicMenuType;

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        initRegistryObjects();

        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            TickHandler.onServerTick();
        });
    }

    private void initRegistryObjects() {
        mimicEntityType = FabricEntityType.Builder.of(FabricGeoMimicEntity::new, MobCategory.MISC).sized(1f, 1f).build(Constants.MIMIC_ENTITY_ID.toString());
        Registry.register(Registry.ENTITY_TYPE, Constants.MIMIC_ENTITY_ID, mimicEntityType);
        FabricDefaultAttributeRegistry.register(mimicEntityType, MimicEntity.createMobAttributes());

        Registry.register(Registry.ITEM, Constants.MIMIC_KEY_ID, new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        Registry.register(Registry.ITEM, Constants.MIMIC_LOCK_ID, new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        Registry.register(Registry.ITEM, new ResourceLocation(Constants.MOD_ID, "fake_chest"), new FakeChestItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        Registry.register(Registry.ITEM, new ResourceLocation(Constants.MOD_ID, "mimic_spawn_egg"), new SpawnEggItem(mimicEntityType, 0xad7110, 0x0, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

        Registry.register(Registry.BLOCK, Constants.SINGLE_MIMIC_SPAWN_BLOCK_ID, new SingleMimicSpawner(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)));

        evilMimicMenuType = new MenuType<MimicContainer>((a, b) -> MimicContainer.create(Constants.EVIL_MIMIC_CONTAINER, a, b));
        tameMimicMenuType = new MenuType<MimicContainer>((a, b) -> MimicContainer.create(Constants.TAME_MIMIC_CONTAINER, a, b));
        Registry.register(Registry.MENU, Constants.EVIL_MIMIC_CONTAINER, evilMimicMenuType);
        Registry.register(Registry.MENU, Constants.TAME_MIMIC_CONTAINER, tameMimicMenuType);
    }
}
