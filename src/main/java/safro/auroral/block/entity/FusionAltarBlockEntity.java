package safro.auroral.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.auroral.api.block.SimpleSidedInventory;
import safro.auroral.config.AuroralConfig;
import safro.auroral.recipe.FusionRecipe;
import safro.auroral.registry.BlockRegistry;
import safro.auroral.registry.RecipeRegistry;

import java.util.Optional;

public class FusionAltarBlockEntity extends EnergyConnectableBlockEntity implements SimpleSidedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private int craftTicks = 0;

    public FusionAltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.FUSION_ALTAR_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FusionAltarBlockEntity be) {
        tickConnections(world, pos, be);

        if (be.craftTicks > 0) {
            if (be.craftTicks == 1) {
                FusionRecipe recipe = world.getRecipeManager().getFirstMatch(RecipeRegistry.FUSION, be, world).orElse(null);
                if (recipe != null) {
                    be.setStack(2, recipe.getOutput(null));
                    be.setStack(0, ItemStack.EMPTY);
                    be.setStack(1, ItemStack.EMPTY);
                    be.craftTicks = 0;

                    world.playSound(null, pos, SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 0.8F, 1.0F);
                    markDirty(world, pos, state);
                    be.update();
                }
            } else {
                be.craftTicks--;
                be.update();
            }
        } else if (be.inventory.get(2).isEmpty() && be.getEnergy() >= AuroralConfig.get().fusionEnergyPerCraft) {
            Optional<FusionRecipe> optional = world.getRecipeManager().getFirstMatch(RecipeRegistry.FUSION, be, world);
            if (optional.isPresent()) {
                be.craftTicks = AuroralConfig.get().fusionCraftTime;
                be.removeEnergy(AuroralConfig.get().fusionEnergyPerCraft);
                be.update();
            }
        }
    }

    @Override
    public long getMaxEnergy() {
        return 10000;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("CraftTicks", this.craftTicks);
        Inventories.writeNbt(nbt, this.inventory, false);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.craftTicks = nbt.getInt("CraftTicks");
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (nbt.contains("Items", 9)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    public boolean isCrafting() {
        return this.craftTicks > 0;
    }

    public int getCraftTicks() {
        return this.craftTicks;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.WEST || side == Direction.SOUTH) {
            return new int[]{0};
        } else if (side == Direction.EAST || side == Direction.NORTH) {
            return new int[]{1};
        } else if (side == Direction.DOWN) {
            return new int[]{2};
        }
        return new int[]{0, 1};
    }

    @Override
    public Direction[] getSides() {
        return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.inventory.get(slot).isEmpty();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 2;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }
}
