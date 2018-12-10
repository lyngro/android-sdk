package com.lyngro.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyngro.android.sdk.LyngroHeartbeatHandler;
import com.lyngro.android.sdk.LyngroTracker;
import com.lyngro.android.sdk.models.LyngroHeartbeat;
import com.lyngro.android.sdk.models.LyngroPageView;
import com.lyngro.android.sdk.widgets.WidgetPost;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SampleActivity extends AppCompatActivity {
    private LyngroTracker tracker;
    RecommendationLayout recommendations = null;
    public ImageLoader imageLoader;
    LyngroHeartbeatHandler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        imageLoader = SharedClass.initializeImageLoader(this);

        tracker = ((SharedClass) getApplication()).getTracker();

        final String targetingextra = this.getIntent().getStringExtra("targeting");


        handler = tracker.sendPageView("18", "http://almayadeen.net");

        recommendations = new RecommendationLayout(this, tracker, getResources().getInteger(R.integer.Lyngro_widget_items_count));
        ((LinearLayout) findViewById(R.id.action_container)).addView(recommendations);
        ImageView postImage = (ImageView) findViewById(R.id.PostImage);
        TextView postTitle = (TextView) findViewById(R.id.PostTitle);


        try {
            WidgetPost post = (WidgetPost) this.getIntent().getSerializableExtra("post");
            imageLoader.displayImage(post.getThumbnail().replace("?height=500", "?width=500"), postImage);
            postTitle.setText(post.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.pause();
    }


}
