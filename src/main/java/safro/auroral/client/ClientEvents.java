package safro.auroral.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import safro.auroral.Auroral;
import safro.auroral.util.EnergyUtil;

public class ClientEvents {
    public static final Identifier ENERGY_BAR_TEXTURE = new Identifier(Auroral.MODID, "textures/gui/energy_bar.png");

    public static void init() {
    }

    public static void renderHud(DrawContext context, TextRenderer textRenderer, PlayerEntity player, int width, int height) {
        if (player != null && player.isAlive()) {
            float energy = EnergyUtil.getFor(player).getEnergy();
            float maxEnergy = EnergyUtil.getFor(player).getMaxEnergy();
            int x = (width / 2) + 110;
            int y = height - 15;

            context.drawTexture(ENERGY_BAR_TEXTURE, x, y, 0, 0, 100, 9, 128, 128);
            if (energy > 0) {
                context.drawTexture(ENERGY_BAR_TEXTURE, x, y, 0, 9, (int) ((energy / maxEnergy) * 100.0F), 9, 128, 128);
            }

            // Debug
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                Text text = Text.literal(energy + "/" + maxEnergy);
                context.drawTextWithShadow(textRenderer, text, x, y - 15, 16777215);
            }
        }
    }
}
