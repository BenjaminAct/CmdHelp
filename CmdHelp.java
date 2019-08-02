package com.ham5teak.plugins.cmdhelp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CmdHelp extends JavaPlugin implements Listener {

    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        commandHandler = new CommandHandler(this);

        getCommand("cmdhelp").setExecutor(commandHandler);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void pluginClicked(InventoryClickEvent event) {
        commandHandler.getTaskHandler().itemClickedHandler(event);
    }
}
