package safro.auroral.registry;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import safro.auroral.client.render.block.EnergyConnectableBlockEntityRenderer;

public class ClientRegistry {

    public static void init() {

        // Block Entity Renderers
        BlockEntityRendererFactories.register(BlockRegistry.TEST_BE, EnergyConnectableBlockEntityRenderer::new);
    }
}
