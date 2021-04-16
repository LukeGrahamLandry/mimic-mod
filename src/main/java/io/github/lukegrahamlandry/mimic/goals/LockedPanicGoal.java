package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.function.ToDoubleFunction;

public class LockedPanicGoal extends Goal {
    protected final MimicEntity mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public LockedPanicGoal(MimicEntity p_i1645_1_, double p_i1645_2_) {
        this.mob = p_i1645_1_;
        this.speedModifier = p_i1645_2_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.mob.isLocked()) return false;

        if (!this.mob.isScared() && this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
            return false;
        } else {
            if (this.mob.isOnFire()) {
                BlockPos blockpos = this.lookForWater(this.mob.level, this.mob, 5, 4);
                if (blockpos != null) {
                    this.posX = (double)blockpos.getX();
                    this.posY = (double)blockpos.getY();
                    this.posZ = (double)blockpos.getZ();
                    return true;
                }
            }

            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosition() {
        Vector3d vector3d = RandomPositionGenerator.getPos(this.mob, 5, 4);
        if (vector3d == null) {
            return false;
        } else {
            this.posX = vector3d.x;
            this.posY = vector3d.y;
            this.posZ = vector3d.z;
            return true;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Nullable
    protected BlockPos lookForWater(IBlockReader p_188497_1_, Entity p_188497_2_, int p_188497_3_, int p_188497_4_) {
        BlockPos blockpos = p_188497_2_.blockPosition();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();
        float f = (float)(p_188497_3_ * p_188497_3_ * p_188497_4_ * 2);
        BlockPos blockpos1 = null;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int l = i - p_188497_3_; l <= i + p_188497_3_; ++l) {
            for(int i1 = j - p_188497_4_; i1 <= j + p_188497_4_; ++i1) {
                for(int j1 = k - p_188497_3_; j1 <= k + p_188497_3_; ++j1) {
                    blockpos$mutable.set(l, i1, j1);
                    if (p_188497_1_.getFluidState(blockpos$mutable).is(FluidTags.WATER)) {
                        float f1 = (float)((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));
                        if (f1 < f) {
                            f = f1;
                            blockpos1 = new BlockPos(blockpos$mutable);
                        }
                    }
                }
            }
        }

        return blockpos1;
    }
}