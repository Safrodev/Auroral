package safro.auroral;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import safro.auroral.command.AuroralCommand;
import safro.auroral.config.AuroralConfig;
import safro.auroral.registry.BlockRegistry;
import safro.auroral.registry.ComponentsRegistry;
import safro.auroral.registry.ItemRegistry;
import safro.auroral.registry.RecipeRegistry;
import safro.saflib.SafLib;

public class Auroral implements ModInitializer {
	public static final String MODID = "auroral";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final RegistryKey<ItemGroup> ITEM_GROUP = SafLib.createGroup(MODID);

	@Override
	public void onInitialize() {
		// Config
		AutoConfig.register(AuroralConfig.class, JanksonConfigSerializer::new);

		// Registry
		BlockRegistry.init();
		ItemRegistry.init();
		RecipeRegistry.init();

		// Events
		ComponentsRegistry.initEvents();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> AuroralCommand.register(dispatcher));

		SafLib.registerAll(ITEM_GROUP, ItemRegistry.LIVING_STEEL);
	}
}