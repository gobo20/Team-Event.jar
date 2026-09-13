package com.example.eventplugin.commands;

import com.example.eventplugin.EventPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private static final Set<UUID> invited = new HashSet<>();

    public static void addInvited(Player p) {
        invited.add(p.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur als Spieler!");
            return true;
        }

        String cmd = command.getName().toLowerCase();
        boolean isBlue = cmd.equals("blau") || cmd.equals("blue");
        boolean isRed = cmd.equals("rot") || cmd.equals("red");

        // Falls Command über /blau oder /Blau aufgerufen wird
        if (label.equalsIgnoreCase("blau") || label.equalsIgnoreCase("blue") || isBlue) {
            joinTeam(player, "blue");
        } else if (label.equalsIgnoreCase("rot") || label.equalsIgnoreCase("red") || isRed) {
            joinTeam(player, "red");
        }
        return true;
    }

    private void joinTeam(Player player, String team) {
        // Optional: Nur eingeladene? Wir lassen es für alle zu, aber warnen
        // if (!invited.contains(player.getUniqueId()) && !player.isOp()) { ... }

        Location loc = getLocation(team);
        if (loc == null) {
            player.sendMessage("§8§l[§9Event§8] §cSpawn für Team " + team + " wurde noch nicht gesetzt! OP muss /event set " + team + " machen.");
            return;
        }

        // Teleport
        player.teleport(loc);
        
        // Kit geben
        giveKit(player, team);

        if (team.equals("blue")) {
            player.sendMessage("§8§l[§9Event§8] §7Du bist jetzt im §9§lBLAUEN TEAM§7!");
            player.sendTitle("§9§lBLAU", "§7Viel Glück!", 10, 60, 10);
        } else {
            player.sendMessage("§8§l[§9Event§8] §7Du bist jetzt im §c§lROTEN TEAM§7!");
            player.sendTitle("§c§lROT", "§7Viel Glück!", 10, 60, 10);
        }

        invited.remove(player.getUniqueId());
    }

    private Location getLocation(String team) {
        String path = "locations." + team;
        if (!EventPlugin.getInstance().getConfig().contains(path + ".world")) return null;
        
        String worldName = EventPlugin.getInstance().getConfig().getString(path + ".world");
        World world = org.bukkit.Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = EventPlugin.getInstance().getConfig().getDouble(path + ".x");
        double y = EventPlugin.getInstance().getConfig().getDouble(path + ".y");
        double z = EventPlugin.getInstance().getConfig().getDouble(path + ".z");
        float yaw = (float) EventPlugin.getInstance().getConfig().getDouble(path + ".yaw");
        float pitch = (float) EventPlugin.getInstance().getConfig().getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private void giveKit(Player player, String team) {
        player.getInventory().clear();

        Color color = team.equals("blue") ? Color.fromRGB(0, 0, 255) : Color.fromRGB(255, 0, 0);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        for (ItemStack piece : new ItemStack[]{helmet, chest, legs, boots}) {
            LeatherArmorMeta meta = (LeatherArmorMeta) piece.getItemMeta();
            meta.setColor(color);
            meta.setUnbreakable(true);
            piece.setItemMeta(meta);
        }

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chest);
        player.getInventory().setLeggings(legs);
        player.getInventory().setBoots(boots);

        // Eisenschwert mit Unbreaking 255
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addUnsafeEnchantment(Enchantment.UNBREAKING, 255);
        sword.getItemMeta().setUnbreakable(true);

        // Bogen mit Unbreaking 255
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.UNBREAKING, 255);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(bow);

        // 256 Pfeile = 4 Stacks
        ItemStack arrows = new ItemStack(Material.ARROW, 64);
        player.getInventory().addItem(arrows, arrows, arrows, arrows);
    }
}
