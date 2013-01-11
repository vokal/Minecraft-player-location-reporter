package com.vokal.locator.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.vokal.locator.PlayerLocator;

public class FartCommandExecutor implements CommandExecutor {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class
 
	public FartCommandExecutor(PlayerLocator aPlugin) {
		mPlugin = aPlugin;
	}
 
	@Override
	public boolean onCommand(CommandSender aSender, Command aCmd, String aLabel, String[] aArgs) {
        if (!(aSender instanceof Player)) {
            aSender.sendMessage("This command can only be run by a player.");
            return false;
        }

        Player player = (Player) aSender;
        mPlugin.getServer().broadcastMessage(ChatColor.DARK_RED + player.getDisplayName() + " farted!");

        return true;
	}
}
