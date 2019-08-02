package com.ham5teak.plugins.cmdhelp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private CmdHelp plugin;
    private TaskHandler taskHandler;

    public CommandHandler(CmdHelp plugin) {
        this.plugin = plugin;
        taskHandler = new TaskHandler(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // ONLY run functionality for Player
        if(!(sender instanceof Player)) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Only Players can use this command!");
            return true;
        }

        Player p = (Player)sender;
        if(command.getName().equalsIgnoreCase("cmdhelp")) {
            if(args.length == 0) {
                if(p.hasPermission("cmdhelp.staff")) {
                    if(p.hasPermission("cmdhelp.rank0")) {
                        taskHandler.displayPlugins(p, 0);
                    } else if(p.hasPermission("cmdhelp.rank1")) {
                        taskHandler.displayPlugins(p, 1);
                    } else if(p.hasPermission("cmdhelp.rank2")) {
                        taskHandler.displayPlugins(p, 2);
                    } else if(p.hasPermission("cmdhelp.rank3")) {
                        taskHandler.displayPlugins(p, 3);
                    }
                } else if(p.hasPermission("cmdhelp.player")) {
                    //taskHandler.displayPlugins(p, 4);
                }
                return true;
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    if(p.hasPermission("cmdhelp.staff")) {
                        reloadConfig();
                        p.sendMessage(ChatColor.RED + "CmdHelp reload completed!");
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public TaskHandler getTaskHandler() {
        return taskHandler;
    }

    private void reloadConfig() {
        plugin.reloadConfig();
        taskHandler = new TaskHandler(plugin);
    }
}
