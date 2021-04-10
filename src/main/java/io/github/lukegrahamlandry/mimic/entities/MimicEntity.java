package io.github.lukegrahamlandry.mimic.entities;

import io.github.lukegrahamlandry.mimic.goals.MimicAttackGoal;
import io.github.lukegrahamlandry.mimic.goals.MimicChaseGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MimicEntity extends MobEntity implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> IS_TAMED = EntityDataManager.defineId(MimicEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.defineId(MimicEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.defineId(MimicEntity.class, DataSerializers.INT);

    public MimicEntity(EntityType<? extends MimicEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createMobAttributes() {
        return AttributeModifierMap.builder().add(Attributes.MAX_HEALTH, 60).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.KNOCKBACK_RESISTANCE).add(Attributes.MOVEMENT_SPEED, 0.55).add(Attributes.ARMOR, 5).add(Attributes.ARMOR_TOUGHNESS).add(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).add(net.minecraftforge.common.ForgeMod.NAMETAG_DISTANCE.get()).add(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()).add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.ATTACK_KNOCKBACK);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.addGoal(2, new MimicChaseGoal(this, 0.5, 10));
        this.goalSelector.addGoal(2, new MimicAttackGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()){
            if (this.getAttackTick() > 0) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);
        }
    }

    // decides which animation to play. animationName is from the json file in resources/id/animations
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if (this.getAttackTick() > 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mimic.attack", false));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mimic.idle", true));
        return PlayState.CONTINUE;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ATTACK_TICK, 0);
        this.getEntityData().define(IS_TAMED, false);
        this.getEntityData().define(IS_ANGRY, true);
    }

    public boolean isTamed() {
        return this.getEntityData().get(IS_TAMED);
    }

    public boolean isAngry() {
        return this.getEntityData().get(IS_ANGRY);
    }

    public boolean hasTarget() {
        return this.getTarget() != null && this.getTarget().isAlive();
    }

    public int getAttackTick() {
        return this.getEntityData().get(ATTACK_TICK);
    }

    public void startAttackAnim() {
        this.getEntityData().set(ATTACK_TICK, 20);
    }

    public void setAngry(boolean flag) {
        this.getEntityData().set(IS_ANGRY, flag);
    }

    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController(this, "moveController", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory(){
        return this.factory;
    }

}
