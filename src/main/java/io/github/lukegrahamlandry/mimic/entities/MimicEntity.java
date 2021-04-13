package io.github.lukegrahamlandry.mimic.entities;

import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.client.MimicContainer;
import io.github.lukegrahamlandry.mimic.goals.EatChestGoal;
import io.github.lukegrahamlandry.mimic.goals.MimicAttackGoal;
import io.github.lukegrahamlandry.mimic.goals.MimicChaseGoal;
import io.github.lukegrahamlandry.mimic.init.ContainerInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MimicEntity extends MobEntity implements IAnimatable, INamedContainerProvider, IInventory {

    private AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> IS_TAMED = EntityDataManager.defineId(MimicEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.defineId(MimicEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.defineId(MimicEntity.class, DataSerializers.INT);
    private static final DataParameter<BlockPos> CHEST_POS = EntityDataManager.defineId(MimicEntity.class, DataSerializers.BLOCK_POS);

    int playerLookTicks = 0;
    PlayerEntity playerLooking;

    private NonNullList<ItemStack> heldItems = NonNullList.withSize(27, ItemStack.EMPTY);

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

        this.goalSelector.addGoal(3, new EatChestGoal(this, 0.5, 3));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()){
            if (this.getAttackTick() > 0) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);

            if (this.playerLookTicks > 0){
                this.playerLookTicks--;
                if (this.playerLookTicks == 0){
                    this.playerLooking.closeContainer();
                    this.setAngry(true);
                }
            }
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
        this.getEntityData().define(IS_ANGRY, false);
        this.getEntityData().define(CHEST_POS, BlockPos.ZERO);
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        player.openMenu(this);
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
        for (int i=0;i<this.heldItems.size();i++){
            if (this.heldItems.get(i).getItem() != Items.AIR){
                this.spawnAtLocation(this.heldItems.get(i));
            }
        }
    }

    public void addItem(ItemStack stack) {
        for (int i=0;i<this.heldItems.size();i++){
            if (this.heldItems.get(i).getItem() == Items.AIR){
                this.heldItems.set(i, stack);
                return;
            }
        }

        MimicMain.LOGGER.debug(this.heldItems);

        ItemEntity item = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(item);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);

        CompoundNBT nbt = new CompoundNBT();
        for (int i=0;i<this.heldItems.size();i++){
            ItemStack stack = this.heldItems.get(i);
            CompoundNBT tag = stack.save(new CompoundNBT());
            nbt.put(String.valueOf(i), tag);
        }

        compoundNBT.put("mimichelditems", nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);

        CompoundNBT nbt = compoundNBT.getCompound("mimichelditems");
        int i = 0;
        while (nbt.contains(String.valueOf(i))) {
            CompoundNBT tag = nbt.getCompound(String.valueOf(i));
            ItemStack stack = ItemStack.of(tag);
            this.heldItems.set(i, stack);
            i++;
        }
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

    public void setChestPos(BlockPos pos) {
        this.getEntityData().set(CHEST_POS, pos);
    }

    public BlockPos getChestPos() {
        return this.getEntityData().get(CHEST_POS);
    }

    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController(this, "moveController", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory(){
        return this.factory;
    }

    public void startStealth() {
        MimicMain.LOGGER.debug("stealth");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        if (this.isTamed()){
            return new MimicContainer(ContainerInit.TAME_MIMIC.get(), id, playerInventory, this, 3);
        } else {
            this.playerLooking = player;
            this.playerLookTicks = 50;
            return new MimicContainer(ContainerInit.EVIL_MIMIC.get(), id, playerInventory, this, 3);
        }
    }

    // IInventory

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public boolean isEmpty() {
        return this.heldItems.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return this.heldItems.get(index);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        ItemStack itemstack = ItemStackHelper.removeItem(this.heldItems, p_70298_1_, p_70298_2_);
        if (!itemstack.isEmpty()) {
            this.setChanged();

            this.playerLookTicks = 2;
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ItemStackHelper.takeItem(this.heldItems, p_70304_1_);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.heldItems.set(index, stack);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        return this.isAlive();
    }

    @Override
    public void clearContent() {
        this.heldItems.clear();
    }
}
