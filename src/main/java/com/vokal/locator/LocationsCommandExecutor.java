package com.vokal.locator;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class LocationsCommandExecutor implements CommandExecutor {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class
 
	public LocationsCommandExecutor(PlayerLocator aPlugin) {
		mPlugin = aPlugin;
	}
 
	@Override
	public boolean onCommand(CommandSender aSender, Command aCmd, String aLabel, String[] aArgs) {
        if (aArgs.length != 2) {
            aSender.sendMessage("Not enough arguments!");
            return false;
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

        mPlugin.saveConfig();

        return true;
	}
}
