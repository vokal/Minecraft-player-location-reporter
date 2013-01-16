package com.vokal.locator.point;

import org.json.*;

public abstract class Point {
    private long mTimestamp;

    public Point() {
        mTimestamp = System.currentTimeMillis() / 1000;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public abstract JSONObject getSerialized() throws JSONException;
}
