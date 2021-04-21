package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class MimicWanderGoal extends RandomWalkingGoal {
    protected final float probability;
    private final MimicEntity mimic;

    public MimicWanderGoal(MimicEntity p_i47301_1_, double p_i47301_2_) {
        this(p_i47301_1_, p_i47301_2_, 0.001F);
    }

    public MimicWanderGoal(MimicEntity p_i47302_1_, double p_i47302_2_, float p_i47302_4_) {
        super(p_i47302_1_, p_i47302_2_);
        this.probability = p_i47302_4_;
        this.mimic = p_i47302_1_;
    }

    public boolean canUse() {
        if (this.mimic.isAngry() || this.mimic.isStealth() || this.mimic.isLocked() || this.mimic.isOpen()) return false;
        if (this.mob.getRandom().nextInt( this.mimic.isTamed() ? 100 : 40) != 0) return false;
        if (!this.mob.getNavigation().isDone()) return false;

        Vector3d vector3d = this.getPosition();
        if (vector3d == null) {
            return false;
        } else {
            this.wantedX = vector3d.x;
            this.wantedY = vector3d.y;
            this.wantedZ = vector3d.z;
            this.forceTrigger = false;
            return true;
        }
    }

    @Nullable
    protected Vector3d getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vector3d vector3d = RandomPositionGenerator.getLandPos(this.mob, this.mimic.isTamed() ? 15 : 5, 7);
            return vector3d == null ? super.getPosition() : vector3d;
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.mob,  this.mimic.isTamed() ? 10 : 5, 7) : super.getPosition();
        }
    }
}