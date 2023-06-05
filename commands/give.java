package me.slimechunk.slimechunkdetector.commands;

import me.slimechunk.slimechunkdetector.SlimeChunkDetector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class give implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(SlimeChunkDetector.getInstance().getConfig().getBoolean("requirePerms")) {
            if (!(sender.hasPermission("slimecheck.command"))) {
                sender.sendMessage(SlimeChunkDetector.getInstance().getConfig().getString("message.no-permission"));
                return false;
            }
        }

        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(SlimeChunkDetector.getInstance().getConfig().getString("item.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§4§l3 Uses Left!");
        im.setLore(lore);
        im.addEnchant(Enchantment.MENDING, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer data = im.getPersistentDataContainer();
        data.set(new NamespacedKey(SlimeChunkDetector.getPlugin(), "uses"), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(im);
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.getInventory().addItem(itemStack);
                p.sendMessage(SlimeChunkDetector.getInstance().getConfig().getString("message.give-item"));
                return true;
            }
        } else if (args.length == 1) {
            String playerName = args[0];
            Player target = sender.getServer().getPlayerExact(playerName);
            if (target == null) { //check whether the player is online
                sender.sendMessage("Player " + playerName + " is not online.");
            } else {
                target.getInventory().addItem(itemStack);
                Player p = (Player) sender;
                p.sendMessage("Item Given to " + target);
                target.sendMessage(SlimeChunkDetector.getInstance().getConfig().getString("message.ran-out-of-uses"));
            }
            return true;
        } else return false;
        return true;
    }
}

