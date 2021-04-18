package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.pathfinding.Path;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;


public class FindChestGoal extends MoveToBlockGoal {
    private final MimicEntity mimic;
    private BlockPos target;

    public FindChestGoal(MimicEntity p_i50330_1_, double speed) {
        super(p_i50330_1_, speed, 8, 2);  // search radius: horizontal, vertical
        this.mimic = p_i50330_1_;
    }

    public boolean canUse() {
        return !this.mimic.isAngry() && !this.mimic.isTamed() && !this.mimic.isLocked() && !this.mimic.isStealth() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.isReachedTarget();
    }

    @Override
    public void stop() {
        super.stop();

        BlockState state = this.mimic.level.getBlockState(this.target);
        if (state.getBlock() == Blocks.CHEST){
            this.mimic.snapToBlock(target.above(), state.getValue(ChestBlock.FACING));
        }

        this.mimic.getNavigation().moveTo((Path) null, 0);
        // this.mimic.setDeltaMovement(0,0,0);
    }

    protected boolean isValidTarget(IWorldReader p_179488_1_, BlockPos p_179488_2_) {
        if (!p_179488_1_.isEmptyBlock(p_179488_2_.above())) {
            return false;
        } else {
            BlockState blockstate = p_179488_1_.getBlockState(p_179488_2_);
            if (blockstate.is(Blocks.CHEST)) {
                this.target = p_179488_2_;
                // this.mimic.setChestPos(p_179488_2_);
                return ChestTileEntity.getOpenCount(p_179488_1_, p_179488_2_) < 1;
            } else {
                return false;
            }
        }
    }
}
