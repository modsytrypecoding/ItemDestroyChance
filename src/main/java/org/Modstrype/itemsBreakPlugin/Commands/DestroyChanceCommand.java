package org.Modstrype.itemsBreakPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.Modstrype.itemsBreakPlugin.Main.Main;

public class DestroyChanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var config = Main.getInstance().getConfig();

        if (!sender.isOp()) {
            send(sender, "messages.command.no-permission");
            return true;
        }

        if (args.length == 0) {
            double chance = config.getDouble("destroy-chance", 0.4);
            send(sender, "messages.command.current-chance", chance);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            try {
                double newChance = Double.parseDouble(args[1]);
                if (newChance < 0 || newChance > 1) {
                    send(sender, "messages.command.invalid-value");
                    return true;
                }

                config.set("destroy-chance", newChance);
                Main.getInstance().saveConfig();

                send(sender, "messages.command.set-success", newChance);
            } catch (NumberFormatException e) {
                send(sender, "messages.command.invalid-value");
            }
            return true;
        }

        send(sender, "messages.command.set-usage");
        return true;
    }

    private void send(CommandSender sender, String path) {
        String msg = Main.getInstance().getConfig().getString(path, "&cNachricht nicht gefunden.");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    private void send(CommandSender sender, String path, double chance) {
        String msg = Main.getInstance().getConfig().getString(path, "&cNachricht nicht gefunden.");
        msg = msg.replace("%chance%", String.valueOf(chance));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}