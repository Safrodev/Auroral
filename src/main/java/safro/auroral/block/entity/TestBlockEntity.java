package safro.auroral.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.auroral.registry.BlockRegistry;
import safro.auroral.util.EnergyUtil;

public class TestBlockEntity extends EnergyConnectableBlockEntity {

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.TEST_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TestBlockEntity be) {
        tickConnections(world, pos, be);
        if (be.getEnergy() < be.getMaxEnergy() && world.getTime() % 20 == 0) {
            be.addEnergy(100);
            be.update();
        }

        if (world.getTime() % 20 == 0) {
            be.getTargets(world, pos).forEach((direction, blockPos) -> {
                if (world.getBlockEntity(blockPos) instanceof EnergyConnectableBlockEntity target) {
                    EnergyUtil.transfer(be, target, EnergyUtil.ENERGY_PER_TICK);
                }
            });
        }
    }

    @Override
    public long getMaxEnergy() {
        return 10000;
    }
}
