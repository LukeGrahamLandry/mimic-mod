package io.github.lukegrahamlandry.mimic.items;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.events.TickHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class FakeChestItem extends Item {
    public FakeChestItem(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        MimicMain.LOGGER.debug("fake chest used");

        if (state.is(Blocks.CHEST) && context.getPlayer().isShiftKeyDown() && !context.getLevel().isClientSide()){
            TickHandler.spawns.add(new TickHandler.MimicSpawnData(context.getLevel(), context.getClickedPos()));
            if (!context.getPlayer().isCreative()) context.getPlayer().getItemInHand(context.getHand()).shrink(1);
            return ActionResultType.SUCCESS;
        }

        return super.useOn(context);
    }
}
