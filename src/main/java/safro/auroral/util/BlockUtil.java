package safro.auroral.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class BlockUtil {

    public static void removeStack(Inventory inventory, PlayerEntity player, int slot) {
        ItemStack stack = inventory.removeStack(slot);
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }
}
