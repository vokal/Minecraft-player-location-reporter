package com.vokal.locator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import com.vokal.locator.PlayerLocator;

public class PlayerQuitListener implements Listener {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public PlayerQuitListener(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent aEvent) {
        Player[] players = mPlugin.getServer().getOnlinePlayers();

        mPlugin.updateLocations(players);
    }
}
