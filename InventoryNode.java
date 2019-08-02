package com.ham5teak.plugins.cmdhelp;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryNode {
    private Inventory pluginInventory;
    private ArrayList<CommandInventory> commandInventories;

    public InventoryNode() {

    }

    public InventoryNode(Inventory pluginInventory, ArrayList<CommandInventory> commandInventories) {
        this.pluginInventory = pluginInventory;
        this.commandInventories = commandInventories;
    }

    public Inventory getPluginInventory() {
        return pluginInventory;
    }

    public void setPluginInventory(Inventory pluginInventory) {
        this.pluginInventory = pluginInventory;
    }

    public ArrayList<CommandInventory> getCommandInventories() {
        return commandInventories;
    }

    public void setCommandInventories(ArrayList<CommandInventory> commandInventories) {
        this.commandInventories = commandInventories;
    }
}
