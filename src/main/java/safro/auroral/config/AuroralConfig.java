package safro.auroral.config;

import blue.endless.jankson.Comment;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import safro.auroral.Auroral;

@Config(name = Auroral.MODID)
public class AuroralConfig implements ConfigData {
    @Comment("""
            The maximum distance (in blocks) that life energy storing blocks can transfer between each other
            Must be between 1 and 10
            Default: 5
            """)
    public int maxBeam = 5;

    @Comment("""
            The amount of life energy consumed per Fusion Altar craft
            Default: 1000
            """)
    public long fusionEnergyPerCraft = 1000;

    @Comment("""
            The time it takes for the Fusion Altar to craft (in ticks)
            20 ticks = 1 second
            Default: 100
            """)
    public int fusionCraftTime = 100;

    public static AuroralConfig get() {
        return AutoConfig.getConfigHolder(AuroralConfig.class).get();
    }
}
