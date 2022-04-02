package ca.lukegrahamlandry.mimic.goals;

import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

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

    protected boolean isValidTarget(LevelReader p_179488_1_, BlockPos p_179488_2_) {
        if (!p_179488_1_.isEmptyBlock(p_179488_2_.above())) {
            return false;
        } else {
            BlockState blockstate = p_179488_1_.getBlockState(p_179488_2_);
            if (blockstate.is(Blocks.CHEST)) {
                this.target = p_179488_2_;
                // this.mimic.setChestPos(p_179488_2_);
                return ChestBlockEntity.getOpenCount(p_179488_1_, p_179488_2_) < 1;
            } else {
                return false;
            }
        }
    }
}
