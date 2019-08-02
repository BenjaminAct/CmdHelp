package com.ham5teak.plugins.cmdhelp;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemNode {
    private String pluginName;
    private String material;
    private String name;
    private String lore;
    private int displayPos;
    private int rankNo;

    public ItemNode() {

    }

    public ItemNode(String pluginName, String material, int displayPos, String name, String lore, int rankNo) {
        this.pluginName = pluginName;
        this.material = material;
        this.displayPos = displayPos;
        this.name = name;
        this.lore = lore;
        this.rankNo = rankNo;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public int getDisplayPos() {
        return displayPos;
    }

    public void setDisplayPos(int displayPos) {
        this.displayPos = displayPos;
    }

    public int getRankNo() {
        return rankNo;
    }

    public void setRankNo(int rankNo) {
        this.rankNo = rankNo;
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(Material.getMaterial(material));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> loreList = new ArrayList<String>();
        loreList.add(lore);
        meta.setLore(loreList);
        item.setItemMeta(meta);

        return item;
    }
}
