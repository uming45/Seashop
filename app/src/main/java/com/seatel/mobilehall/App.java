package com.seatel.mobilehall;

import android.app.Application;
import android.content.Context;

import com.seatel.im.ImModule;

/**
 * Created by eldorado on 17-4-27.
 */
public class App extends Application {
    private final static String TAG = App.class.getSimpleName();
    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        ImModule.init(sContext);
    }

    public Context getContext() {
        return sContext;
    }
}
