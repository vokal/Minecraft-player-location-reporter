package com.vokal.locator.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.Random;

import com.vokal.locator.PlayerLocator;

public class PeepCommandExecutor implements CommandExecutor {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    private Random mRandGen = new Random();

    public PeepCommandExecutor(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCmd, String aLabel, String[] aArgs) {
        if (!(aSender instanceof Player)) {
            aSender.sendMessage("This command can only be run by a player.");
            return false;
        }

        Player player = (Player) aSender;

        StringBuilder builder = new StringBuilder("8");
        int length = mRandGen.nextInt(10) + 1;
        for (int i = 0; i < length; ++i) {
            builder.append("=");
        }
        builder.append("D");
        if (mRandGen.nextBoolean()) {
            builder.append("~~~");
        }

        mPlugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + builder.toString());

        return true;
    }
}
