package com.vokal.locator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

import com.vokal.locator.PlayerLocator;

public class PlayerMoveListener implements Listener {
    private PlayerLocator mPlugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public PlayerMoveListener(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent aEvent) {
        double distance = aEvent.getFrom().distance(aEvent.getTo());

        // Let's not be greedy
        if (distance < 0.2) {
            return;
        }

        Player[] players = mPlugin.getServer().getOnlinePlayers();

        mPlugin.updateLocations(players);
    }
}
