package com.vokal.locator;

import io.socket.*;
import org.json.*;

public class SocketCallback implements IOCallback {
    private PlayerLocator mPlugin;

	public SocketCallback(PlayerLocator aPlugin) {
        mPlugin = aPlugin;
	}

    @Override
    public void onMessage(JSONObject json, IOAcknowledge ack) {
        try {
            mPlugin.getLogger().info("Server said:" + json.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String data, IOAcknowledge ack) {
        mPlugin.getLogger().info("Server said: " + data);
    }

    @Override
    public void onError(SocketIOException socketIOException) {
        mPlugin.getLogger().info("An error occurred");
        socketIOException.printStackTrace();

        try {
            mPlugin.resetSocket();
        } catch (Exception e) {
            mPlugin.getLogger().warning(e.toString());
        }
    }

    @Override
    public void onDisconnect() {
        mPlugin.getLogger().info("Connection terminated");
    }

    @Override
    public void onConnect() {
        mPlugin.getLogger().info("Connection established");
    }

    @Override
    public void on(String event, IOAcknowledge ack, Object... args) {
        // System.out.println("Server triggered event '" + event + "'");
    }
}
