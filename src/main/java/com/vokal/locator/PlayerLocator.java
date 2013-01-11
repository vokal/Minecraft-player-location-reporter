package com.vokal.locator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerLocator extends JavaPlugin {
    private Runnable mUpdateLocations = new Runnable() {
        @Override 
        public void run() {
            Player[] players = getServer().getOnlinePlayers();
            
            final JSONArray location_list = new JSONArray();
            for (Player player : players) {
                JSONObject loc = new JSONObject();
                loc.put("player", player.getDisplayName());
                loc.put("x", player.getLocation().getX());
                loc.put("y", player.getLocation().getY());
                loc.put("z", player.getLocation().getZ());
                
                location_list.add(loc);
            }
            
            new Thread() {
                public void run() {
                    try {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://localhost:5000/update");
                        httpPost.setEntity(new StringEntity(location_list.toString()));
                        httpPost.setHeader("Accept", "application/json");
                        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
                        
                        client.execute(httpPost);
                    } catch (Exception e) {
                        // Log
                        getLogger().warning(e.toString());
                    }
                }
            }.start();
        }
    };

    @Override
    public void onEnable() {
        getCommand("gob").setExecutor(new GobCommandExecutor(this));
        getCommand("fart").setExecutor(new FartCommandExecutor(this));

        getServer().getScheduler().scheduleSyncRepeatingTask(this, mUpdateLocations, 600L, 600L);
    }
}
