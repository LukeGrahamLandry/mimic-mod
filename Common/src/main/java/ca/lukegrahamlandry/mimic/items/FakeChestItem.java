package ca.lukegrahamlandry.mimic.items;

import ca.lukegrahamlandry.mimic.events.TickHandler;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FakeChestItem extends Item {
    public FakeChestItem(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (state.is(Blocks.CHEST) && context.getPlayer().isShiftKeyDown()){
            TickHandler.spawns.add(new TickHandler.MimicSpawnData(context.getLevel(), context.getClickedPos()));
            if (!context.getPlayer().isCreative()) context.getPlayer().getItemInHand(context.getHand()).shrink(1);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
