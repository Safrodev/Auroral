package safro.auroral.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import safro.auroral.Auroral;
import safro.auroral.util.EnergyUtil;
import safro.auroral.api.LifeEnergyComponent;

public class ComponentsRegistry implements EntityComponentInitializer {
    public static final ComponentKey<LifeEnergyComponent> LIFE_ENERGY_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(Auroral.MODID, "life_energy"), LifeEnergyComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, LIFE_ENERGY_COMPONENT, LifeEnergyComponent::new);
    }

    public static void initEvents() {
        PlayerCopyCallback.EVENT.register((original, clone, lossless) -> {
            EnergyUtil.getFor(clone).setEnergy(EnergyUtil.getFor(original).getEnergy());
            EnergyUtil.getFor(clone).setEnergy(EnergyUtil.getFor(original).getMaxEnergy());
        });
    }
}
