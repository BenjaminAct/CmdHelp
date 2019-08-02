package com.ham5teak.plugins.cmdhelp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskHandler {

    private CmdHelp plugin;

    // List of item nodes, each node consists of ItemStack and RankNo
    private ArrayList<ItemNode> pluginItems;
    private ArrayList<ItemNode> commandItems;

    // Inventory to act as GUI
    private ArrayList<InventoryNode> inventoryNodes;

    // Opened inventory
    private Inventory openedInventory;

    // Is plugin item position fixed in inventory
    private boolean isFixedPos;

    public TaskHandler(CmdHelp plugin) {
        this.plugin = plugin;
        pluginItems = new ArrayList<ItemNode>();
        commandItems = new ArrayList<ItemNode>();
        inventoryNodes = new ArrayList<InventoryNode>();
        isFixedPos = plugin.getConfig().getBoolean("Configurations.FixedPos");
        setupItemNodes();
        setupInventoryNodes();
    }

    private void setupItemNodes() {
        // Setup plugin and command items
        setupPluginNodes("PluginItems");
    }

    private void setupPluginNodes(String configSection) {
        for(String pluginKey : plugin.getConfig().getConfigurationSection(configSection).getKeys(false)) {
            String infoKeyPath = configSection+"."+pluginKey+".Info";
            String pluginName = plugin.getConfig().getString(infoKeyPath+".Name");
            String material = plugin.getConfig().getString(infoKeyPath+".Material");
            int displayPos = plugin.getConfig().getInt(infoKeyPath+".DisplayPos");
            String name = plugin.getConfig().getString(infoKeyPath+".Name");
            String lore = plugin.getConfig().getString(infoKeyPath+".Lore");
            int rankno = plugin.getConfig().getInt(infoKeyPath+".RankNo");
            ItemNode item = new ItemNode(pluginName, material, displayPos, name, lore, rankno);
            pluginItems.add(item);

            try {
                for(String commandKey : plugin.getConfig().getConfigurationSection(configSection+"."+pluginKey+".CommandItems").getKeys(false)) {
                    String commandKeyPath = configSection+"."+pluginKey+".CommandItems."+commandKey;
                    setupCommandNodes(pluginName, commandKeyPath);
                }
            } catch(NullPointerException e) {
                System.out.println("[CmdHelp] No command items found for plugin: "+pluginKey);
            }
        }
    }

    private void setupCommandNodes(String pName, String commandKeyPath) {
        String pluginName = pName;
        String material = plugin.getConfig().getString(commandKeyPath+".Material");
        int displayPos = plugin.getConfig().getInt(commandKeyPath+".DisplayPos");
        String name = plugin.getConfig().getString(commandKeyPath+".Name");
        String lore = plugin.getConfig().getString(commandKeyPath+".Lore");
        int rankno = plugin.getConfig().getInt(commandKeyPath+".RankNo");
        ItemNode item = new ItemNode(pluginName, material, displayPos, name, lore, rankno);
        commandItems.add(item);
    }

    private void setupInventoryNodes() {
        setupInventoryNode(0);
        setupInventoryNode(1);
        setupInventoryNode(2);
        setupInventoryNode(3);
        setupInventoryNode(4);
    }

    private void setupInventoryNode(int rankNo) {
        // Setup Plugin Inventory
        Inventory pluginInventory = setupPluginInventory(rankNo);

        // Setup Command Inventories
        ArrayList<CommandInventory> commandInventories = setupCommandInventory(pluginInventory, rankNo);

        // Create Inventory Node with Above Inventories
        InventoryNode inventoryNode = new InventoryNode(pluginInventory, commandInventories);
        inventoryNodes.add(inventoryNode);
    }

    private Inventory setupPluginInventory(int rankNo) {
        Inventory inv = Bukkit.createInventory(null, 36, "Command Help");

        switch(rankNo) {
            case 0: addPluginItems(inv, 0);
            case 1: addPluginItems(inv, 1);
            case 2: addPluginItems(inv, 2);
            case 3: addPluginItems(inv, 3);
                    break;
            case 4: addPluginItems(inv, 4);
                    break;
        }

        return inv;
    }

    // Add itemStack of the corresponding rankNo to inventory
    private void addPluginItems(Inventory inv, int rankNo) {
        for(ItemNode item : pluginItems) {
            if(item.getRankNo() == rankNo) {
                if(isFixedPos) {
                    inv.setItem(item.getDisplayPos(), item.createItemStack());
                } else {
                    inv.addItem(item.createItemStack());
                }
            }
        }
    }

    private ArrayList<CommandInventory> setupCommandInventory(Inventory pluginInventory, int rankNo) {
        ArrayList<CommandInventory> invs = new ArrayList<CommandInventory>();

        // For each plugin item, create command inventory
        for(ItemStack item : pluginInventory.getContents()) {
            if(item != null) {
                // Create command inventory for the plugin
                Inventory inv = Bukkit.createInventory(null, 36, item.getItemMeta().getDisplayName());

                // Add back button at bottom left(inventory size - 9) which brings player back to plugin inventory view
                ItemStack back = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Configurations.BackButton.Material")));
                ItemMeta meta = back.getItemMeta();
                meta.setDisplayName(plugin.getConfig().getString("Configurations.BackButton.Name"));
                meta.setLore(Arrays.asList(plugin.getConfig().getString("Configurations.BackButton.Lore")));
                back.setItemMeta(meta);
                inv.setItem(inv.getSize() - 9, back);

                // Add all command items of the plugin
                String pluginName = item.getItemMeta().getDisplayName();

                switch(rankNo) {
                    case 0: addCommandItems(inv, pluginName, 0);
                    case 1: addCommandItems(inv, pluginName, 1);
                    case 2: addCommandItems(inv, pluginName, 2);
                    case 3: addCommandItems(inv, pluginName, 3);
                            break;
                    case 4: addCommandItems(inv, pluginName, 4);
                            break;
                }

                // Create command inventory
                CommandInventory commandInventory = new CommandInventory(pluginName, inv);
                invs.add(commandInventory);
            }
        }
        return invs;
    }

    private void addCommandItems(Inventory inv, String pluginName, int rankNo) {
        for(ItemNode item : commandItems) {
            if(item.getPluginName().equals(pluginName)) {
                if(item.getRankNo() == rankNo) {
                    inv.addItem(item.createItemStack());
                }
            }
        }
    }

    private void openInventory(Player p, Inventory inventory) {
        p.openInventory(inventory);
        openedInventory = inventory;
    }

    private int getPlayerRank(Player p) {
        if(p.hasPermission("cmdhelp.rank0")) {
            return 0;
        } else if(p.hasPermission("cmdhelp.rank1")) {
            return 1;
        } else if(p.hasPermission("cmdhelp.rank2")) {
            return 2;
        } else if(p.hasPermission("cmdhelp.rank3")) {
            return 3;
        } else if(p.hasPermission("cmdhelp.player")) {
            return 4;
        }
        return 999;
    }

    public void displayPlugins(Player p, int rankNo) {
        Inventory inv = inventoryNodes.get(rankNo).getPluginInventory();
        openInventory(p, inv);
    }

    public void itemClickedHandler(InventoryClickEvent event) {
        // Get player who clicked
        if(event.getWhoClicked() instanceof Player) {
            Player p = (Player)event.getWhoClicked();
            // Compare clicked inventory with plugin inventory based on player's rank
            InventoryNode inventoryNode = inventoryNodes.get(getPlayerRank(p));
            try {
                if(event.getClickedInventory().equals(openedInventory)) {
                    ItemStack item = event.getCurrentItem();
                    if(!item.getType().toString().equalsIgnoreCase("air")) { // Do not fetch item data if is empty slot
                        String itemName = item.getItemMeta().getDisplayName();

                        // Back Button Clicked: Change to plugins view
                        // Command Item Clicked: Send command usage to the player
                        if(itemName.equals("Back")) {
                            displayPlugins(p, getPlayerRank(p));
                        } else if(item.getType().equals(Material.PAPER)) {
                            p.sendMessage(ChatColor.GOLD + item.getItemMeta().getDisplayName());
                        } else {
                            // Open the command inventory of the plugin clicked
                            ArrayList<CommandInventory> commandInventories = inventoryNode.getCommandInventories();
                            for(CommandInventory commandInventory : commandInventories) {
                                if(commandInventory.getPluginName().equals(itemName)) {
                                    openInventory(p, commandInventory.getInventory());
                                }
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            } catch(NullPointerException e) {
                System.out.println("[CmdHelper] Non Inventory Area is Clicked!");
            }
        }
    }
}
