/*
 * Android SDK for Lyngro
 *
 */

package com.lyngro.android.sdk;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * This objects represents one query to Lyngro.
 * For each event send to Lyngro a TrackMe gets created, either explicitly by you or implicitly by the LyngroTracker.
 */
public class TrackMe {
    private static final int DEFAULT_QUERY_CAPACITY = 14;
    private final HashMap<String, String> mQueryParams = new HashMap<>(DEFAULT_QUERY_CAPACITY);

    public TrackMe() {
    }

    public TrackMe(TrackMe trackMe) {
        mQueryParams.putAll(trackMe.mQueryParams);
    }

    protected synchronized TrackMe set(@NonNull String key, String value) {
        if (value == null)
            mQueryParams.remove(key);
        else if (value.length() > 0)
            mQueryParams.put(key, value);
        return this;
    }

    /**
     * You can set any additional Tracking API Parameters within the SDK.
     * This includes for example the local time (parameters h, m and s).
     * <pre>
     * set(QueryParams.HOURS, "10");
     * set(QueryParams.MINUTES, "45");
     * set(QueryParams.SECONDS, "30");
     * </pre>
     *
     * @param key   query params name
     * @param value value
     * @return tracker instance
     */
    public synchronized TrackMe set(@NonNull QueryParams key, String value) {
        set(key.toString(), value);
        return this;
    }

    public synchronized TrackMe set(@NonNull QueryParams key, int value) {
        set(key, Integer.toString(value));
        return this;
    }

    public synchronized TrackMe set(@NonNull QueryParams key, float value) {
        set(key, Float.toString(value));
        return this;
    }

    public synchronized TrackMe set(@NonNull QueryParams key, long value) {
        set(key, Long.toString(value));
        return this;
    }

    public synchronized boolean has(@NonNull QueryParams queryParams) {
        return mQueryParams.containsKey(queryParams.toString());
    }

    /**
     * Only sets the value if it doesn't exist.
     *
     * @param key   type
     * @param value value
     * @return this (for chaining)
     */
    public synchronized TrackMe trySet(@NonNull QueryParams key, int value) {
        return trySet(key, String.valueOf(value));
    }

    /**
     * Only sets the value if it doesn't exist.
     *
     * @param key   type
     * @param value value
     * @return this (for chaining)
     */
    public synchronized TrackMe trySet(@NonNull QueryParams key, float value) {
        return trySet(key, String.valueOf(value));
    }

    public synchronized TrackMe trySet(@NonNull QueryParams key, long value) {
        return trySet(key, String.valueOf(value));
    }

    /**
     * Only sets the value if it doesn't exist.
     *
     * @param key   type
     * @param value value
     * @return this (for chaining)
     */
    public synchronized TrackMe trySet(@NonNull QueryParams key, String value) {
        if (!has(key))
            set(key, value);
        return this;
    }

    /**
     * The tracker calls this to build the final query to be sent via HTTP
     *
     * @return the parameter map, but without the base URL
     */
    public synchronized Map<String, String> toMap() {
        return new HashMap<>(mQueryParams);
    }

    public synchronized String get(@NonNull QueryParams queryParams) {
        return mQueryParams.get(queryParams.toString());
    }

}
