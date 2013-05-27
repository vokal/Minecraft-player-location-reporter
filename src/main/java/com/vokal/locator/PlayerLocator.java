package com.vokal.locator;

import org.json.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.java_websocket.WebSocketImpl;

import java.net.URI;
import java.util.ArrayList;

import com.vokal.locator.command.*;
import com.vokal.locator.event.*;
import com.vokal.locator.point.*;

public final class PlayerLocator extends JavaPlugin {
    private SocketClient mSocket;

    private PointList<DeathPoint> mDeathPoints = new PointList<DeathPoint>(15);

    public void updateLocations(Player[] aPlayers) {
        JSONArray location_list = new JSONArray();
        long timestamp = System.currentTimeMillis() / 1000;

        try {
            for (Player player : aPlayers) {
                JSONObject loc = new JSONObject();
                loc.put("player", player.getDisplayName());
                loc.put("x", player.getLocation().getX());
                loc.put("y", player.getLocation().getY());
                loc.put("z", player.getLocation().getZ());
                loc.put("realm", player.getWorld().getEnvironment().toString());
                loc.put("timestamp", timestamp);
                
                location_list.put(loc);
            }

            JSONObject payload = new JSONObject();
            payload.put("players", location_list);
            payload.put("death_points", mDeathPoints.getSerializedPoints());

            emitMessage("update", payload);
        } catch (JSONException e) {
            getLogger().warning(e.toString());
        }
    }

    public synchronized void emitMessage(String aKey, JSONObject aMessage) {
        try {
            if (mSocket.isConnected()) {
                JSONObject payload = new JSONObject();
                payload.put("type", "position-update");
                payload.put("payload", aMessage);
                mSocket.send(payload.toString());
            } else {
                mSocket.connectBlocking();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void resetSocket() {
        try {
            String host = getConfig().getString("server");
            int port = getConfig().getInt("port");

            String uri = "ws://" + host + ":" + Integer.toString(port);
            mSocket = new SocketClient(this, uri);
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning(e.toString());
        }
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
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, mUpdateLocations, 10L, 10L);

        resetSocket();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
