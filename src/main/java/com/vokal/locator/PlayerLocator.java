package com.vokal.locator;

import org.json.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import io.socket.*;

import com.vokal.locator.command.*;
import com.vokal.locator.event.*;
import com.vokal.locator.point.*;

public final class PlayerLocator extends JavaPlugin {
    private SocketIO mSocket;

    private PointList<DeathPoint> mDeathPoints = new PointList<DeathPoint>(15);

    public void updateLocations(Player[] aPlayers) {
        final JSONArray location_list = new JSONArray();

        try {
            for (Player player : aPlayers) {
                JSONObject loc = new JSONObject();
                loc.put("player", player.getDisplayName());
                loc.put("x", player.getLocation().getX());
                loc.put("y", player.getLocation().getY());
                loc.put("z", player.getLocation().getZ());
                loc.put("realm", player.getWorld().getEnvironment().toString());
                
                location_list.put(loc);
            }

            JSONObject payload = new JSONObject();
            payload.put("players", location_list);
            payload.put("death_points", mDeathPoints.getSerializedPoints());

            mSocket.emit("update", payload);
        } catch (JSONException e) {
            getLogger().warning(e.toString());
        }
    }

    private Runnable mUpdateLocations = new Runnable() {
        @Override 
        public void run() {
            Player[] players = getServer().getOnlinePlayers();
        
            PlayerLocator.this.updateLocations(players);
        }
    };

    public void resetSocket() throws Exception {
        String host = getConfig().getString("server");
        int port = getConfig().getInt("port");

        mSocket = new SocketIO("http://" + host + ":" + Integer.toString(port));
        mSocket.connect(new SocketCallback(this));
    }

    public void addDeathPoint(DeathPoint aPoint) {
        mDeathPoints.add(aPoint);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("gob").setExecutor(new GobCommandExecutor(this));
        getCommand("fart").setExecutor(new FartCommandExecutor(this));
        getCommand("locations").setExecutor(new LocationsCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        // getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, mUpdateLocations, 10L, 10L);

        try {
            resetSocket();

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
