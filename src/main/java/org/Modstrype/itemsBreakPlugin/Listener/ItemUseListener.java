package org.Modstrype.itemsBreakPlugin.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void onAnvilResultClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!(event.getInventory() instanceof AnvilInventory inventory)) return;

        // Slot 2 = Ergebnis-Slot im Amboss
        if (event.getRawSlot() != 2) return;

        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() == Material.AIR) return;

        if (random.nextDouble() < config.getDouble("destroy-chance", 0.4)) {
            // verhindere das Herausnehmen, wenn random kleiner als eingestellte Chance
            event.setCancelled(true);

            // lösche beide Eingabe-Items
            inventory.setItem(0, null);
            inventory.setItem(1, null);

            sendMessage(player, "messages.anvil-destroyed");
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