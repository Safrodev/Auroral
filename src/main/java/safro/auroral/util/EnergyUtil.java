package safro.auroral.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.auroral.api.LifeEnergyComponent;
import safro.auroral.api.block.EnergyStorage;
import safro.auroral.block.entity.EnergyConnectableBlockEntity;
import safro.auroral.registry.ComponentsRegistry;

public class EnergyUtil {
    public static final long ENERGY_PER_TICK = 64L;

    public static LifeEnergyComponent getFor(LivingEntity entity) {
        return ComponentsRegistry.LIFE_ENERGY_COMPONENT.get(entity);
    }

    public static void transfer(EnergyStorage from, LivingEntity to, long amount) {
        LifeEnergyComponent component = getFor(to);
        long canExtract = extract(from, amount, true);
        long inserted = component.insert(canExtract);
        ComponentsRegistry.LIFE_ENERGY_COMPONENT.sync(to);
        extract(from, inserted, false);
    }

    public static void transfer(EnergyStorage from, EnergyStorage to, long amount) {
        long canExtract = extract(from, amount, true);
        long inserted = insert(to, canExtract);
        extract(from, inserted, false);
    }

    public static ActionResult storageInteract(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof EnergyConnectableBlockEntity blockEntity) {
            if (player.isSneaking()) {
                Text text = Text.literal(blockEntity.getEnergy() + "/" + blockEntity.getMaxEnergy() + " Life Energy").formatted(Formatting.GREEN);
                player.sendMessage(text, true);
                return ActionResult.SUCCESS;
            } else if (blockEntity.getEnergy() > 0 && getFor(player).getEnergy() < getFor(player).getMaxEnergy()) {
                transfer(blockEntity, player, Long.MAX_VALUE);
                Text text = Text.translatable("text.auroral.filled").formatted(Formatting.GREEN);
                player.sendMessage(text, true);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    public static long extract(EnergyStorage storage, long amount, boolean simulate) {
        long extracted = Math.min(storage.getEnergy(), amount);
        if (!simulate) {
            storage.setEnergy(storage.getEnergy() - amount);
        }
        return extracted;
    }

    public static long insert(EnergyStorage storage, long amount) {
        long inserted = 0;
        if (storage.getEnergy() < storage.getMaxEnergy()) {
            if (storage.getEnergy() + amount > storage.getMaxEnergy()) {
                inserted = storage.getMaxEnergy() - storage.getEnergy();
                storage.setEnergy(storage.getMaxEnergy());
            } else {
                inserted = amount;
                storage.setEnergy(storage.getEnergy() + amount);
            }
        }
        return inserted;
    }

    public static long clampLong(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }
}
