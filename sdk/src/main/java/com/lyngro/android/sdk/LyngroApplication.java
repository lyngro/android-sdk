/*
 * Android SDK for Lyngro
 *
 */

package com.lyngro.android.sdk;

import android.app.Application;
import android.os.Build;

import java.net.MalformedURLException;

public abstract class LyngroApplication extends Application {
    private LyngroTracker mLyngroTracker;

    public Lyngro getLyngro() {
        return Lyngro.getInstance(this);
    }

    /**
     * Gives you an all purpose thread-safe persisted LyngroTracker object.
     *
     * @return a shared LyngroTracker
     */
    public synchronized LyngroTracker getTracker() {
        if (mLyngroTracker == null) {
            try {
                mLyngroTracker = getLyngro().newTracker( getAppKey(), getAppDomain(),"");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("LyngroTracker URL was malformed.");
            }
        }
        return mLyngroTracker;
    }

    /**
     * The URL of your remote Lyngro server.
     */
    public abstract String getTrackerUrl();


    /**
     * The URL of your remote Lyngro widget server.
     */
    public abstract String getWidgetUrl();


    /**
     * The appKey you specified for this application in Lyngro.
     */
    public abstract String getAppKey();

    /**
     * The appDomain you specified for this application in Lyngro.
     */
    public abstract String getAppDomain();


    @Override
    public void onLowMemory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH && mLyngroTracker != null) {
            mLyngroTracker.dispatch();
        }
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        if ((level == TRIM_MEMORY_UI_HIDDEN || level == TRIM_MEMORY_COMPLETE) && mLyngroTracker != null) {
            mLyngroTracker.dispatch();
        }
        super.onTrimMemory(level);
    }

}
