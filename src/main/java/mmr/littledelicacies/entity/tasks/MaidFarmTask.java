package mmr.littledelicacies.entity.tasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import mmr.littledelicacies.entity.LittleMaidBaseEntity;
import mmr.littledelicacies.init.MaidJob;
import mmr.littledelicacies.network.MaidPacketHandler;
import net.minecraft.block.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class MaidFarmTask extends Task<LittleMaidBaseEntity> {
    @Nullable
    private BlockPos field_220422_a;
    private boolean field_220423_b;
    private boolean field_220424_c;
    private long field_220425_d;
    private int field_220426_e;
    private final List<BlockPos> field_223518_f = Lists.newArrayList();

    public MaidFarmTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_PRESENT));
    }

    protected boolean shouldExecute(ServerWorld worldIn, LittleMaidBaseEntity owner) {
        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, owner)) {
            return false;
        } else if (owner.getMaidData().getJob() != MaidJob.FARMER) {
            return false;
        } else {
            this.field_220423_b = owner.isFarmItemInInventory();
            this.field_220424_c = false;
            Inventory inventory = owner.getInventoryMaidMain();
            int i = inventory.getSizeInventory();

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventory.getStackInSlot(j);
                if (itemstack.isEmpty()) {
                    this.field_220424_c = true;
                    break;
                }

                if (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.BEETROOT_SEEDS) {
                    this.field_220424_c = true;
                    break;
                }
            }

            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(owner.getPosX(), owner.getPosY(), owner.getPosZ());
            this.field_223518_f.clear();

            for (int i1 = -1; i1 <= 1; ++i1) {
                for (int k = -1; k <= 1; ++k) {
                    for (int l = -1; l <= 1; ++l) {
                        blockpos$mutableblockpos.setPos(owner.getPosX() + (double) i1, owner.getPosY() + (double) k, owner.getPosZ() + (double) l);
                        if (this.func_223516_a(blockpos$mutableblockpos, worldIn)) {
                            this.field_223518_f.add(new BlockPos(blockpos$mutableblockpos));
                        }
                    }
                }
            }

            this.field_220422_a = this.func_223517_a(worldIn);
            return (this.field_220423_b || this.field_220424_c) && this.field_220422_a != null;
        }
    }

    @Nullable
    private BlockPos func_223517_a(ServerWorld p_223517_1_) {
        return this.field_223518_f.isEmpty() ? null : this.field_223518_f.get(p_223517_1_.getRandom().nextInt(this.field_223518_f.size()));
    }

    private boolean func_223516_a(BlockPos p_223516_1_, ServerWorld p_223516_2_) {
        BlockState blockstate = p_223516_2_.getBlockState(p_223516_1_);
        Block block = blockstate.getBlock();
        Block block1 = p_223516_2_.getBlockState(p_223516_1_.down()).getBlock();
        return block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate) && this.field_220424_c || blockstate.isAir() && block1 instanceof FarmlandBlock && this.field_220423_b;
    }

    protected void startExecuting(ServerWorld worldIn, LittleMaidBaseEntity entityIn, long gameTimeIn) {
        if (gameTimeIn > this.field_220425_d && this.field_220422_a != null) {
            entityIn.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(this.field_220422_a));
            entityIn.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(this.field_220422_a), 0.5F, 1));
        }

    }

    protected void resetTask(ServerWorld worldIn, LittleMaidBaseEntity entityIn, long gameTimeIn) {
        entityIn.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
        entityIn.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        this.field_220426_e = 0;
        this.field_220425_d = gameTimeIn + 40L;
    }

    protected void updateTask(ServerWorld worldIn, LittleMaidBaseEntity owner, long gameTime) {
        if (this.field_220422_a != null && gameTime > this.field_220425_d) {
            BlockState blockstate = worldIn.getBlockState(this.field_220422_a);
            Block block = blockstate.getBlock();
            Block block1 = worldIn.getBlockState(this.field_220422_a.down()).getBlock();
            if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate) && this.field_220424_c) {
                worldIn.destroyBlock(this.field_220422_a, true);
                owner.swingArm(Hand.MAIN_HAND);
                owner.giveExperiencePoints(1 + owner.getRNG().nextInt(1));
            }

            if (blockstate.isAir() && block1 instanceof FarmlandBlock && this.field_220423_b) {
                Inventory inventory = owner.getInventoryMaidMain();

                for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack = inventory.getStackInSlot(i);
                    boolean flag = false;
                    if (!itemstack.isEmpty()) {
                        if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                            worldIn.setBlockState(this.field_220422_a, Blocks.WHEAT.getDefaultState(), 3);
                            flag = true;
                            MaidPacketHandler.animationModel(owner, LittleMaidBaseEntity.FARM_ANIMATION);
                            owner.giveExperiencePoints(1);
                        } else if (itemstack.getItem() == Items.POTATO) {
                            worldIn.setBlockState(this.field_220422_a, Blocks.POTATOES.getDefaultState(), 3);
                            flag = true;
                            MaidPacketHandler.animationModel(owner, LittleMaidBaseEntity.FARM_ANIMATION);
                            owner.giveExperiencePoints(1);
                        } else if (itemstack.getItem() == Items.CARROT) {
                            worldIn.setBlockState(this.field_220422_a, Blocks.CARROTS.getDefaultState(), 3);
                            flag = true;
                            MaidPacketHandler.animationModel(owner, LittleMaidBaseEntity.FARM_ANIMATION);
                            owner.giveExperiencePoints(1);
                        } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                            worldIn.setBlockState(this.field_220422_a, Blocks.BEETROOTS.getDefaultState(), 3);
                            flag = true;
                            MaidPacketHandler.animationModel(owner, LittleMaidBaseEntity.FARM_ANIMATION);
                            owner.giveExperiencePoints(1);
                        } else if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
                            if (((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlantType(worldIn, field_220422_a) == net.minecraftforge.common.PlantType.Crop) {
                                worldIn.setBlockState(field_220422_a, ((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlant(worldIn, field_220422_a), 3);
                                flag = true;
                            }
                            MaidPacketHandler.animationModel(owner, LittleMaidBaseEntity.FARM_ANIMATION);
                            owner.giveExperiencePoints(1);
                        }
                    }

                    if (flag) {
                        worldIn.playSound((PlayerEntity) null, (double) this.field_220422_a.getX(), (double) this.field_220422_a.getY(), (double) this.field_220422_a.getZ(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                        break;
                    }
                }
            }

            if (block instanceof CropsBlock && !((CropsBlock) block).isMaxAge(blockstate)) {
                this.field_223518_f.remove(this.field_220422_a);
                this.field_220422_a = this.func_223517_a(worldIn);
                if (this.field_220422_a != null) {
                    this.field_220425_d = gameTime + 20L;
                    owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(this.field_220422_a), 0.5F, 1));
                    owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(this.field_220422_a));
                }
            }
        }

        ++this.field_220426_e;
    }

    protected boolean shouldContinueExecuting(ServerWorld worldIn, LittleMaidBaseEntity entityIn, long gameTimeIn) {
        return this.field_220426_e < 800;
    }
}