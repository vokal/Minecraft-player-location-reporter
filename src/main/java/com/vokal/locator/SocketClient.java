package com.vokal.locator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.*;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.*;

public class SocketClient extends WebSocketClient {
    private boolean mConnected = false;
    private PlayerLocator mPlugin;

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
    }

    @Override
    public void onOpen(ServerHandshake aHandshake) {
        mPlugin.getLogger().info("Connection established");
        mConnected = true;
    }

    public boolean isConnected() {
        return mConnected;
    }
}
