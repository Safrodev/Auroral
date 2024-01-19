package safro.auroral.api.block;

import net.minecraft.nbt.NbtCompound;

public interface EnergyStorage {
    long getEnergy();
    void setEnergy(long amount);

    default void readEnergy(NbtCompound nbt) {
        this.setEnergy(nbt.getLong("LifeEnergy"));
    }

    default void writeEnergy(NbtCompound nbt) {
        nbt.putLong("LifeEnergy", this.getEnergy());
    }
}
