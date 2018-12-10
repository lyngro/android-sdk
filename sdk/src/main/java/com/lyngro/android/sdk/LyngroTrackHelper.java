package com.lyngro.android.sdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LyngroTrackHelper {
    private final TrackMe mBaseTrackMe;
    public static final String PAGE_VIEW = "pageview";
    public static final String HEART_BEAT = "heartbeat";
    public static final String WIDGET_CLICK = "widget_click";
    public static final String WIDGET_RECOMMEND_APPEARED = "widget_recommend_appeared";

    private LyngroTrackHelper() {
        this(null);
    }

    private LyngroTrackHelper(@Nullable TrackMe baseTrackMe) {
        if (baseTrackMe == null) baseTrackMe = new TrackMe();
        mBaseTrackMe = baseTrackMe;
    }

    public static LyngroTrackHelper track() {
        return new LyngroTrackHelper();
    }

    public static LyngroTrackHelper track(@NonNull TrackMe base) {
        return new LyngroTrackHelper(base);
    }

    static abstract class BaseEvent {

        private final LyngroTrackHelper mBaseBuilder;

        BaseEvent(LyngroTrackHelper baseBuilder) {
            mBaseBuilder = baseBuilder;
        }

        TrackMe getBaseTrackMe() {
            return mBaseBuilder.mBaseTrackMe;
        }

        @Nullable
        public abstract TrackMe build();

        public void with(@NonNull LyngroApplication kryptonzApplication) {
            with(kryptonzApplication.getTracker());
        }

        public void with(@NonNull LyngroTracker tracker) {
            TrackMe trackMe = build();
            if (trackMe != null) tracker.track(trackMe);
        }
    }

    public RecommendWidgetAppearedEvent widgetAppeared(String widgetId) {
        return new RecommendWidgetAppearedEvent(this, widgetId);
    }

    public HeartbeatEvent heartbeat() {
        return new HeartbeatEvent(this);
    }

    public WidgetClickEvent Click(String widgetId, int index, double rank) {
        return new WidgetClickEvent(this, widgetId, index, rank);
    }

    public PageViewEvent pageView() {
        return new PageViewEvent(this);
    }

    public static class RecommendWidgetAppearedEvent extends BaseEvent {
        private String mAction;
        private String mParentId;
        private String mPostId;
        private String mWidgetId;

        RecommendWidgetAppearedEvent(LyngroTrackHelper baseBuilder, String widgetId) {
            super(baseBuilder);
            mAction= WIDGET_RECOMMEND_APPEARED;
            mWidgetId = widgetId;
        }

        public RecommendWidgetAppearedEvent parentId(String parentId) {
            mParentId = parentId;
            return this;
        }

        public RecommendWidgetAppearedEvent postId(String postId) {
            mPostId = postId;
            return this;
        }

        private String getExtraString(String widgetId) {
            if(widgetId == null || widgetId.isEmpty())
                return "";

            JSONStringer js = new JSONStringer();
            try {
                js.object()
                        .key("widgetid")
                        .value(widgetId)
                        .endObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return js.toString();
        }

        @Nullable
        @Override
        public TrackMe build() {
            return new TrackMe(getBaseTrackMe())
                    .set(QueryParams.TRACKING_ACTION, mAction)
                    .set(QueryParams.TRACKING_PARENT_ID, mParentId)
                    .set(QueryParams.TRACKING_POST_ID, mPostId)
                    .set(QueryParams.TRACKING_EXTRA, getExtraString(mWidgetId));
        }
    }

    public static class HeartbeatEvent extends BaseEvent {
        private String mAction;
        private String mParentId;
        private String mPostId;
        private String mDuration;

        HeartbeatEvent(LyngroTrackHelper baseBuilder) {
            super(baseBuilder);
            mAction= HEART_BEAT;
        }

        public HeartbeatEvent parentId(String parentId) {
            mParentId = parentId;
            return this;
        }

        public HeartbeatEvent postId(String postId) {
            mPostId = postId;
            return this;
        }

        public HeartbeatEvent duration(String duration) {
            mDuration = duration;
            return this;
        }

        @Nullable
        @Override
        public TrackMe build() {
            return new TrackMe(getBaseTrackMe())
                    .set(QueryParams.TRACKING_ACTION, mAction)
                    .set(QueryParams.TRACKING_PARENT_ID, mParentId)
                    .set(QueryParams.TRACKING_POST_ID, mPostId)
                    .set(QueryParams.TRACKING_DURATION, mDuration);
        }
    }

    public static class WidgetClickEvent extends BaseEvent {
        private String mAction;
        private String mParentId;
        private String mPostId;
        private String mWidgetId;
        private int mIndex;
        private double mRank;

        WidgetClickEvent(LyngroTrackHelper baseBuilder, String widgetId, int index, double rank) {
            super(baseBuilder);
            mAction = WIDGET_CLICK;
            mWidgetId = widgetId;
            mIndex = index;
            mRank = rank;
        }

        public WidgetClickEvent parentId(String parentId) {
            mParentId = parentId;
            return this;
        }

        public WidgetClickEvent postId(String postId) {
            mPostId = postId;
            return this;
        }

        private String getExtraString(String widgetId, int index, double rank) {
            if(widgetId == null || widgetId.isEmpty() || index < 0)
                return "";

            JSONStringer js = new JSONStringer();
            try {
                js.object()
                        .key("widgetid")
                        .value(widgetId)
                        .key("index")
                        .value(index)
                        .key("rank")
                        .value(rank)
                        .endObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return js.toString();
        }

        @Nullable
        @Override
        public TrackMe build() {
            return new TrackMe(getBaseTrackMe())
                    .set(QueryParams.TRACKING_ACTION, mAction)
                    .set(QueryParams.TRACKING_PARENT_ID, mParentId)
                    .set(QueryParams.TRACKING_POST_ID, mPostId)
                    .set(QueryParams.TRACKING_EXTRA, getExtraString(mWidgetId, mIndex, mRank));
        }
    }

    public static class PageViewEvent extends BaseEvent {
        private String mAction;
        private String mParentId;

        private String mUserName;
        private String mUserDisplayName;
        private String mUserEmail;
        private Date mUserBirthDate;
        private String mUserImage;

        private String mAuthorName;
        private String mActivityName;
        private String mPostId;
        private String mType;
        private String mTitle;
        private String mCategory;
        private String mSection;
        private String mSubSection;
        private String mUrl;
        private String mThumbnail;
        private Date mPublishDate;
        private String mTags;
        private String mWidgetId;
        private String mTargeting;
        //private String mDuration;

        PageViewEvent(LyngroTrackHelper baseBuilder) {
            super(baseBuilder);
            mAction = PAGE_VIEW;
        }

        public PageViewEvent userName(String userName) {
            mUserName = userName;
            return this;
        }

        public PageViewEvent userDisplayName(String userDisplayName) {
            mUserDisplayName = userDisplayName;
            return this;
        }

        public PageViewEvent userEmail(String userEmail) {
            mUserEmail = userEmail;
            return this;
        }

        public PageViewEvent userBirthDate(Date userBirthDate) {
            mUserBirthDate = userBirthDate;
            return this;
        }

        public PageViewEvent userImage(String userImage) {
            mUserImage = userImage;
            return this;
        }

        public PageViewEvent authorName(String authorName) {
            mAuthorName = authorName;
            return this;
        }

        public PageViewEvent activityName(String activityName) {
            mActivityName = activityName;
            return this;
        }
        public PageViewEvent parentId(String parentId) {
            mParentId = parentId;
            return this;
        }

        public PageViewEvent postId(String postId) {
            mPostId = postId;
            return this;
        }

        public PageViewEvent type(String type) {
            mType = type;
            return this;
        }

        public PageViewEvent title(String title) {
            mTitle = title;
            return this;
        }

        public PageViewEvent category(String category) {
            mCategory = category;
            return this;
        }

        public PageViewEvent section(String section) {
            mSection = section;
            return this;
        }

        public PageViewEvent subSection(String subSection) {
            mSubSection = subSection;
            return this;
        }

        public PageViewEvent url(String url) {
            mUrl = url;
            return this;
        }

        public PageViewEvent thumbnail(String thumbnail) {
            mThumbnail = thumbnail;
            return this;
        }

        public PageViewEvent publishDate(Date publishDate) {
            mPublishDate = publishDate;
            return this;
        }

        public PageViewEvent tags(String tags) {
            mTags = tags;
            return this;
        }

        public PageViewEvent widgetId(String widgetId) {
            this.mWidgetId = widgetId;
            return this;
        }

        public PageViewEvent targeting(String targeting) {
            this.mTargeting = targeting;
            return this;
        }

//        public PageViewEvent duration(String duration) {
//            mDuration = duration;
//            return this;
//        }

        private String getExtraString(String widgetId) {
            if(widgetId == null || widgetId.isEmpty())
                return "";
            JSONStringer js = new JSONStringer();
            try {
                js.object()
                        .key("widgetid")
                        .value(widgetId)
                        .endObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return js.toString();
        }

        @Nullable
        @Override
        public TrackMe build() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String strUserBirthDate = "";
            String strPublishDate = "";

            if(mUserBirthDate != null)
            {
                strUserBirthDate = dateFormat.format(mUserBirthDate);
            }

            if(mPublishDate != null)
            {
                strPublishDate = dateFormat.format(mPublishDate);
            }

//            try {
//                mUrl = URLEncoder.encode(mUrl, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            return new TrackMe(getBaseTrackMe())
                    .set(QueryParams.TRACKING_ACTION, mAction)
                    .set(QueryParams.TRACKING_PARENT_ID, mParentId)

                    .set(QueryParams.TRACKING_USER_NAME, mUserName)
                    .set(QueryParams.TRACKING_USER_DISPLAY_NAME, mUserDisplayName)
                    .set(QueryParams.TRACKING_USER_EMAIL, mUserEmail)
                    .set(QueryParams.TRACKING_USER_BIRTH_DATE, strUserBirthDate)
                    .set(QueryParams.TRACKING_USER_IMAGE, mUserImage)

                    .set(QueryParams.TRACKING_POST_AUTHOR_NAME, mAuthorName)
                    .set(QueryParams.TRACKING_POST_ACTIVITY_NAME, mActivityName)
                    .set(QueryParams.TRACKING_POST_ID, mPostId)
                    .set(QueryParams.TRACKING_POST_TYPE, mType)
                    .set(QueryParams.TRACKING_POST_TITLE, mTitle)
                    .set(QueryParams.TRACKING_CATEGORY, mCategory)
                    .set(QueryParams.TRACKING_SECTION, mSection)
                    .set(QueryParams.TRACKING_SUBSECTION, mSubSection)
                    .set(QueryParams.TRACKING_POST_URL, mUrl)
                    .set(QueryParams.TRACKING_POST_THUMBNAIL, mThumbnail)
                    .set(QueryParams.TRACKING_POST_PUBLISH_DATE, strPublishDate)
                    .set(QueryParams.TRACKING_POST_TAGS, mTags)
                    .set(QueryParams.TRACKING_EXTRA, getExtraString(mWidgetId))
                    .set(QueryParams.TRACKING_TARGETING, mTargeting);
                    //.set(QueryParams.TRACKING_DURATION, mDuration);
        }


    }
}
