package org.Modstrype.itemsBreakPlugin.Main;

import org.Modstrype.itemsBreakPlugin.Commands.DestroyChanceCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.Modstrype.itemsBreakPlugin.Listener.ItemUseListener;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig(); // Erstellt Default-Konfiguration, falls nicht vorhanden
        getCommand("destroychance").setExecutor(new DestroyChanceCommand());
        getServer().getPluginManager().registerEvents(new ItemUseListener(), this);
        getLogger().info("************************************************");
        getLogger().info("[ModstrypeCoding] ItemDestroyChance aktiviert!");
        getLogger().info("************************************************");
    }

    @Override
    public void onDisable() { //Sendet eine Konsol msg bei Rl oder server close
        instance = this;
        getLogger().info("************************************************");
        getLogger().info("[ModstrypeCoding] ItemDestroyChance wird deaktiviert!");
        getLogger().info("************************************************");
    }
    public static Main getInstance() {
        return instance;
    }
}