package ca.lukegrahamlandry.mimic;

import ca.lukegrahamlandry.mimic.entities.MimicEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ForgeGeoMimicEntity extends MimicEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public ForgeGeoMimicEntity(EntityType<? extends MimicEntity> type, Level world) {
        super(type, world);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        Pair<String, Boolean> anim = super.animationPredicate(event.getLimbSwingAmount());
        event.getController().setAnimation(new AnimationBuilder().addAnimation(anim.getFirst(), anim.getSecond()));
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController(this, "moveController", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
