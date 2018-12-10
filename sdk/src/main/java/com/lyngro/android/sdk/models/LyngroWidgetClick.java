package com.lyngro.android.sdk.models;

import com.lyngro.android.sdk.LyngroTrackHelper;
import com.lyngro.android.sdk.LyngroTracker;

/**
 * Created by j.naim on 02-Aug-16.
 */
public class LyngroWidgetClick {

    private String parentId = "";
    private String postId = "";
    private String widgetId = "";
    private int clickedIndex = -1;
    private double clickedRank = -1;

    public LyngroWidgetClick(LyngroPageView pageView, String widgetId, int clickedIndex, double clickedRank) {
        this.parentId = pageView.parentId;
        this.postId = pageView.postId;
        this.widgetId = widgetId;
        this.clickedIndex = clickedIndex;
        this.clickedRank = clickedRank;
    }

    public static void sendWidgetClick(LyngroWidgetClick widgetClick, LyngroTracker tracker)
    {
        if(widgetClick != null && tracker != null)
        {
            LyngroTrackHelper.track().Click(widgetClick.widgetId, widgetClick.clickedIndex, widgetClick.clickedRank)
                    .parentId(widgetClick.parentId)
                    .postId(widgetClick.postId)
                    .with(tracker);
        }
    }
}
