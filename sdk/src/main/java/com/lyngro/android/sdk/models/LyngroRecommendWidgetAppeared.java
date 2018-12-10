package com.lyngro.android.sdk.models;

import com.lyngro.android.sdk.LyngroTrackHelper;
import com.lyngro.android.sdk.LyngroTracker;

/**
 * Created by j.naim on 02-Aug-16.
 */
public class LyngroRecommendWidgetAppeared {
    private String parentId = "";
    private String postId = "";
    private String widgetId = "";

    public LyngroRecommendWidgetAppeared(LyngroPageView pageView, String widgetId) {
        this.parentId = pageView.parentId;
        this.postId = pageView.postId;
        this.widgetId = widgetId;
    }

    public static void sendRcommendWidgetAppeared(LyngroRecommendWidgetAppeared recommendWidgetAppeared, LyngroTracker tracker) {
        if (recommendWidgetAppeared != null && tracker != null) {
            LyngroTrackHelper.track().widgetAppeared(recommendWidgetAppeared.widgetId)
                    .parentId(recommendWidgetAppeared.parentId)
                    .postId(recommendWidgetAppeared.postId)
                    .with(tracker);
        }

    }
}
