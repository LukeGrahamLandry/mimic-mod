package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

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
        BlockState state = this.owner.level.getBlockState(pos);
        if (this.owner.getAttackTick() == 3 && state.is(Blocks.CHEST)){
            this.owner.consumeChest(pos);
        }
    }
}
