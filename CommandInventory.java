package com.ham5teak.plugins.cmdhelp;

import org.bukkit.inventory.Inventory;

public class CommandInventory {
    private String pluginName;
    private Inventory inventory;

    public CommandInventory() {

    }

    public CommandInventory(String pluginName, Inventory inventory) {
        this.pluginName = pluginName;
        this.inventory = inventory;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
