package com.vokal.locator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.*;
import org.java_websocket.handshake.ServerHandshake;

import org.bukkit.entity.Player;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.*;

public class SocketClient extends WebSocketClient {
    private boolean mConnected = false;
    private PlayerLocator mPlugin;

    private Runnable mUpdateLocations = new Runnable() {
        @Override 
        public void run() {
            Player[] players = mPlugin.getServer().getOnlinePlayers();
        
            mPlugin.updateLocations(players);
        }
    };

	public SocketClient(PlayerLocator aPlugin, String aUri) throws URISyntaxException {
        super(URI.create(aUri), new Draft_17());
        mPlugin = aPlugin;
        mPlugin.getLogger().info("Connecting to: " + aUri);
	}

    @Override
    public void onMessage(String aMessage) {
        try {
            JSONObject json = new JSONObject(aMessage);
            mPlugin.getLogger().info("Server said:" + json.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception aEx) {
        mPlugin.getLogger().info("An error occurred");
        aEx.printStackTrace();

        try {
            mPlugin.resetSocket();
        } catch (Exception e) {
            mPlugin.getLogger().warning(e.toString());
        }
    }

    @Override
    public void onClose(int aCode, String aReason, boolean aRemote) {
        mPlugin.getLogger().info("Connection terminated: " + aReason);
        mConnected = false;

        mPlugin.getServer().getScheduler().cancelAllTasks();
    }

    @Override
    public void onOpen(ServerHandshake aHandshake) {
        mPlugin.getLogger().info("Connection established");
        mConnected = true;

        mPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(mPlugin, mUpdateLocations, 10L, 10L);
    }

    public boolean isConnected() {
        return mConnected;
    }
}
