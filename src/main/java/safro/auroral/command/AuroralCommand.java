package safro.auroral.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import safro.auroral.Auroral;
import safro.auroral.registry.ComponentsRegistry;
import safro.auroral.util.EnergyUtil;

import java.util.Collection;

public class AuroralCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(Auroral.MODID)
                .requires(source -> source.hasPermissionLevel(2))

                // Set life energy
                .then(CommandManager.literal("setEnergy")
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                        .executes(context -> setLifeEnergy(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))))))
        );
    }

    private static int setLifeEnergy(ServerCommandSource source, Collection<ServerPlayerEntity> players, int amount) {
        for (ServerPlayerEntity player : players) {
            EnergyUtil.getFor(player).setEnergy(amount);
            ComponentsRegistry.LIFE_ENERGY_COMPONENT.sync(player);
        }

        if (players.size() == 1) {
            source.sendFeedback(() -> Text.translatable("command.auroral.set_one", players.iterator().next().getDisplayName(), amount), true);
        } else {
            source.sendFeedback(() -> Text.translatable("command.auroral.set", amount, players.size()), true);
        }
        return players.size();
    }
}
