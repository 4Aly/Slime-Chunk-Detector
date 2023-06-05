package me.slimechunk.slimechunkdetector;

import me.slimechunk.slimechunkdetector.commands.SlimeChunkCheck;
import me.slimechunk.slimechunkdetector.commands.give;
import me.slimechunk.slimechunkdetector.listener.Events;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class SlimeChunkDetector extends JavaPlugin {

    private static SlimeChunkDetector instance;
    private static SlimeChunkDetector plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        plugin = this;
        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Commands
        getCommand("sdcheck").setExecutor(new SlimeChunkCheck());
        getCommand("sdgive").setExecutor(new give());
        getServer().getPluginManager().registerEvents(new Events(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SlimeChunkDetector getInstance() {
        return instance;
    }
    public static SlimeChunkDetector getPlugin() {
        return plugin;
    }

    private void registerEvents()
    {
        this.getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    private void registerConfig()
    {
        this.saveDefaultConfig();
    }
}
