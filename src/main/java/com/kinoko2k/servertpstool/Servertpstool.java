package com.kinoko2k.servertpstool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.dsig.spec.TransformParameterSpec;

public final class Servertpstool extends JavaPlugin implements Listener {
    @Override
    public void onEnable(){
        // Register events
        this.getServer().getPluginManager().registerEvents(this, this);
        // Log loading
        this.getLogger().info("ロード中 " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
    }

    @Override
    public void onDisable(){
        // Log disabling
        this.getLogger().info("Disabled " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
    }

    Class<?> CPClass;

    String serverName  = Bukkit.getServer().getClass().getPackage().getName(),
            serverVersion = serverName.substring(serverName.lastIndexOf(".") + 1, serverName.length());

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String prefix = ChatColor.RED + "[Ping]" + ChatColor.RESET;

        if (cmd.getName().equalsIgnoreCase("ping")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(prefix + " " + "[ServerTpsTool]あなたのPing値: " + ChatColor.GRAY + "0 ms" + ChatColor.RESET + "...");
                    return true;
                } else {
                    if (sender.hasPermission("servertps.player")) {
                        // Define player object
                        final Player p = (Player) sender;
                        sender.sendMessage(prefix + " " + "[ServerTpsTool]あなたのPing値: " + ChatColor.GRAY + getPing(p) + " ms" + ChatColor.RESET);
                        return true;
                    } else {
                        sender.sendMessage(prefix + " " + "あなたにPing値を表示できるわけないやん...こちらにも権限が...: " + ChatColor.GRAY + "- servertps.player");
                        return true;
                    }
                }
            } else if (args.length == 1) {
                if (sender.hasPermission("servertps.op")) {
                    Player argplayer = getServer().getPlayer(args[0]);
                    if (argplayer == null) {
                        sender.sendMessage(prefix + " " + "The player " + ChatColor.GRAY + args[0] + ChatColor.RESET + " could not be found");
                        return true;
                    }
                    // Send command overview
                    sender.sendMessage(prefix + " " + getServer().getPlayer(args[0]).getDisplayName().toString() + "'s ping: " + ChatColor.GRAY + getPing(argplayer) + " ms" + ChatColor.RESET);
                    return true;
                } else {
                    sender.sendMessage(prefix + " " + "あなたにPing値を表示できるわけないやん...こちらにも権限が...: " + ChatColor.GRAY + "- servertps.op");
                    return true;
                }
            } else {
                // Send command overview
                sender.sendMessage(prefix + ChatColor.YELLOW + " Plugin help:");
                sender.sendMessage("/ping" + ChatColor.GRAY + " - " + ChatColor.GOLD + "Pingを確認してください。");
                sender.sendMessage("/ping <player>" + ChatColor.GRAY + " - " + ChatColor.GOLD + "PlayerのPingを確認してください。");
                return true;
            }

        }
        if (cmd.getName().equalsIgnoreCase("ping")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(prefix + " " + "[ServerTpsTool]あなたのPing値: " + ChatColor.GRAY + "0 ms" + ChatColor.RESET + "...");
                    return true;
                } else {
                    if (sender.hasPermission("servertps.player")) {
                        // Define player object
                        final Player p = (Player) sender;
                        sender.sendMessage(prefix + " " + "[ServerTpsTool]あなたのPing値: " + ChatColor.GRAY + getPing(p) + " ms" + ChatColor.RESET);
                        return true;
                    } else {
                        sender.sendMessage(prefix + " " + "あなたにPing値を表示できるわけないやん...こちらにも権限が...: " + ChatColor.GRAY + "- servertps.player");
                        return true;
                    }
                }
            } else if (args.length == 1) {
                if (sender.hasPermission("servertps.op")) {
                    Player argplayer = getServer().getPlayer(args[0]);
                    if (argplayer == null) {
                        sender.sendMessage(prefix + " " + "The player " + ChatColor.GRAY + args[0] + ChatColor.RESET + " could not be found");
                        return true;
                    }
                    // Send command overview
                    sender.sendMessage(prefix + " " + getServer().getPlayer(args[0]).getDisplayName().toString() + "'s ping: " + ChatColor.GRAY + getPing(argplayer) + " ms" + ChatColor.RESET);
                    return true;
                } else {
                    sender.sendMessage(prefix + " " + "あなたにPing値を表示できるわけないやん...こちらにも権限が...: " + ChatColor.GRAY + "- servertps.op");
                    return true;
                }
            } else {
                // Send command overview
                sender.sendMessage(prefix + ChatColor.YELLOW + " Plugin help:");
                sender.sendMessage("/ping" + ChatColor.GRAY + " - " + ChatColor.GOLD + "Pingを確認してください。");
                sender.sendMessage("/ping <player>" + ChatColor.GRAY + " - " + ChatColor.GOLD + "PlayerのPingを確認してください。");
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("playerlist")) {
            StringBuilder online = new StringBuilder();
            final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player player : players) {

                if (sender instanceof Player && !((Player) sender).canSee(player))
                    continue;
                if (online.length() > 0) {
                    online.append(", ");
                }
                online.append(player.getDisplayName());
            }
            sender.sendMessage("There are " + players.size() + "/" + Bukkit.getMaxPlayers() + " players online:\n" + online.toString());
            return true;
        }

        return false;
    }

    public int getPing(Player p) {
        try {
            CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object CraftPlayer = CPClass.cast(p);

            Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
            Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);

            Field ping = EntityPlayer.getClass().getDeclaredField("ping");

            return ping.getInt(EntityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
