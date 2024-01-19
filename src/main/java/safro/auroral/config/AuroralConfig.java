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

    public static AuroralConfig get() {
        return AutoConfig.getConfigHolder(AuroralConfig.class).get();
    }
}
