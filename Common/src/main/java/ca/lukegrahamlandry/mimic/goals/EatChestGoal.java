package ca.lukegrahamlandry.mimic.goals;

import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EatChestGoal extends Goal {
    MimicEntity owner;
    BlockPos pos;

    public EatChestGoal(MimicEntity mimicEntity) {
        this.owner = mimicEntity;
    }

    @Override
    public boolean canUse() {
        return !this.owner.isAngry() && !this.owner.isTamed() && !this.owner.isStealth() && !this.owner.isLocked() &&
                this.owner.level.getBlockState((new BlockPos(this.owner.getBoundingBox().getCenter())).below()).getBlock() == Blocks.CHEST;
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner.getAttackTick() > 0;
    }


    // TODO doesnt get rotation when it starts on top of chest instead of using find goal
    @Override
    public void start() {
        this.pos = (new BlockPos(this.owner.getBoundingBox().getCenter())).below();
        BlockState state = this.owner.level.getBlockState(pos);
        this.owner.snapToBlock(pos.above(), state.getValue(ChestBlock.FACING));
        this.owner.startAttackAnim();
    }

    @Override
    public void tick() {
        // this.owner.getNavigation().moveTo((Path) null, 0);

        BlockState state = this.owner.level.getBlockState(pos);
        // take items from the chest and break it
        if (this.owner.getAttackTick() == 3 && state.is(Blocks.CHEST)){
            this.owner.consumeChest(pos);
        }
    }
}
