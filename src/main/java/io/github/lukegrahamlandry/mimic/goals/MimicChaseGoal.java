package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class MimicChaseGoal extends Goal {
    MimicEntity owner;
    double speed;
    double rangeSq;

    public MimicChaseGoal(MimicEntity mimicEntity, double speedIn, int range) {
        this.owner = mimicEntity;
        this.speed = speedIn;
        this.rangeSq = Math.pow(range, 2);
    }

    @Override
    public boolean canUse() {
        return this.owner.isAngry() && this.owner.hasTarget();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.owner.hasTarget() && this.owner.getRandom().nextInt(2) == 0){
            LivingEntity target = this.owner.getTarget();


            double dist = this.owner.distanceToSqr(target);
            if (dist > rangeSq){
                // forget about you if you get out of range
                // this.owner.setAngry(false);
                // this.owner.setTarget(null);
            } else if (dist < 1) {
                // when it gets close just watch
                this.owner.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            } else {
                this.owner.getNavigation().moveTo(target, this.speed);
            }
        }
    }
}
