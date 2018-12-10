package com.lyngro.android.demo;

import android.os.AsyncTask;

import com.lyngro.android.sdk.LyngroTracker;
import com.lyngro.android.sdk.models.LyngroPageView;
import com.lyngro.android.sdk.models.LyngroRecommendWidgetAppeared;
import com.lyngro.android.sdk.widgets.Widget;

/**
 * Created by j.naim on 27-Apr-17.
 */

public class RecommendationAsync extends AsyncTask<LyngroPageView, Void, Widget> {
    LyngroTracker mTracker;
    RecommendationLayout mLayout;
    LyngroPageView mPageView;

    public RecommendationAsync(LyngroTracker tracker, RecommendationLayout layout) {
        mTracker = tracker;
        mLayout = layout;
    }

    protected Widget doInBackground(LyngroPageView... pageViews) {
        Widget widget;
        mPageView = pageViews[0];
        try{
            widget = mTracker.getRecommendationsWidget(mPageView.postId,mPageView.postUrl);
            LyngroRecommendWidgetAppeared widgetAppeared = new LyngroRecommendWidgetAppeared(mPageView, widget.getWidgetId());
            LyngroRecommendWidgetAppeared.sendRcommendWidgetAppeared(widgetAppeared, mTracker);
        }catch (Exception e){
            return null;
        }
        return widget;
    }

    protected void onPostExecute(Widget widget) {
        mLayout.fillRecommendations(mPageView, widget);
    }
}
