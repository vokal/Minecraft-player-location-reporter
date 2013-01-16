package com.vokal.locator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.entity.Player;

import com.vokal.locator.PlayerLocator;

public class PlayerLoginListener implements Listener {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public PlayerLoginListener(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent aEvent) {
        Player[] players = new Player[mPlugin.getServer().getOnlinePlayers().length + 1];
        players[players.length] = aEvent.getPlayer();

        mPlugin.updateLocations(players);
    }
}
