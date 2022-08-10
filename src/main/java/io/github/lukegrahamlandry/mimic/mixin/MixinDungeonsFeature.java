package io.github.lukegrahamlandry.mimic.mixin;

import io.github.lukegrahamlandry.mimic.MimicConfig;
import io.github.lukegrahamlandry.mimic.MimicMain;
import io.github.lukegrahamlandry.mimic.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.DungeonsFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(DungeonsFeature.class)
public class MixinDungeonsFeature {
    @Inject(at = @At("RETURN"), method = "place(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/NoFeatureConfig;)Z")
    private void place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, NoFeatureConfig p_241855_5_, CallbackInfoReturnable<Boolean> cir) {
        // MimicMain.LOGGER.debug("!! dungeon mixin !!");

        int i = 3;
        int j = p_241855_3_.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int i1 = -1;
        int j1 = 4;
        int k1 = p_241855_3_.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for(int k2 = k; k2 <= l; ++k2) {
            for(int l2 = -1; l2 <= 4; ++l2) {
                for(int i3 = l1; i3 <= i2; ++i3) {
                    BlockPos blockpos = p_241855_4_.offset(k2, l2, i3);
                    Material material = p_241855_1_.getBlockState(blockpos).getMaterial();
                    boolean flag = material.isSolid();
                    if (l2 == -1 && !flag) {
                        return;
                    }

                    if (l2 == 4 && !flag) {
                        return;
                    }

                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2) && l2 == 0 && p_241855_1_.isEmptyBlock(blockpos) && p_241855_1_.isEmptyBlock(blockpos.above())) {
                        ++j2;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5) {
            for(int k3 = k; k3 <= l; ++k3) {
                for(int i4 = 3; i4 >= -1; --i4) {
                    for(int k4 = l1; k4 <= i2; ++k4) {
                        BlockPos blockpos1 = p_241855_4_.offset(k3, i4, k4);
                        BlockState blockstate = p_241855_1_.getBlockState(blockpos1);
                        if (blockstate.is(Blocks.CHEST)){
                            p_241855_1_.setBlock(blockpos1.above(), BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
                            p_241855_1_.getBlockTicks().scheduleTick(blockpos1.above(), BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
                        }
                    }
                }
            }
        }

        for(int l3 = 0; l3 < 2; ++l3) {
            for(int j4 = 0; j4 < 3; ++j4) {
                int l4 = p_241855_4_.getX() + p_241855_3_.nextInt(j * 2 + 1) - j;
                int i5 = p_241855_4_.getY();
                int j5 = p_241855_4_.getZ() + p_241855_3_.nextInt(k1 * 2 + 1) - k1;
                BlockPos blockpos2 = new BlockPos(l4, i5, j5);
                if (p_241855_1_.isEmptyBlock(blockpos2)) {
                    int j3 = 0;

                    for(Direction direction : Direction.Plane.HORIZONTAL) {
                        if (p_241855_1_.getBlockState(blockpos2.relative(direction)).getMaterial().isSolid()) {
                            ++j3;
                        }
                    }

                    if (j3 == 1 && p_241855_1_.getBlockState(blockpos2).is(Blocks.CHEST)) {
                        p_241855_1_.setBlock(blockpos2.above(), BlockInit.SINGLE_MIMIC_SPAWN.get().defaultBlockState(), 3);
                        p_241855_1_.getBlockTicks().scheduleTick(blockpos2.above(), BlockInit.SINGLE_MIMIC_SPAWN.get(), 1);
                        break;
                    }
                }
            }
        }
    }
}
