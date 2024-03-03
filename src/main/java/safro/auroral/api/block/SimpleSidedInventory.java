package safro.auroral.api.block;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface SimpleSidedInventory extends SidedInventory {
    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : this.getItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.getItems().size() ? this.getItems().get(slot) : ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.getItems(), slot, amount);
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.getItems().size()) {
            this.getItems().set(slot, stack);
        }
    }

    @Override
    default void clear() {
        this.getItems().clear();
    }
}
