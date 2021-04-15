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
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class EatChestGoal extends Goal {
    MimicEntity owner;
    double speed;
    int range;

    BlockPos target;

    public EatChestGoal(MimicEntity mimicEntity, double speedIn, int rangeIn) {
        this.owner = mimicEntity;
        this.speed = speedIn;
        this.range = rangeIn;
    }

    @Override
    public boolean canUse() {
        return !this.owner.isAngry() && !this.owner.isTamed();
    }

    @Override
    public void tick() {
        super.tick();

        // TODO: fix the positioning when it bites a chest & corner case where chest inst there anymore and it gets stuck in bite loop
        // and it seems like it can get stuck > 2 blocks away maybe need to get rid of +0.5

        if (this.owner.getAttackTick() > 0){
            // take items from the chest and break it
            BlockPos pos = this.owner.getChestPos();
            if (this.owner.getAttackTick() == 3 && this.owner.level.getBlockState(pos).getBlock() == Blocks.CHEST){
                ChestTileEntity chest = (ChestTileEntity) this.owner.level.getBlockEntity(pos);
                for (int i=0;i<chest.getContainerSize();i++){
                    this.owner.addItem(chest.getItem(i));
                    chest.setItem(i, ItemStack.EMPTY);
                }
                int direction = this.owner.level.getBlockState(pos).getValue(ChestBlock.FACING).get2DDataValue();
                this.owner.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                this.owner.addItem(new ItemStack(Items.CHEST));

                this.target = null;
                this.owner.setPos(pos.getX()+0.5d, pos.getY()+0.5d, pos.getZ()+0.5d);
                this.owner.setFacingDirection(direction);
                this.owner.setStealth(true);
            }

        } else if (target == null){
            BlockPos pos = this.owner.getChestPos();
            // MimicMain.LOGGER.debug("null " + pos.toString());

            // calculate the position to move to
            if (pos != BlockPos.ZERO && this.owner.level.getBlockState(pos).getBlock() == Blocks.CHEST){
                for (int i=0;i<4;i++){
                    Direction dir = Direction.from2DDataValue(i);
                    BlockPos checkPos = pos.relative(dir);
                    if (this.owner.level.getBlockState(checkPos).isAir()){
                        target = checkPos;
                    }
                }
            } else {  // find a chest
                int negRange = range * -1;
                for (int x=negRange;x<range;x++){
                    for (int y=negRange;y<range;y++){
                        for (int z=negRange;z<range;z++){
                            BlockPos checkPos = new BlockPos(this.owner.getX() + x, this.owner.getY() + y, this.owner.getZ() + z);
                            // memoize for preformance eventually
                            BlockState state = this.owner.level.getBlockState(checkPos);
                            if (state.getBlock() == Blocks.CHEST){
                                this.owner.setChestPos(checkPos);
                                MimicMain.LOGGER.debug("found " + checkPos.toString());
                                return;
                            }
                        }
                    }
                }
            }

        } else if (!owner.isAngry() && !owner.isTamed()){
            double dist = this.owner.distanceToSqr(new Vector3d(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D));
            MimicMain.LOGGER.debug(dist + " " + target.toString());

            if (dist > 4){ // move to the position
                this.owner.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), this.speed);
            } else { // start biting
                this.owner.getLookControl().setLookAt(this.owner.getChestPos().getX(), this.owner.getChestPos().getY(), this.owner.getChestPos().getZ());
                this.owner.startAttackAnim();
            }
        }

    }
}
