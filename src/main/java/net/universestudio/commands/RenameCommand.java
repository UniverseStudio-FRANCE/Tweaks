package net.universestudio.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.universestudio.api.IPluginCommand;
import net.universestudio.utils.TextHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class RenameCommand implements IPluginCommand {
    private static final LiteralCommandNode<CommandSourceStack> renameCommand = Commands.literal("rename")
        .requires(context ->
            context.getSender().isOp() ||
                context.getSender().hasPermission("tweaks.*") ||
                context.getSender().hasPermission("tweaks.rename")
        )
        .then(Commands.argument("name", StringArgumentType.string())
            .executes(context -> {
                Entity executor = context.getSource().getExecutor();
                if (executor instanceof Player player) {
                    PlayerInventory inv = player.getInventory();
                    ItemStack item = inv.getItemInMainHand();

                    if (item.getType() != Material.AIR) {
                        String rawName = StringArgumentType.getString(context, "name");
                        if (rawName == null) rawName = "<!italic>";
                        if (rawName.isEmpty()) {
                            item.editMeta(meta -> meta.customName(null));

                            player.sendMessage(TextHelper.prefixMessage("<gray>Item custom name cleared</gray>"));
                        }
                        else {
                            if (!rawName.startsWith("<!italic>")) rawName = "<!italic>" + rawName;

                            Component name = TextHelper.text(rawName);
                            item.editMeta(meta -> meta.customName(name));

                            player.sendMessage(TextHelper.prefixMessage("<gray>Item renamed to %s</gray>".formatted(rawName)));
                        }
                    }
                }

                return Command.SINGLE_SUCCESS;
            })
        )
        .build();

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(renameCommand);
        });
    }
}
