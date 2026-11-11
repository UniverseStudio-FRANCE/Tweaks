package net.universestudio;

import net.universestudio.commands.FlyCommand;
import net.universestudio.commands.LockCommand;
import net.universestudio.commands.RenameCommand;
import net.universestudio.events.LockListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tweaks extends JavaPlugin {

    private static Tweaks instance;
    public static NamespacedKey lockedKey;

    @Override
    public void onEnable() {
        instance = this;
        lockedKey = key("locked");

        new FlyCommand().register(this);
        new LockCommand().register(this);
        new RenameCommand().register(this);

        this.getServer().getPluginManager().registerEvents(new LockListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NamespacedKey key(String key) {
        return new NamespacedKey(instance, key);
    }
}
