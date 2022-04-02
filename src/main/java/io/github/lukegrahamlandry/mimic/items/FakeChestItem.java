package io.github.lukegrahamlandry.mimic.items;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.events.TickHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

public class FakeChestItem extends Item {
    public FakeChestItem(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        MimicMain.LOGGER.debug("fake chest used");

        if (state.is(Blocks.CHEST) && context.getPlayer().isShiftKeyDown()){
            TickHandler.spawns.add(new TickHandler.MimicSpawnData(context.getLevel(), context.getClickedPos()));
            if (!context.getPlayer().isCreative()) context.getPlayer().getItemInHand(context.getHand()).shrink(1);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
