package safro.auroral.api.block;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class Beam {
    private final int length;
    private final Direction direction;

    private Beam(int l, Direction d) {
        this.length = l;
        this.direction = d;
    }

    public static Beam of(Direction direction, int length) {
        return new Beam(length, direction);
    }

    public static Beam fromNbt(NbtCompound tag) {
        Direction dir = Direction.byId(tag.getInt("Direction"));
        return new Beam(tag.getInt("Length"), dir);
    }

    public static NbtCompound toNbt(Beam beam) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("Direction", beam.getDirection().getId());
        tag.putInt("Length", beam.getLength());
        return tag;
    }

    public int getLength() {
        return this.length;
    }

    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public String toString() {
        return "{ " + this.direction.asString() + ", " + this.length + " }";
    }
}
