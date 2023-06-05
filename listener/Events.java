package me.slimechunk.slimechunkdetector.listener;

import me.slimechunk.slimechunkdetector.SlimeChunkDetector;
import me.slimechunk.slimechunkdetector.utils.HexFormat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;

import static me.slimechunk.slimechunkdetector.SlimeChunkDetector.getPlugin;

public class Events implements Listener
{

    private SlimeChunkDetector plugin;

    public Events(SlimeChunkDetector plugin)
    {
        this.plugin = plugin;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();


        //if player right clicked w/main hand
        if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND)
        {

            if(itemInMainHand == null) {

                return;
            }
            if(itemInMainHand.getItemMeta() == null) {

                return;
            }
            if(itemInMainHand.getItemMeta().getDisplayName() == null) {

                return;
            }
            String ItemName = itemInMainHand.getItemMeta().getDisplayName();
                //if there are enchants
            if(itemInMainHand.getEnchantments().size() != 0) {
                if (itemInMainHand.getItemMeta().getDisplayName().equals(SlimeChunkDetector.getInstance().getConfig().getString("item.name"))) {
                    ItemMeta im = itemInMainHand.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    ArrayList<String> lore2 = new ArrayList<>();

                    PersistentDataContainer data = im.getPersistentDataContainer();
                    if (im.getPersistentDataContainer().get(new NamespacedKey(plugin,"uses"), PersistentDataType.INTEGER).equals(1)){
                        data.set(new NamespacedKey(getPlugin(), "uses"), PersistentDataType.INTEGER, 2);
                        lore.add("§4§l2 Uses Left!");
                        im.setLore(lore);
                        itemInMainHand.setItemMeta(im);
                    }else if (im.getPersistentDataContainer().get(new NamespacedKey(plugin,"uses"), PersistentDataType.INTEGER).equals(2)){
                        data.set(new NamespacedKey(getPlugin(), "uses"), PersistentDataType.INTEGER, 3);
                        itemInMainHand.setItemMeta(im);
                        lore2.add("§4§l1 Use Left!");
                        im.setLore(lore2);
                        itemInMainHand.setItemMeta(im);
                    }else if (im.getPersistentDataContainer().get(new NamespacedKey(plugin,"uses"), PersistentDataType.INTEGER).equals(3)){
                        p.sendMessage(SlimeChunkDetector.getInstance().getConfig().getString("message.ran-out-of-uses"));
                        itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
                    }

                    String message;
                    Chunk chunk = p.getLocation().getChunk();
                    if (chunk.isSlimeChunk()) {
                        message = HexFormat.format(SlimeChunkDetector.getInstance().getConfig().getString("isSlimeChunkActionBar"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);

                        int minX = chunk.getX() * 16;
                        int minZ = chunk.getZ() * 16;
                        int minY = p.getLocation().getBlockY();
                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(100, 255, 0), 1);

                        for (int y = 0; y < 3; y++) {
                            for (int x = 0; x < 17; x++) {
                                p.spawnParticle(Particle.REDSTONE, minX + x, minY + y, minZ, 20, dustOptions);
                            }
                            for (int x = 0; x < 17; x++) {
                                p.spawnParticle(Particle.REDSTONE, minX + x, minY + y, minZ + 16, 20, dustOptions);
                            }
                            for (int z = 0; z < 17; z++) {
                                p.spawnParticle(Particle.REDSTONE, minX, minY + y, minZ + z, 20, dustOptions);
                            }
                            for (int z = 0; z < 17; z++) {
                                p.spawnParticle(Particle.REDSTONE, minX + 16, minY + y, minZ + z, 20, dustOptions);
                            }
                        }


                        if (SlimeChunkDetector.getInstance().getConfig().getBoolean("doSound")) {
                            p.playSound(p.getLocation(),
                                    Sound.valueOf(SlimeChunkDetector.getInstance().getConfig().getString("soundSettings.sound")),
                                    (float) SlimeChunkDetector.getInstance().getConfig().getDouble("soundSettings.vol"),
                                    (float) SlimeChunkDetector.getInstance().getConfig().getDouble("soundSettings.pitch"));
                        }
                    } else {
                        message = HexFormat.format(SlimeChunkDetector.getInstance().getConfig().getString("isNotSlimeChunkActionBar"));

                    }

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 29);

                } else {

                    return;

                }
            }
        }
    }
}
