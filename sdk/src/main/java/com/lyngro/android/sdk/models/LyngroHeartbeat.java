package com.lyngro.android.sdk.models;

import com.lyngro.android.sdk.LyngroTrackHelper;
import com.lyngro.android.sdk.LyngroTracker;

/**
 * Created by j.naim on 02-Aug-16.
 */
public class LyngroHeartbeat {

    private String parentId = "";
    private String postId = "";

    public int duration;

    public LyngroHeartbeat(LyngroPageView pageView) {
        this.parentId = pageView.parentId;
        this.postId = pageView.postId;
    }

    public static void sendHearbeat(LyngroHeartbeat heartbeat, LyngroTracker tracker)
    {
        if(heartbeat != null && tracker != null)
        {
            LyngroTrackHelper.track().heartbeat()
                    .parentId(heartbeat.parentId)
                    .postId(heartbeat.postId)
                    .duration(String.valueOf(heartbeat.duration))
                    .with(tracker);
        }

    }
}
