package com.vokal.locator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

import com.vokal.locator.PlayerLocator;
import com.vokal.locator.point.DeathPoint;

public class PlayerDeathListener implements Listener {
    private PlayerLocator mPlugin;

    public PlayerDeathListener(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent aEvent) {
        mPlugin.getLogger().info("Player " + aEvent.getEntity().getDisplayName() + " died!");
        mPlugin.addDeathPoint(new DeathPoint(aEvent.getEntity(), 
                    aEvent.getEntity().getLocation(), aEvent.getEntity().getWorld().getEnvironment().toString()));

        Player[] players = mPlugin.getServer().getOnlinePlayers();
        mPlugin.updateLocations(players);
    }
}
