package net.universestudio.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.universestudio.utils.TextHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyCommand {
    private static final LiteralCommandNode<CommandSourceStack> flyCommand = Commands.literal("fly")
        .requires(context ->
            context.getSender().isOp() ||
                context.getSender().hasPermission("tweaks.*") ||
                context.getSender().hasPermission("tweaks.fly")
        )
        .executes(context -> {
            Entity executor = context.getSource().getExecutor();
            if (executor instanceof Player player) {
                boolean canFly = !player.getAllowFlight();
                player.setAllowFlight(canFly);
                player.setFlying(canFly);
                player.sendActionBar(TextHelper.text("<gold>Fly %s</gold>".formatted(canFly ? "enabled" : "disabled")));
            }

            return Command.SINGLE_SUCCESS;
        })
        .build();

    private static final LiteralCommandNode<CommandSourceStack> flySpeedCommand = Commands.literal("flyspeed")
        .requires(context ->
            context.getSender().isOp() ||
                context.getSender().hasPermission("tweaks.*") ||
                context.getSender().hasPermission("tweaks.flyspeed")
        )
        .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 10F))
            .executes(context -> {
                float speed = FloatArgumentType.getFloat(context, "speed");
                Entity executor = context.getSource().getExecutor();
                if (executor instanceof Player player) {
                    player.setFlySpeed(speed / 10);
                    player.sendMessage(TextHelper.prefixMessage("<gray>Fly <dark_aqua>speed</dark_aqua> set to <dark_aqua>%s</dark_aqua></gray>".formatted(TextHelper.FORMAT.format(speed))));
                }

                return Command.SINGLE_SUCCESS;
            })
        )
        .build();

    public static void registerFlyCommand(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(flyCommand);
            event.registrar().register(flySpeedCommand);
        });
    }
}
