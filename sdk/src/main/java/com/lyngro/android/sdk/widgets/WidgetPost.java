package com.lyngro.android.sdk.widgets;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by j.naim on 27-Apr-17.
 */

public class WidgetPost implements Serializable {
/*
    only fields needed to show widget
 */

    private String mPostId;
    private String mTitle;
    private String mThumbnail;
    private String mUrl;
    private int mIndex;
    private double mRank;

    public WidgetPost(String postId, String postTitle, String url, String thumbnail, int index, double rank) {
        this.mPostId = postId;
        this.mTitle = postTitle;
        this.mUrl = url;
        if (!TextUtils.isEmpty(thumbnail))
            this.mThumbnail = thumbnail.replace("http:", "https:");
        this.mIndex = index;
        this.mRank = rank;
    }

    public String getPostId() {
        return mPostId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public int getIndex() {
        return mIndex;
    }

    public double getRank() {
        return mRank;
    }

}
