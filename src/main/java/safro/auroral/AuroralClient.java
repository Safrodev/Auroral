package safro.auroral;

import net.fabricmc.api.ClientModInitializer;
import safro.auroral.client.ClientEvents;
import safro.auroral.registry.ClientRegistry;

public class AuroralClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientRegistry.init();
        ClientEvents.init();
    }
}
