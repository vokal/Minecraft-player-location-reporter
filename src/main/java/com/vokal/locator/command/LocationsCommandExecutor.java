package com.vokal.locator.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.vokal.locator.PlayerLocator;

public class LocationsCommandExecutor implements CommandExecutor {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public LocationsCommandExecutor(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    private void broadcastUpdated(CommandSender aSender) {
        String host = mPlugin.getConfig().getString("server");
        int port = mPlugin.getConfig().getInt("port");

        aSender.sendMessage(ChatColor.BLUE + "Server URL set to http://" + host + ":" + Integer.toString(port));
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCmd, String aLabel, String[] aArgs) {
        if (aArgs.length != 2) {
            aSender.sendMessage("Not enough arguments!");
            return false;
        }

        if (aSender instanceof Player) {
            Player player = (Player) aSender;

            if (!player.hasPermission("locator.moderator")) {
                aSender.sendMessage("Not authorized to do that!");
                return false;
            }
        }

        if (aArgs[0].equals("server")) {
            mPlugin.getConfig().set("server", aArgs[1]);
            mPlugin.getLogger().info("Setting server to: " + aArgs[1]);
        } else if (aArgs[0].equals("port")) {
            try {
                mPlugin.getConfig().set("port", Integer.parseInt(aArgs[1]));
                mPlugin.getLogger().info("Setting port to: " + aArgs[1]);
            } catch (NumberFormatException e) {
                aSender.sendMessage("Port needs to be a valid integer");
                return false;
            }
        }

        try {
            mPlugin.resetSocket();
        } catch (Exception e) {
            mPlugin.getLogger().warning(e.toString());
        }

        broadcastUpdated(aSender);
        mPlugin.saveConfig();

        return true;
    }
}
