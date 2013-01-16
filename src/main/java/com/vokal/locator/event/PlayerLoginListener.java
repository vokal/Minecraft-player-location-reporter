package com.vokal.locator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

import com.vokal.locator.PlayerLocator;

public class PlayerLoginListener implements Listener {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public PlayerLoginListener(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent aEvent) {
        Player[] players = mPlugin.getServer().getOnlinePlayers();

        // Remove the player from the array and send the updated list
        ArrayList<Player> totalplayers = 
            new ArrayList<Player>(Arrays.asList(players));
        totalplayers.add(aEvent.getPlayer());

        mPlugin.updateLocations(totalplayers.toArray(new Player[totalplayers.size()]));
    }
}
