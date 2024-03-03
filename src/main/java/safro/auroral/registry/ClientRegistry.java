package safro.auroral.registry;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import safro.auroral.client.render.block.EnergyConnectableBlockEntityRenderer;
import safro.auroral.client.render.block.FusionAltarBlockEntityRenderer;

public class ClientRegistry {

    public static void init() {

        // Block Entity Renderers
        BlockEntityRendererFactories.register(BlockRegistry.TEST_BE, EnergyConnectableBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockRegistry.FUSION_ALTAR_BE, FusionAltarBlockEntityRenderer::new);
    }
}
