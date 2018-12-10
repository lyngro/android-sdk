/*
 * Android SDK for Lyngro
 *
 */

package com.lyngro.android.sdk;
/**
 * Query parameters supported by the tracking HTTP API.
 */
public enum QueryParams {

    //Device
    TRACKING_DEVICE_ID("tdi"),
    TRACKING_APP_ID("tai"),
    TRACKING_APP_KEY("tapk"),
    TRACKING_APP_DOMAIN("tapd"),
    TRACKING_USER_AGENT("tua"),
    TRACKING_DATE_TIME("tda"),


    TRACKING_MANUFACTURER("tmn"),
    TRACKING_BRAND("tbr"),
    TRACKING_DEVICE("tdv"),
    TRACKING_ANDROID_SDK_VERSION("tsa"),
    TRACKING_KRYPTONZ_SDK_VERSION("tsk"),

    //User
    TRACKING_USER_ID("tui"),
    TRACKING_USER_NAME("tun"),//*
    TRACKING_USER_DISPLAY_NAME("tud"),//*
    TRACKING_USER_EMAIL("tue"),//*
    TRACKING_USER_BIRTH_DATE("tub"),//*
    TRACKING_USER_IMAGE("tum"),//*

    //Post
    TRACKING_POST_AUTHOR_NAME("tpa"),
    TRACKING_POST_ACTIVITY_NAME("tpc"),
    TRACKING_POST_ID("tpi"),
    TRACKING_POST_TYPE("tpy"),
    TRACKING_POST_TITLE("tpt"),
    TRACKING_CATEGORY("tca"),
    TRACKING_SECTION("tsc"),
    TRACKING_SUBSECTION("tss"),
    TRACKING_POST_URL("tpu"),
    TRACKING_POST_THUMBNAIL("tpb"),
    TRACKING_POST_PUBLISH_DATE("tpp"),
    TRACKING_POST_TAGS("tpg"),

    //Behavior
    TRACKING_ACTION("tac"),
    TRACKING_PARENT_ID("tpd"),
    TRACKING_EXTRA("tex"),
    TRACKING_TARGETING("ttg"),
    TRACKING_DURATION("tdu");

    private final String value;

    QueryParams(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
