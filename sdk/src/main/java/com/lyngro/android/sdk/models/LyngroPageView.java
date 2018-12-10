package com.lyngro.android.sdk.models;

import com.lyngro.android.sdk.LyngroTrackHelper;
import com.lyngro.android.sdk.LyngroTracker;

import java.util.Date;
import java.util.UUID;

/**
 * Created by j.naim on 02-Aug-16.
 */
public class LyngroPageView {
    public String parentId = "";
    public String targeting = "";
    public String postActivity = "";
    public String userName = "";
    public String userDisplayName = "";
    public String userEmail = "";
    public Date userBirthDate;
    public String userImage = "";
    public String postAuthor = "";
    public String postId = "";
    public String postType = "";
    public String postTitle = "";
    public String postCategory = "";
    public String postSection = "";
    public String postSubSection = "";
    public String postUrl = "";
    public String postThumbnail = "";
    public Date postPublishDate ;
    public String postTags = "";
    public String widgetId = "";
    //private String duration = "";


    public LyngroPageView() {
        this.parentId = UUID.randomUUID().toString();
    }

}
