package safro.auroral.registry;

import net.minecraft.item.Item;
import safro.auroral.Auroral;
import safro.saflib.registry.BaseBlockItemRegistry;

public class ItemRegistry extends BaseBlockItemRegistry {
    static { MODID = Auroral.MODID; }

    public static final Item LIVING_STEEL = register("living_steel", new Item(settings()));

    public static void init() {}
}
