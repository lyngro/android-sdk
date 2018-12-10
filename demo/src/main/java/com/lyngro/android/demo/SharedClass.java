package com.lyngro.android.demo;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.lyngro.android.sdk.Lyngro;
import com.lyngro.android.sdk.LyngroTracker;
import com.lyngro.android.timber.Timber;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.MalformedURLException;

/**
 * Created by j.nasrallah on 4/5/2017.
 */

public class SharedClass extends Application {

    private LyngroTracker mLyngroTracker;

    public synchronized LyngroTracker getTracker() {
        if (mLyngroTracker == null) {
            try {

                String LyngroAppKey = getResources().getString(R.string.Lyngro_app_key);
                String LyngroAppDomain = getResources().getString(R.string.Lyngro_app_domain);
                String LyngroWidget = getResources().getString(R.string.Lyngro_widgetid);


                Lyngro LyngroInstance = Lyngro.getInstance(this);
                //LyngroInstance.setDryRun(true);

                mLyngroTracker = LyngroInstance.newTracker( LyngroAppKey, LyngroAppDomain,LyngroWidget);
                mLyngroTracker.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                mLyngroTracker.setDispatchInterval(15);
                Timber.plant(new Timber.DebugTree());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("LyngroTracker URL was malformed.");
            }
        }
        // Print debug output when working on an app.
        Timber.plant(new Timber.DebugTree());
        return mLyngroTracker;
    }

    public static ImageLoader initializeImageLoader(Context c){
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()){
            //creating a configuration for imageloader
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    //.showImageOnLoading(R.drawable.ic_placeholder) // resource or drawable
                   // .showImageForEmptyUri(R.drawable.ic_placeholder) // resource or drawable
                    //.showImageOnFail(R.drawable.ic_placeholder) // resource or drawable
                    .build();

            //set the configuration for imageloader
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c)
                    .defaultDisplayImageOptions(options)
                    .build();
            imageLoader.init(config);
        }
        return imageLoader;
    }
}
