package com.example.eventplugin.commands;

import com.example.eventplugin.EventPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("event.admin")) {
            sender.sendMessage("§cKeine Rechte!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§8§l[§9Event§8] §7Benutzung:");
            sender.sendMessage("§7/event <Spieler> §8- §7Spieler zum Event einladen");
            sender.sendMessage("§7/event set red §8- §7Roten Spawn setzen");
            sender.sendMessage("§7/event set blue §8- §7Blauen Spawn setzen");
            return true;
        }

        // /event set <red|blue>
        if (args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§cNur als Spieler ausführbar!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("§c/event set <red|blue>");
                return true;
            }
            String team = args[1].toLowerCase();
            if (!team.equals("red") && !team.equals("blue") && !team.equals("rot") && !team.equals("blau")) {
                sender.sendMessage("§cNutze: red oder blue");
                return true;
            }
            String key = team.startsWith("r") ? "red" : "blue";
            Location loc = p.getLocation();

            EventPlugin.getInstance().getConfig().set("locations." + key + ".world", loc.getWorld().getName());
            EventPlugin.getInstance().getConfig().set("locations." + key + ".x", loc.getX());
            EventPlugin.getInstance().getConfig().set("locations." + key + ".y", loc.getY());
            EventPlugin.getInstance().getConfig().set("locations." + key + ".z", loc.getZ());
            EventPlugin.getInstance().getConfig().set("locations." + key + ".yaw", loc.getYaw());
            EventPlugin.getInstance().getConfig().set("locations." + key + ".pitch", loc.getPitch());
            EventPlugin.getInstance().saveConfig();

            sender.sendMessage("§8§l[§9Event§8] §aSpawn für Team " + (key.equals("red") ? "§cROT" : "§9BLAU") + " §agesetzt bei §7" + (int)loc.getX() + " " + (int)loc.getY() + " " + (int)loc.getZ());
            return true;
        }

        // /event <Spieler>
        String playerName = args[0];
        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            target = Bukkit.getPlayer(playerName);
        }
        if (target == null) {
            sender.sendMessage("§cSpieler " + playerName + " nicht online!");
            return true;
        }

        // Einladung
        TeamCommand.addInvited(target);

        target.sendMessage("");
        target.sendMessage("§8§m------------------------------");
        target.sendMessage("§8§l[§9Event§8§l] §fDu wurdest zum Event eingeladen!");
        target.sendMessage("");
        target.sendMessage("§7Schreibe §9/Blau §7in den Chat für §9Blaues Team");
        target.sendMessage("§7Schreibe §c/Rot §7in den Chat für §cRotes Team");
        target.sendMessage("§8§m------------------------------");
        target.sendMessage("");

        // Klickbare Nachrichten für neuere Versionen
        target.sendMessage("§7Klicke: §9[BLAU] §8| §c[ROT]");
        
        sender.sendMessage("§aSpieler " + target.getName() + " wurde eingeladen.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("set");
            for (Player p : Bukkit.getOnlinePlayers()) list.add(p.getName());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            list.addAll(Arrays.asList("red", "blue"));
        }
        return list;
    }
}
