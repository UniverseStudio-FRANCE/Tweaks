package net.universestudio;

import net.universestudio.commands.FlyCommand;
import net.universestudio.commands.RenameCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        FlyCommand.registerFlyCommand(this);
        RenameCommand.registerRenameCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
