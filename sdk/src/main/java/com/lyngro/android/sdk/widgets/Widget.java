package com.lyngro.android.sdk.widgets;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by j.naim on 10-Jun-16.
 */
public class Widget implements Serializable {
    private String mWidgetId;
    private String mWidgetType;
    private ArrayList<WidgetPost> mPosts;

    public Widget()
    {
        mPosts = new ArrayList<>();
    }

    public String getWidgetId() {
        return mWidgetId;
    }

    public String getWidgetType() {
        return mWidgetType;
    }

    public ArrayList<WidgetPost> getPosts() {
        return mPosts;
    }

    public void setWidgetId(String widgetId) {
        this.mWidgetId = widgetId;
    }

    public void setWidgetType(String widgetType) {
        this.mWidgetType = widgetType;
    }

    public void setPosts(ArrayList<WidgetPost> mPosts) {
        this.mPosts = mPosts;
    }


}
