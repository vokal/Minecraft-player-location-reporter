package com.vokal.locator.point;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;

import com.vokal.locator.point.Point;

public class PointList<T extends Point> {
    private ArrayList<T> mPoints;
    private int mTimeout;

    public PointList() {
        mPoints = new ArrayList<T>();
        mTimeout = 900;
    }

    public PointList(int aTimeout) {
        mPoints = new ArrayList<T>();
        mTimeout = aTimeout * 60;
    }

    public void add(T aPoint) {
        mPoints.add(aPoint);
    }

    /*
     * Returns any existing deathpoints and expires any
     * old ones
     *
     */
    public synchronized JSONArray getSerializedPoints() throws JSONException {
        JSONArray result = new JSONArray();
        long timestamp = System.currentTimeMillis() / 1000;

        Iterator<T> iter = mPoints.iterator();
        while(iter.hasNext()) {
            T p = iter.next();

            if (timestamp - p.getTimestamp() >= mTimeout) {
                iter.remove();
            }

            result.put(p.getSerialized());
        }

        return result;
    }
}
