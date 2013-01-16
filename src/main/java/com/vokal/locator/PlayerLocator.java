package com.vokal.locator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.util.logging.Logger;
import java.util.logging.Level;

import io.socket.*;

import com.vokal.locator.command.*;
import com.vokal.locator.event.*;

public final class PlayerLocator extends JavaPlugin {
    private SocketIO mSocket;

    public void updateLocations(Player[] aPlayers) {
        final JSONArray location_list = new JSONArray();

        for (Player player : aPlayers) {
            JSONObject loc = new JSONObject();
            loc.put("player", player.getDisplayName());
            loc.put("x", player.getLocation().getX());
            loc.put("y", player.getLocation().getY());
            loc.put("z", player.getLocation().getZ());
            loc.put("realm", player.getWorld().getEnvironment().toString());
            
            location_list.add(loc);
        }

        mSocket.emit("update", location_list);
    }

    private Runnable mUpdateLocations = new Runnable() {
        @Override 
        public void run() {
            Player[] players = getServer().getOnlinePlayers();

            if (players.length == 0) {
                return;
            }
        
            PlayerLocator.this.updateLocations(players);
        }
    };

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("gob").setExecutor(new GobCommandExecutor(this));
        getCommand("fart").setExecutor(new FartCommandExecutor(this));
        getCommand("locations").setExecutor(new LocationsCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, mUpdateLocations, 600L, 600L);

        try {
            mSocket = new SocketIO("http://localhost:5000");
            mSocket.connect(new SocketCallback(this));

            // This line is cached until the connection is establisched.
            mSocket.send("Hello!");
        } catch (Exception e) {
            getLogger().warning(e.toString());
        }

        Logger.getLogger("io.socket").setLevel(Level.WARNING);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
