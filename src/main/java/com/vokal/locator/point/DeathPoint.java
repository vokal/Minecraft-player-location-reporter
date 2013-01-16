package com.vokal.locator.point;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import org.json.JSONObject;
import org.json.JSONException;

public class DeathPoint extends Point {
    private String mPlayer;
    private double mX;
    private double mY;
    private double mZ;
    private String mRealm;

    public DeathPoint(Player aPlayer, Location aLocation, String aRealm) {
        mPlayer = aPlayer.getDisplayName();
        mX = aLocation.getX();
        mY = aLocation.getY();
        mZ = aLocation.getZ();
        mRealm = aRealm;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public double getZ() {
        return mZ;
    }

    public String getPlayerDisplayName() {
        return mPlayer;
    }

    public String getRealm() {
        return mRealm;
    }

    public JSONObject getSerialized() throws JSONException {
        JSONObject result = new JSONObject();

        result.put("x", mX);
        result.put("y", mY);
        result.put("z", mZ);
        result.put("player", mPlayer);
        result.put("realm", mRealm);

        return result;
    }
}
