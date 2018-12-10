/*
 * Android SDK for Lyngro
 *
 */

package com.lyngro.android.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.MalformedURLException;


public class Lyngro {
    public static final String LOGGER_PREFIX = "LYNGRO:";
    public static final String PREFERENCE_FILE_NAME = "com.lyngro.sdk";
    public static final String PREFERENCE_KEY_OPTOUT = "lyngro.optout";
    private final Context mContext;
    private boolean mOptOut = false;
    private boolean mDryRun = false;

    private static Lyngro sInstance;
    private final SharedPreferences mSharedPreferences;

    public static synchronized Lyngro getInstance(Context context) {
        if (sInstance == null)
            sInstance = new Lyngro(context);
        return sInstance;
    }

    private Lyngro(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = getContext().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        mOptOut = getSharedPreferences().getBoolean(PREFERENCE_KEY_OPTOUT, false);
    }

    protected Context getContext() {
        return mContext;
    }

//    /**
//     * @param trackerUrl (required) Tracking HTTP API endpoint, for example, http://your-kryptonz-domain.tld/kryptonz.php
//     * @param appId     (required) id of site
//     * @param authToken  (optional) could be null or valid auth token.
//     * @return LyngroTracker object
//     * @throws MalformedURLException
//     * @deprecated Use {@link #newTracker(String, int)} as there are security concerns over the authToken.
//     */
//    @Deprecated
//    public synchronized LyngroTracker newTracker(@NonNull String baseUrl, @NonNull String trackerUrl, @NonNull String widgetUrl, int appId, String authToken) throws MalformedURLException {
//        return new LyngroTracker(trackerUrl, widgetUrl, appId, authToken, this);
//    }

    /**
     * @return LyngroTracker object
     * @throws MalformedURLException
     */
    public synchronized LyngroTracker newTracker(String appKey, String appDomain, String widgetid) throws MalformedURLException {

        return new LyngroTracker(mContext.getResources().getString(R.string.Lyngro_tracker_url),
                mContext.getResources().getString(R.string.Lyngro_widget_url),
                appKey , appDomain,widgetid, null, this);
    }

    /**
     * Use this to disable Lyngro, e.g. if the user opted out of tracking.
     * Lyngro will persist the choice and remain disable on next instance creation.</p>
     * The choice is stored in {@link #PREFERENCE_FILE_NAME} under the key {@link #PREFERENCE_KEY_OPTOUT}.
     *
     * @param optOut true to disable reporting
     */
    public void setOptOut(boolean optOut) {
        mOptOut = optOut;
        getSharedPreferences().edit().putBoolean(PREFERENCE_KEY_OPTOUT, optOut).apply();
    }

    /**
     * @return true if Lyngro is currently disabled
     */
    public boolean isOptOut() {
        return mOptOut;
    }

    public boolean isDryRun() {
        return mDryRun;
    }

    /**
     * The dryRun flag set to true prevents any data from being sent to Lyngro.
     * The dryRun flag should be set whenever you are testing or debugging an implementation and do not want
     * test data to appear in your Lyngro reports. To set the dry run flag, use:
     *
     * @param dryRun true if you don't want to send any data to Lyngro
     */
    public void setDryRun(boolean dryRun) {
        mDryRun = dryRun;
    }

    public String getApplicationDomain() {
        return getContext().getPackageName();
    }

    /**
     * Returns the shared preferences used by Lyngro that are stored under {@link #PREFERENCE_FILE_NAME}
     *
     * @return Lyngro's SharedPreferences instance
     */
    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
