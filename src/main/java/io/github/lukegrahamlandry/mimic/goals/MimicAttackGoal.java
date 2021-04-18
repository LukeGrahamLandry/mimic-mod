package io.github.lukegrahamlandry.mimic.goals;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.entities.MimicEntity;
import net.minecraft.entity.ai.goal.Goal;

public class MimicAttackGoal extends Goal {
    MimicEntity owner;
    static double rangeSq = 4;
    public MimicAttackGoal(MimicEntity mimicEntity) {
        this.owner = mimicEntity;
    }

    @Override
    public boolean canUse() {
        if (!(this.owner.isAngry() && this.owner.hasTarget() && this.owner.getRandom().nextInt(3) == 0)) return false;
        return inRange();
    }

    private boolean inRange(){
        return this.owner.distanceToSqr(this.owner.getTarget()) <= rangeSq;
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner.isAngry() && this.owner.hasTarget() && this.owner.getAttackTick() > 0;
    }

    @Override
    public void start() {
        this.owner.startAttackAnim();
    }

    @Override
    public void tick() {
        if (this.owner.getAttackTick() == 4 && this.owner.hasTarget() && inRange()){
            this.owner.doHurtTarget(this.owner.getTarget());
            if (!this.owner.hasTarget()){
                this.owner.setAngry(false);
            }
        }
    }
}
