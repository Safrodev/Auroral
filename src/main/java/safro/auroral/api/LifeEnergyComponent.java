package safro.auroral.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import safro.auroral.registry.ComponentsRegistry;

public class LifeEnergyComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final LivingEntity entity;
    private int energy = 0;
    private int maxEnergy = 1000;

    public LifeEnergyComponent(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void serverTick() {
        this.clamp();
        if (this.entity != null && this.entity.getWorld() != null) {
            if (this.energy < this.maxEnergy) {
                if (this.entity.getWorld().getTime() % 60 == 0) {
                    this.energy++;
                    ComponentsRegistry.LIFE_ENERGY_COMPONENT.sync(this.entity);
                }
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.energy = tag.getInt("LifeEnergy");
        this.maxEnergy = tag.getInt("MaxLifeEnergy");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("LifeEnergy", this.energy);
        tag.putInt("MaxLifeEnergy", this.maxEnergy);
    }

    public void clamp() {
        if (this.energy < 0) {
            this.setEnergy(1);
        }

        if (this.energy > this.maxEnergy) {
            this.setEnergy(this.maxEnergy);
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getMaxEnergy() {
        return this.maxEnergy;
    }

    public void setEnergy(int amount) {
        this.energy = amount;
    }

    public void setMaxEnergy(int amount) {
        this.maxEnergy = amount;
    }
}
