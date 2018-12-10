package com.lyngro.android.demo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lyngro.android.sdk.LyngroTracker;
import com.lyngro.android.sdk.models.LyngroPageView;
import com.lyngro.android.sdk.models.LyngroWidgetClick;
import com.lyngro.android.sdk.widgets.Widget;
import com.lyngro.android.sdk.widgets.WidgetPost;

/**
 * Created by maf on 2/8/2016.
 */
public class RecommendationLayout extends LinearLayout {
    public com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    public Context context;

    private LyngroPageView mPageView;
    private LyngroTracker mTracker;
    private int postsCount;

    public RecommendationLayout(Context context, LyngroTracker LyngroTracker, int postsCount) {
        super(context);
        this.context = context;
        imageLoader = SharedClass.initializeImageLoader(context);

        this.mTracker = LyngroTracker;
        this.mPageView = mTracker.lyngroPageView;
        this.postsCount = postsCount;

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0,5,0,0);
        this.setGravity(Gravity.TOP);
        this.setLayoutParams(params);


        ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);

        progress.setPadding(5,35,5,35);
        this.addView(progress);

        RecommendationAsync task = new RecommendationAsync(mTracker, this);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mPageView);
        else
            task.execute(mPageView);
    }

    public void fillRecommendations(final LyngroPageView pageView, final Widget widget) {

        try {
            this.removeAllViews();

            if(widget == null) return;
            if(widget.getPosts() == null || widget.getPosts().size() == 0) return;

            for(int i = 0; i < postsCount; i++) {
                final WidgetPost post = widget.getPosts().get(i);
                LayoutInflater layoutInflater = (LayoutInflater)  this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.item_recommendations_list, null);
                TextView title = (TextView) view.findViewById(R.id.RecommendTitle);
                ImageView thumb = (ImageView) view.findViewById(R.id.RecommendImage);
                title.setText(post.getTitle());
                imageLoader.displayImage(post.getThumbnail() + "=w150-h88-c", thumb);
                System.out.print(thumb);
                this.addView(view);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SampleActivity.class);
                        intent.putExtra("post",  post);
                        intent.putExtra("targeting", "recommend");
                        intent.putExtra("widgetid", widget.getWidgetId());

                        LyngroWidgetClick widgetClick = new LyngroWidgetClick(pageView, widget.getWidgetId(), post.getIndex(), post.getRank());
                        LyngroWidgetClick.sendWidgetClick(widgetClick, mTracker);

                        context.startActivity(intent);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
