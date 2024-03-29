package ca.lukegrahamlandry.mimic.entities;

import ca.lukegrahamlandry.mimic.Constants;
import ca.lukegrahamlandry.mimic.client.MimicContainer;
import ca.lukegrahamlandry.mimic.goals.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nullable;
import java.util.UUID;

public class MimicEntity extends PathfinderMob implements MenuProvider, Container {
    private static final EntityDataAccessor<Boolean> IS_TAMED = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_STEALTH = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LOCKED = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> UP_DOWN_TICK = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_OPEN = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> OPEN_CLOSE_TICK = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FACING_DIRECTION = SynchedEntityData.defineId(MimicEntity.class, EntityDataSerializers.INT);

    private UUID owner;

    int playerLookTicks = 0;
    Player playerLooking;
    int scaredTicks = 0;
    boolean hasAlreadyGeneratedLoot = false;

    private NonNullList<ItemStack> heldItems = NonNullList.withSize(27, ItemStack.EMPTY);

    private static final int boredOfWanderingChance = 2 * 60 * 20;

    public MimicEntity(EntityType<? extends MimicEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60).add(Attributes.ATTACK_DAMAGE, 14)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.MOVEMENT_SPEED, 0.55).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new MimicChaseGoal(this, 0.6D, 10));
        this.goalSelector.addGoal(2, new MimicAttackGoal(this));

        this.goalSelector.addGoal(3, new EatChestGoal(this));
        this.goalSelector.addGoal(3, new FindChestGoal(this, 0.5D));

        this.goalSelector.addGoal(2, new LockedPanicGoal(this, 0.6));
        this.goalSelector.addGoal(6, new TamedFollowGoal(this, 0.75D, 8.0F, 2.0F, false));

        this.goalSelector.addGoal(7, new MimicWanderGoal(this, 0.5D));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()){
            if (this.getAttackTick() > 0) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);
            if (this.getEntityData().get(UP_DOWN_TICK) > 0) this.getEntityData().set(UP_DOWN_TICK, this.getEntityData().get(UP_DOWN_TICK) - 1);
            if (this.getEntityData().get(OPEN_CLOSE_TICK) > 0) this.getEntityData().set(OPEN_CLOSE_TICK, this.getEntityData().get(OPEN_CLOSE_TICK) - 1);
            if (scaredTicks > 0) scaredTicks--;

            if (this.playerLookTicks > 0){
                this.playerLookTicks--;
                if (this.playerLookTicks == 0 && !isLocked() && !isTamed()){
                    ((ServerPlayer)this.playerLooking).closeContainer();
                    this.setAngry(true);
                    float strength = 1;
                    this.playerLooking.knockback(strength * 0.5F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                }
            }

            if (!this.isAngry() && !this.isStealth() && !this.isTamed() && getRandom().nextInt(boredOfWanderingChance) == 0){
                this.snapToBlock(this.blockPosition(), Direction.from2DDataValue(Math.floorDiv((int) this.yBodyRot, 90)));
                this.setStealth(true);
            }

            // stealth if a player is near
            if (!this.isAngry() && !this.isStealth() && !this.isTamed() && !this.isLocked() && getRandom().nextInt(5) == 0){
                AABB box = this.getBoundingBox().inflate(10);  // range in blocks
                for(Player playerentity : level.players()) {
                    if (!playerentity.isCreative() && box.contains(playerentity.getX(), playerentity.getY(), playerentity.getZ())) {
                        this.snapToBlock(this.blockPosition(), null);
                        this.setStealth(true);
                    }
                }
            }

            // dont get stuck in blocks
            if (this.isInWall() && this.isStealth()){
                this.setPos(this.getX(), this.getY() + 1, this.getZ());
            }
        }
    }

    public void snapToBlock(BlockPos pos, @Nullable Direction dir){
        if (dir == null) dir = Direction.from2DDataValue(getRandom().nextInt(4));
        this.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, dir.get2DDataValue() * 90, 0);
        this.setYBodyRot(dir.get2DDataValue() * 90);
        this.getNavigation().moveTo((Path) null, 0);
    }

    // decides which animation to play. animationName is from the json file in resources/id/animations
    public Pair<String, Boolean> animationPredicate(float limbSwingAmount){
        if (this.getAttackTick() > 0){
            return Pair.of("animation.mimic.attack", false);
        }

        if (isOpen()){
            if (isLocked()){
                return Pair.of("animation.locked.mimic.chest.open", false);
            } else {
                return Pair.of("animation.mimic.chest.open", false);
            }

        } else if (this.getEntityData().get(OPEN_CLOSE_TICK) > 0){
            if (isLocked()){
                return Pair.of("animation.locked.mimic.chest.close", false);

            } else {
                return Pair.of("animation.mimic.chest.close", false);
            }
        }


        if (this.isLocked() && this.getEntityData().get(IS_STEALTH)){
            if (this.getEntityData().get(UP_DOWN_TICK) > 0){
                return Pair.of("animation.mimic.lock", false);
            }

            return Pair.of("animation.mimic.lockidle", true);
        } else if (this.isLocked() && this.getEntityData().get(UP_DOWN_TICK) > 0){
            return Pair.of("animation.locked.mimic.getup", false);
        }

        if (this.getEntityData().get(IS_STEALTH)){  // dont replace condition with isStealth(), it will break, im being too clever
            if (this.getEntityData().get(UP_DOWN_TICK) > 0){
                return Pair.of("animation.mimic.sit", false);
            }

            return Pair.of("animation.mimic.idle.chest", true);

        } else if (this.getEntityData().get(UP_DOWN_TICK) > 0){
            return Pair.of("animation.mimic.getup", false);
        }

        if (!(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F)) {
            if (this.isLocked()){
                return Pair.of("animation.locked.mimic.run", true);
            } else if (this.isAngry()){
                return Pair.of("animation.mimic.run", true);
            } else {
                return Pair.of("animation.mimic.walkcycle", true);
            }
        }

        if (this.isLocked()) {
            return Pair.of("animation.locked.mimic.idle", true);
        } else {
            return Pair.of("animation.mimic.idle", true);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ATTACK_TICK, 0);
        this.getEntityData().define(UP_DOWN_TICK, 0);
        this.getEntityData().define(IS_TAMED, false);
        this.getEntityData().define(IS_LOCKED, false);
        this.getEntityData().define(IS_ANGRY, false);
        this.getEntityData().define(IS_STEALTH, false);
        this.getEntityData().define(IS_OPEN, false);
        this.getEntityData().define(OPEN_CLOSE_TICK, 0);
        this.getEntityData().define(FACING_DIRECTION, -1);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;

        // sit / stand
        if (isTamed() && player.isShiftKeyDown() && !this.level.isClientSide()){
            boolean targetState = !this.isStealth();
            this.setStealth(targetState);

            if (targetState){
                this.snapToBlock(this.blockPosition(), Direction.from2DDataValue(Math.floorDiv((int) this.yBodyRot, 90)));
            }

            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);

        // lock
        if (canItemLock(stack) && !this.isTamed() && !this.isLocked()){
            this.setLocked(true);
            this.snapToBlock(this.blockPosition(), Direction.from2DDataValue(Math.floorDiv((int) this.yBodyRot, 90)));
            if (!player.isCreative()) stack.shrink(1);
            this.getNavigation().moveTo((Path) null, 0);
            return InteractionResult.CONSUME;
        }

        // tame
        if (canItemTame(stack) && !this.isTamed() && !this.isLocked()){
            if (!level.isClientSide()){
                this.setTamed(true);
                this.setStealth(true);
                if (!player.isCreative()) stack.shrink(1);
                this.owner = player.getUUID();
                this.getNavigation().moveTo((Path) null, 0);
                // still doesnt work??
                for(int i = 0; i < 7; ++i) {
                    ((ServerLevel)level).sendParticles(ParticleTypes.HEART, getBoundingBox().getCenter().x + (random.nextDouble() * 3 - 1.5D), getY() + 1, getBoundingBox().getCenter().z + (random.nextDouble() * 3 - 1.5D), 1, 0.0D, 0.0D, 0.0D, 1.0D);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // open
        if (this.isStealth() && !player.isShiftKeyDown()){
            if (this.isEmpty()) this.generateDefaultLoot();
            player.openMenu(this);
            return InteractionResult.SUCCESS;
        }

        if (!this.isStealth() && !player.isShiftKeyDown() && this.isTamed()){
            player.displayClientMessage(Component.translatable("mimic.no_open_standing"), true);
        }

        return InteractionResult.PASS;
    }

    protected boolean canItemTame(ItemStack stack) {
        return stack.getItem() == Registry.ITEM.get(Constants.MIMIC_KEY_ID);
    }

    protected boolean canItemLock(ItemStack stack) {
        return stack.getItem() == Registry.ITEM.get(Constants.MIMIC_LOCK_ID);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
        if (this.isEmpty()) this.generateDefaultLoot();
        for (int i=0;i<this.heldItems.size();i++){
            if (this.heldItems.get(i).getItem() != Items.AIR){
                this.spawnAtLocation(this.heldItems.get(i));
            }
        }
    }

    public void addItem(ItemStack stack) {
        if (stack.isEmpty()) return;
        this.hasAlreadyGeneratedLoot = true;

        for (int i=0;i<this.heldItems.size();i++){
            if (this.heldItems.get(i).getItem() == Items.AIR){
                this.heldItems.set(i, stack);
                this.setPersistenceRequired();
                return;
            }
        }

        ItemEntity item = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(item);
    }

    private void generateDefaultLoot() {
        if (this.level.isClientSide() || this.hasAlreadyGeneratedLoot) return;
        ResourceLocation lootLocation = new ResourceLocation(Constants.MOD_ID, "default_mimic_loot");
        LootTable loottable = this.level.getServer().getLootTables().get(lootLocation);
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, this.position());
        loottable.fill(this, lootcontext$builder.create(LootContextParamSets.CHEST));

        this.hasAlreadyGeneratedLoot = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);

        CompoundTag nbt = new CompoundTag();
        for (int i=0;i<this.heldItems.size();i++){
            ItemStack stack = this.heldItems.get(i);
            CompoundTag tag = stack.save(new CompoundTag());
            nbt.put(String.valueOf(i), tag);
        }

        compoundNBT.put("mimichelditems", nbt);

        compoundNBT.putBoolean("tame", isTamed());
        compoundNBT.putBoolean("lock", isLocked());
        compoundNBT.putBoolean("angry", isAngry());
        compoundNBT.putBoolean("stealth", isStealth());
        compoundNBT.putBoolean("genloot", this.hasAlreadyGeneratedLoot);

        if (this.isTamed()){
            compoundNBT.putString("owner", this.owner.toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);

        CompoundTag nbt = compoundNBT.getCompound("mimichelditems");
        int i = 0;
        while (nbt.contains(String.valueOf(i))) {
            CompoundTag tag = nbt.getCompound(String.valueOf(i));
            ItemStack stack = ItemStack.of(tag);
            this.heldItems.set(i, stack);
            i++;
        }

        setTamed(compoundNBT.getBoolean("tame"));
        setLocked(compoundNBT.getBoolean("lock"));
        setStealth(compoundNBT.getBoolean("stealth"));
        setAngry(compoundNBT.getBoolean("angry"));
        this.hasAlreadyGeneratedLoot = compoundNBT.getBoolean("genloot");

        if (this.isTamed()){
            this.owner = UUID.fromString(compoundNBT.getString("owner"));
        }

    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (source.getDirectEntity() != null && source.getDirectEntity() instanceof LivingEntity && ((LivingEntity)source.getDirectEntity()).getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AxeItem){
            amount *= 2;
        }

        if (!level.isClientSide() && !this.isTamed() && source.getEntity() != null && source.getEntity() instanceof Player && !((Player)source.getEntity()).isCreative()){
            this.setAngry(true);
        }

        return super.hurt(source, amount);
    }

    public LivingEntity getOwner(){
        if (!this.isTamed() || this.level.isClientSide()) return null;
        return (LivingEntity) ((ServerLevel)this.level).getEntity(this.owner);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void push(Entity p_70108_1_) {
        // super.push(p_70108_1_);
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
        // super.push(p_70024_1_, p_70024_3_, p_70024_5_);
    }

    public boolean isScared(){
        return scaredTicks > 0;
    }

    public boolean isOpen() {
        return this.getEntityData().get(IS_OPEN);
    }

    public boolean isTamed() {
        return this.getEntityData().get(IS_TAMED);
    }

    public boolean isAngry() {
        boolean flag = this.getEntityData().get(IS_ANGRY);
        if (flag && isStealth()) return false; // it is playing the stand animation and will be ready to be angry soon
        return flag;
    }

    public boolean isStealth() {
        boolean flag = this.getEntityData().get(IS_STEALTH);

        if (!flag && this.getEntityData().get(UP_DOWN_TICK) > 0){ // currently standing up
            return true;
        }

        return flag;
    }

    public boolean isLocked() {
        return this.getEntityData().get(IS_LOCKED);
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
        if (flag){
            setStealth(false);
            if (isTamed() || isLocked()) return;
        }
        this.getEntityData().set(IS_ANGRY, flag);
    }

    public void setStealth(boolean flag) {
        if (!isStealth() && flag){
            this.getEntityData().set(UP_DOWN_TICK, 20);
        } else if (isStealth() && !flag){
            this.getEntityData().set(UP_DOWN_TICK, 2); // 22 but animation doesnt play so just dont bother with the delay until i fix it
        }
        this.getEntityData().set(IS_STEALTH, flag);
    }

    public void setLocked(boolean flag) {
        if (flag){
            setAngry(false);
            setStealth(true);
        }
        if (!isLocked() && flag){
            this.getEntityData().set(UP_DOWN_TICK, 28);
        }

        this.getEntityData().set(IS_LOCKED, flag);
    }

    public void setTamed(boolean flag) {
        if (flag){
            setAngry(false);
        }
        this.getEntityData().set(IS_TAMED, flag);
    }

    /* public void setChestPos(BlockPos pos) {
        this.getEntityData().set(CHEST_POS, pos);
    }

    public BlockPos getChestPos() {
        return this.getEntityData().get(CHEST_POS);
    } */

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        if (!this.isTamed()) {
            this.playerLooking = player;
            this.playerLookTicks = 15;
        }
        MenuType<MimicContainer> menuType = (MenuType<MimicContainer>) Registry.MENU.get(this.isTamed() ? Constants.TAME_MIMIC_CONTAINER : Constants.EVIL_MIMIC_CONTAINER);
        return new MimicContainer(menuType, id, playerInventory, this, 3);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOOD_STEP, 0.15F, 1.0F);
    }

    // IInventory

    @Override
    public void stopOpen(Player player) {
        this.getEntityData().set(OPEN_CLOSE_TICK, 10);
        this.getEntityData().set(IS_OPEN, false);
        this.level.playSound((Player)null, getX(), getY(), getZ(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(Player player) {
        this.getEntityData().set(OPEN_CLOSE_TICK, 13);
        this.getEntityData().set(IS_OPEN, true);
        this.level.playSound((Player)null, getX(), getY(), getZ(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.1F + 0.9F);
    }

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

    private void onTake(){
        this.playerLookTicks = 2;
        if (this.isLocked() && getRandom().nextInt(3) == 0){
            if (this.playerLooking instanceof ServerPlayer){
                ((ServerPlayer)this.playerLooking).closeContainer();
            } else if (this.playerLooking instanceof LocalPlayer){
                ((LocalPlayer)this.playerLooking).closeContainer();
            }

            this.scaredTicks = 400;
            this.setStealth(false);
        }
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        ItemStack itemstack = ContainerHelper.removeItem(this.heldItems, p_70298_1_, p_70298_2_);
        if (!itemstack.isEmpty()) {
            this.setChanged();
            onTake();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        ItemStack itemstack = ContainerHelper.takeItem(this.heldItems, p_70304_1_);
        if (!itemstack.isEmpty()) onTake();

        return itemstack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.heldItems.set(index, stack);
        if (stack.isEmpty()) onTake();
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player p_70300_1_) {
        return this.isAlive();
    }

    @Override
    public void clearContent() {
        this.heldItems.clear();
    }


    public void consumeChest(BlockPos pos) {
        BlockEntity tile = this.level.getBlockEntity(pos);
        if (!(tile instanceof BaseContainerBlockEntity)) {
            System.out.println("mimic failed to eat chest at " + pos);
            return;
        }

        BaseContainerBlockEntity chest = (BaseContainerBlockEntity) tile;

        // take items
        for (int i=0;i<chest.getContainerSize();i++){
            this.addItem(chest.getItem(i));
            chest.setItem(i, ItemStack.EMPTY);
        }
        // this.owner.addItem(new ItemStack(Items.CHEST));

        Direction chestFacing = this.level.getBlockState(pos).getOptionalValue(ChestBlock.FACING).orElse(Direction.NORTH);
        if (chestFacing.getAxis() == Direction.Axis.Y) chestFacing = Direction.NORTH;
        this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        this.snapToBlock(pos, chestFacing);
        this.setStealth(true);
    }
}
