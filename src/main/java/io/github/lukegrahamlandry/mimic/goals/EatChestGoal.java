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

    @Override
    public void start() {
        this.pos = (new BlockPos(this.owner.getBoundingBox().getCenter())).below();
        BlockState state = this.owner.level.getBlockState(pos);
        int direction = state.getValue(ChestBlock.FACING).get2DDataValue();
        this.owner.setFacingDirection(direction);
        this.owner.startAttackAnim();

        MimicMain.LOGGER.debug("start eat chest");
    }

    @Override
    public void tick() {
        // take items from the chest and break it
        if (this.owner.getAttackTick() == 3 && this.owner.level.getBlockState(pos).getBlock() == Blocks.CHEST){
            ChestTileEntity chest = (ChestTileEntity) this.owner.level.getBlockEntity(pos);

            // take items
            for (int i=0;i<chest.getContainerSize();i++){
                this.owner.addItem(chest.getItem(i));
                chest.setItem(i, ItemStack.EMPTY);
            }
            this.owner.addItem(new ItemStack(Items.CHEST));

            int direction = this.owner.level.getBlockState(pos).getValue(ChestBlock.FACING).get2DDataValue();
            this.owner.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            this.owner.setPos(pos.getX()+0.5d, pos.getY(), pos.getZ()+0.5d);
            this.owner.setFacingDirection(direction);
            this.owner.setStealth(true);
        }
    }
}
