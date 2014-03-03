package com.ivangusev.rmcc;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by ivan on 21.02.14.
 */
public class RMCCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Configure ImageLoader
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }
}
