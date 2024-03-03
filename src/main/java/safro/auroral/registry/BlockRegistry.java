package safro.auroral.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import safro.auroral.Auroral;
import safro.auroral.block.FusionAltarBlock;
import safro.auroral.block.TestBlock;
import safro.auroral.block.entity.FusionAltarBlockEntity;
import safro.auroral.block.entity.TestBlockEntity;
import safro.saflib.registry.BaseBlockItemRegistry;

public class BlockRegistry extends BaseBlockItemRegistry {
    static { MODID = Auroral.MODID; }

    public static final Block TEST = register("test", new TestBlock(FabricBlockSettings.create()));
    public static final Block FUSION_ALTAR = register("fusion_altar", new FusionAltarBlock(FabricBlockSettings.create()));

    public static final BlockEntityType<TestBlockEntity> TEST_BE = register("test", TestBlockEntity::new, TEST);
    public static final BlockEntityType<FusionAltarBlockEntity> FUSION_ALTAR_BE = register("fusion_altar", FusionAltarBlockEntity::new, FUSION_ALTAR);

    public static void init() {}
}
