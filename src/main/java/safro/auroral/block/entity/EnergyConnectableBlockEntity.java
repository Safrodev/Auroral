package safro.auroral.block.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import safro.auroral.api.block.Beam;
import safro.auroral.api.block.EnergyConnectable;
import safro.saflib.block.entity.BasicBlockEntity;

import java.util.HashMap;
import java.util.List;

public abstract class EnergyConnectableBlockEntity extends BasicBlockEntity implements EnergyConnectable {
    private long energy = 0;
    private List<Beam> beams = Lists.newArrayList();

    public EnergyConnectableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected static void tickConnections(World world, BlockPos pos, EnergyConnectableBlockEntity be) {
        if (be.getEnergy() > 0 && world.getTime() % 20 == 0) {
            List<Beam> updated = Lists.newArrayList();
            HashMap<Direction, BlockPos> targets = be.getTargets(world, pos);
            if (targets.isEmpty() && !be.beams.isEmpty()) {
                be.beams = Lists.newArrayList();
                be.update();
            } else {
                targets.forEach((direction, blockPos) -> {
                    int length = pos.getManhattanDistance(blockPos);
                    updated.add(Beam.of(direction, length));
                });
                be.beams = updated;
                be.update();
            }
        }
    }

    @Override
    public long getEnergy() {
        return this.energy;
    }

    public abstract long getMaxEnergy();

    @Override
    public void setEnergy(long amount) {
        this.energy = amount;
        this.update();
    }

    public void addEnergy(long amount) {
        this.setEnergy(Math.min(this.getMaxEnergy(), this.getEnergy() + amount));
    }

    public void removeEnergy(long amount) {
        this.setEnergy(Math.max(0, this.getEnergy() - amount));
    }

    public List<Beam> getBeams() {
        return this.beams;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.writeEnergy(nbt);
        if (!this.getBeams().isEmpty()) {
            NbtList list = new NbtList();
            for (Beam beam : this.getBeams()) {
                list.add(Beam.toNbt(beam));
            }
            nbt.put("Beams", list);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.readEnergy(nbt);
        if (nbt.contains("Beams")) {
            List<Beam> updated = Lists.newArrayList();
            NbtList list = nbt.getList("Beams", 10);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound tag = list.getCompound(i);
                updated.add(Beam.fromNbt(tag));
            }
            this.beams = updated;
        } else {
            this.beams = Lists.newArrayList();
        }
    }
}
