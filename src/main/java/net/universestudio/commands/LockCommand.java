package net.universestudio.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.universestudio.Tweaks;
import net.universestudio.api.IPluginCommand;
import net.universestudio.utils.TextHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class LockCommand implements IPluginCommand {
    private static final LiteralCommandNode<CommandSourceStack> lockCommand = Commands.literal("lock")
        .requires(context ->
            context.getSender().isOp() ||
                context.getSender().hasPermission("tweaks.*") ||
                context.getSender().hasPermission("tweaks.lock")
        )
        .executes(context -> {
            Entity executor = context.getSource().getExecutor();
            if (executor instanceof Player player) {
                PlayerInventory inv = player.getInventory();
                ItemStack item = inv.getItemInMainHand();

                if (item.getType() != Material.AIR) {
                    item.editMeta(meta -> {
                       PersistentDataContainer pdc = meta.getPersistentDataContainer();
                       pdc.set(Tweaks.lockedKey, PersistentDataType.BYTE, (byte) 1);
                    });

                    player.sendMessage(TextHelper.prefixMessage("<gray>Item locked! You can no longer drop it <dark_aqua>✔</dark_aqua></gray>"));
                }
            }

            return Command.SINGLE_SUCCESS;
        })
        .build();

    private static final LiteralCommandNode<CommandSourceStack> unlockCommand = Commands.literal("unlock")
        .requires(context ->
            context.getSender().isOp() ||
                context.getSender().hasPermission("tweaks.*") ||
                context.getSender().hasPermission("tweaks.unlock")
        )
        .executes(context -> {
            Entity executor = context.getSource().getExecutor();
            if (executor instanceof Player player) {
                PlayerInventory inv = player.getInventory();
                ItemStack item = inv.getItemInMainHand();

                if (item.getType() != Material.AIR && item.hasItemMeta()) {
                    item.editMeta(meta -> {
                        PersistentDataContainer pdc = meta.getPersistentDataContainer();
                        if (pdc.has(Tweaks.lockedKey)) {
                            pdc.remove(Tweaks.key("locked"));
                            player.sendMessage(TextHelper.prefixMessage("<gray>Item unlocked! Be careful - you can now drop your item <dark_aqua>⚠</dark_aqua></gray>"));
                        }
                    });
                }
            }

            return Command.SINGLE_SUCCESS;
        })
        .build();

    @Override
    public void register(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(lockCommand);
            event.registrar().register(unlockCommand);
        });
    }
}
