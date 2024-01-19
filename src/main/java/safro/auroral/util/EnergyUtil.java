package safro.auroral.util;

import net.minecraft.entity.LivingEntity;
import safro.auroral.api.LifeEnergyComponent;
import safro.auroral.registry.ComponentsRegistry;

public class EnergyUtil {

    public static LifeEnergyComponent getFor(LivingEntity entity) {
        return ComponentsRegistry.LIFE_ENERGY_COMPONENT.get(entity);
    }
}
