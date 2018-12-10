/*
 * Android SDK for Lyngro
 *
 */

package com.lyngro.android.sdk;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

import com.lyngro.android.sdk.dispatcher.Dispatcher;
import com.lyngro.android.sdk.models.LyngroHeartbeat;
import com.lyngro.android.sdk.models.LyngroPageView;
import com.lyngro.android.sdk.tools.DeviceHelper;
import com.lyngro.android.sdk.widgets.HTTPDataHandler;
import com.lyngro.android.sdk.widgets.Widget;
import com.lyngro.android.sdk.widgets.WidgetDataHandler;
import com.lyngro.android.timber.Timber;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class LyngroTracker {
    //kryptonz APIs
    private static final String TRACK_MOBILE = "trackmobile";
    protected static final String LOGGER_TAG = Lyngro.LOGGER_PREFIX + "LyngroTracker";
    // Sharedpreference keys for persisted values
    protected static final String PREF_KEY_TRACKER_USERID = "tracker.userid";
    protected static final String PREF_KEY_TRACKER_DEVICEID = "tracker.deviceid";
    protected static final String PREF_KEY_TRACKER_SDK_VERSION = "tracker.sdkversion";

    private final Lyngro mLyngro;


    /**
     * Tracking HTTP API endpoint, for example, http://your-ryptonz-domain.tld/kryptonz.php
     */
    private final URL mApiUrl;
    private final URL mBaseUrl;
    private final URL mWidgetUrl;
    private Widget mWidget;

    /**
     * The ID of the website we're tracking a visit/action for.
     */
    //private final int mAppId;
    private final String mAppKey;
    private  String mWidgetId;
    private final String mAppDomain;
    private final Dispatcher mDispatcher;
    private final TrackMe mDefaultTrackMe = new TrackMe();
    public LyngroPageView lyngroPageView;

    /**
     * Use Lyngro.newTracker() method to create new trackers
     *
     * @param url       (required) Tracking HTTP API endpoint, for example, http://your-kryptonz-domain.tld/kryptonz.php
     *                  //* @param appId    (required) id of site
     * @param appKey    (required) key of site
     * @param appDomain (required) domain of site
     * @param authToken (optional) could be null
     * @param lyngro    lyngro object used to gain access to application params such as name, resolution or lang
     * @throws MalformedURLException
     */
    protected LyngroTracker(@NonNull final String url, @NonNull final String widgetUrl, String appKey, String appDomain,String widgetid, String authToken, @NonNull Lyngro lyngro) throws MalformedURLException {

        String checkUrl = url;
        if (!checkUrl.endsWith("/")) {
            checkUrl += "/";
        }
        mBaseUrl = new URL(checkUrl);

        String checkUrlWidget = widgetUrl;
        if (!checkUrlWidget.endsWith("/")) {
            checkUrlWidget += "/";
        }
        mWidgetUrl = new URL(checkUrlWidget);

        mApiUrl = new URL(checkUrl + TRACK_MOBILE);
        mWidgetId=widgetid;

        mLyngro = lyngro;
        //mAppId = appId;
        mAppKey = appKey;
        mAppDomain = appDomain;

        mDispatcher = new Dispatcher(mLyngro, mApiUrl, authToken);

        String sdkVersion = getSharedPreferences().getString(PREF_KEY_TRACKER_SDK_VERSION, null);
        if (sdkVersion == null) {
            sdkVersion = mLyngro.getContext().getResources().getString(R.string.sdk_version);
            getSharedPreferences().edit().putString(PREF_KEY_TRACKER_SDK_VERSION, sdkVersion).apply();
        }

        String deviceId = getSharedPreferences().getString(PREF_KEY_TRACKER_DEVICEID, null);
        String userId = getSharedPreferences().getString(PREF_KEY_TRACKER_USERID, null);
        if (userId == null) {
            userId = UUID.randomUUID().toString();
            getSharedPreferences().edit().putString(PREF_KEY_TRACKER_USERID, userId).apply();
        }

        mDefaultTrackMe.set(QueryParams.TRACKING_USER_ID, userId);
        mDefaultTrackMe.set(QueryParams.TRACKING_DEVICE_ID, deviceId);
//        mDefaultTrackMe.set(QueryParams.TRACKING_APP_ID, appId);
        mDefaultTrackMe.set(QueryParams.TRACKING_APP_KEY, appKey);
        mDefaultTrackMe.set(QueryParams.TRACKING_APP_DOMAIN, appDomain);
        mDefaultTrackMe.set(QueryParams.TRACKING_USER_AGENT, DeviceHelper.getUserAgent());

        mDefaultTrackMe.set(QueryParams.TRACKING_MANUFACTURER, Build.MANUFACTURER);
        mDefaultTrackMe.set(QueryParams.TRACKING_BRAND, Build.BRAND);
        mDefaultTrackMe.set(QueryParams.TRACKING_DEVICE, Build.DEVICE);
        mDefaultTrackMe.set(QueryParams.TRACKING_ANDROID_SDK_VERSION, String.valueOf(Build.VERSION.SDK_INT));
        mDefaultTrackMe.set(QueryParams.TRACKING_KRYPTONZ_SDK_VERSION, sdkVersion);
    }

    public Lyngro getKryptonz() {
        return mLyngro;
    }

//    protected int getAppId() {
//        return mAppId;
//    }

    protected String getAppKey() {
        return mAppKey;
    }

    protected String getAppDomain() {
        return mAppDomain;
    }

    /**
     * {@link Dispatcher#setConnectionTimeOut(int)}
     */
    public void setDispatchTimeout(int timeout) {
        mDispatcher.setConnectionTimeOut(timeout);
    }

    /**
     * Processes all queued events in background thread
     *
     * @return true if there are any queued events and opt out is inactive
     */
    public boolean dispatch() {
        if (!mLyngro.isOptOut()) {
            mDispatcher.forceDispatch();
            return true;
        }
        return false;
    }

    /**
     * Set the interval to 0 to dispatch events as soon as they are queued.
     * If a negative value is used the dispatch timer will never run, a manual dispatch must be used.
     *
     * @param dispatchInterval in milliseconds
     */
    public LyngroTracker setDispatchInterval(long dispatchInterval) {
        mDispatcher.setDispatchInterval(dispatchInterval);
        return this;
    }



    public LyngroHeartbeatHandler sendPageView(final String postid, final String url) {
        lyngroPageView = new LyngroPageView() {{
            this.postId = postid;
            this.postUrl = url;
        }};
        return sendPageView(lyngroPageView);
    }

    public LyngroHeartbeatHandler sendPageView(LyngroPageView pageView) {

        if (pageView != null) {
            LyngroTrackHelper.track().pageView()
                    .parentId(pageView.parentId)
                    .activityName(pageView.postActivity)
                    .userName(pageView.userName)
                    .userDisplayName(pageView.userDisplayName)
                    .userEmail(pageView.userEmail)
                    .userBirthDate(pageView.userBirthDate)
                    .userImage(pageView.userImage)
                    .authorName(pageView.postAuthor)
                    .postId(pageView.postId)
                    .type(pageView.postType)
                    .title(pageView.postTitle)
                    .category(pageView.postCategory)
                    .section(pageView.postSection)
                    .subSection(pageView.postSubSection)
                    .url(pageView.postUrl)
                    .thumbnail(pageView.postThumbnail)
                    .publishDate(pageView.postPublishDate)
                    .tags(pageView.postTags)
                    .widgetId(pageView.widgetId)
                    .targeting(pageView.targeting)
                    //.duration(pageView.duration)
                    .with(this);
            LyngroHeartbeat heartbeat = new LyngroHeartbeat(pageView);
            return new LyngroHeartbeatHandler(this, heartbeat);
        }
        return null;
    }

    /**
     * @return in milliseconds
     */
    public long getDispatchInterval() {
        return mDispatcher.getDispatchInterval();
    }

    public LyngroTracker setUserId(String userId) {
        mDefaultTrackMe.set(QueryParams.TRACKING_USER_ID, userId);
        getSharedPreferences().edit().putString(PREF_KEY_TRACKER_USERID, userId).apply();
        return this;
    }

    public LyngroTracker setDeviceId(String deviceId) {
        mDefaultTrackMe.set(QueryParams.TRACKING_DEVICE_ID, deviceId);
        getSharedPreferences().edit().putString(PREF_KEY_TRACKER_DEVICEID, deviceId).apply();
        return this;
    }

    /**
     * @return a user-id string, either the one you set or the one Lyngro generated for you.
     */
    public String getUserId() {
        return mDefaultTrackMe.get(QueryParams.TRACKING_USER_ID);
    }

    public String getDeviceId() {
        return mDefaultTrackMe.get(QueryParams.TRACKING_DEVICE_ID);
    }

    /**
     * These parameters are required for all queries.
     */
    private void injectBaseParams(TrackMe trackMe) {
        trackMe.trySet(QueryParams.TRACKING_USER_ID, mDefaultTrackMe.get(QueryParams.TRACKING_USER_ID));
        trackMe.trySet(QueryParams.TRACKING_DEVICE_ID, mDefaultTrackMe.get(QueryParams.TRACKING_DEVICE_ID));
        //trackMe.trySet(QueryParams.TRACKING_APP_ID, mDefaultTrackMe.get(QueryParams.TRACKING_APP_ID));
        trackMe.trySet(QueryParams.TRACKING_APP_KEY, mDefaultTrackMe.get(QueryParams.TRACKING_APP_KEY));
        trackMe.trySet(QueryParams.TRACKING_APP_DOMAIN, mDefaultTrackMe.get(QueryParams.TRACKING_APP_DOMAIN));
        trackMe.trySet(QueryParams.TRACKING_USER_AGENT, mDefaultTrackMe.get(QueryParams.TRACKING_USER_AGENT));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        trackMe.trySet(QueryParams.TRACKING_DATE_TIME, calendar.getTimeInMillis());

        trackMe.trySet(QueryParams.TRACKING_MANUFACTURER, mDefaultTrackMe.get(QueryParams.TRACKING_MANUFACTURER));
        trackMe.trySet(QueryParams.TRACKING_BRAND, mDefaultTrackMe.get(QueryParams.TRACKING_BRAND));
        trackMe.trySet(QueryParams.TRACKING_DEVICE, mDefaultTrackMe.get(QueryParams.TRACKING_DEVICE));
        trackMe.trySet(QueryParams.TRACKING_ANDROID_SDK_VERSION, mDefaultTrackMe.get(QueryParams.TRACKING_ANDROID_SDK_VERSION));
        trackMe.trySet(QueryParams.TRACKING_KRYPTONZ_SDK_VERSION, mDefaultTrackMe.get(QueryParams.TRACKING_KRYPTONZ_SDK_VERSION));
    }

    private static String fixUrl(String url, String baseUrl) {
        if (url == null) url = baseUrl + "/";

        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://")) {
            url = baseUrl + (url.startsWith("/") ? "" : "/") + url;
        }
        return url;
    }

    public LyngroTracker track(TrackMe trackMe) {
        injectBaseParams(trackMe);
        String event = Dispatcher.urlEncodeUTF8(trackMe.toMap());
        mDispatcher.submit(event);
        Timber.tag(LOGGER_TAG).d("URL added to the queue: %s", event);
        return this;
    }

    public SharedPreferences getSharedPreferences() {
        return mLyngro.getSharedPreferences();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LyngroTracker tracker = (LyngroTracker) o;
        //return mAppId == tracker.mAppId && mApiUrl.equals(tracker.mApiUrl);
        return mAppKey.equals(tracker.mAppKey) && mAppDomain.equals(tracker.mAppDomain) && mApiUrl.equals(tracker.mApiUrl);
    }

    @Override
    public int hashCode() {
//        int result = mAppId;
//        result = 31 * result + mApiUrl.hashCode();
        int result = mAppKey.hashCode() + mAppDomain.hashCode() + mApiUrl.hashCode();
        return result;
    }

    public Widget getRecommendationsWidget(String postid,String posturl) {
        mWidget = new WidgetDataHandler().getWidget(mWidgetUrl + "widget?appkey=" + mAppKey + "&appdomain=" + mAppDomain + "&userid=" + getDeviceId() + "&widgetid=" + mWidgetId+  "&currentpostid=" + postid +  "&currentposturl=" + posturl);
        return mWidget;
    }

    public void setUserToken(final String token) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", getUserId());
            jsonObject.put("token", token);
            jsonObject.put("appkey", getAppKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Thread t = new Thread() {
            public void run() {
                HTTPDataHandler httpDataHandler = new HTTPDataHandler();
                httpDataHandler.SendHTTPData(mBaseUrl + "usertoken", jsonObject);
            }
        };
        t.start();
    }
}