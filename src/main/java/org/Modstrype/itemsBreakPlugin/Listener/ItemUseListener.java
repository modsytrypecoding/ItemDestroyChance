package org.Modstrype.itemsBreakPlugin.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import org.Modstrype.itemsBreakPlugin.Main.Main;


import java.util.Random;

public class ItemUseListener implements Listener {

    private final Random random = new Random();
    private final FileConfiguration config;

    public ItemUseListener() {
        this.config = Main.getInstance().getConfig();
    }

    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack item1 = inventory.getItem(0); //item Slot kanns links im Anvil menu
        ItemStack item2 = inventory.getItem(1); //item Slot rechts daneben

        if (item1 == null || item2 == null) return;

        if (random.nextDouble() < config.getDouble("destroy-chance", 0.4)) { //random Zahl wird mit eingestellter Zahl abgeglichen default value = 0.4
            inventory.setItem(0, null); //setzt beide Felder Null --> Items verschwinden
            inventory.setItem(1, null);

            if (!inventory.getViewers().isEmpty() && inventory.getViewers().get(0) instanceof Player player) {
                sendMessage(player, "messages.anvil-destroyed"); //config Message
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {

        if (random.nextDouble() < config.getDouble("destroy-chance", 0.4)) { //random Zahl wird mit eingestellter Zahl abgeglichen default Value = 0.4
            Player player = event.getEnchanter();

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                // Entferne das Item aus dem Enchantment Table Slot
                event.getInventory().setItem(0, new ItemStack(Material.AIR)); //setzt verzauberslot = air --> Item verschwindet


                // Nachricht senden
                sendMessage(player, "messages.enchant-destroyed"); //config Message
            }, 1L);
        }
    }

    private void sendMessage(Player player, String path) {
        String msg = config.getString(path);
        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)); //richtiges Formatieren der config msg
        }
    }
}