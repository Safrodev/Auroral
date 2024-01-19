package safro.auroral.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import safro.auroral.config.AuroralConfig;

import java.util.HashMap;

public interface EnergyConnectable extends EnergyStorage {

    default Direction[] getSides() {
        return Direction.values();
    }

    default HashMap<Direction, BlockPos> getTargets(World world, BlockPos pos) {
        HashMap<Direction, BlockPos> map = new HashMap<>();
        int max = MathHelper.clamp(AuroralConfig.get().maxBeam, 1, 10);
        for (Direction direction : this.getSides()) {
            for (int i = 1; i <= max; i++) {
                BlockPos target = pos.offset(direction, i);
                if (world.getBlockEntity(target) instanceof EnergyConnectable) {
                    map.put(direction, target);
                    break;
                } else if (!world.getBlockState(target).isAir()) {
                    break;
                }
            }
        }
        return map;
    }
}
