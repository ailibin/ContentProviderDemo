package com.aiitec.contentproviderdemo;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ailibin
 * @description Application
 * @time 2019/03/21
 */
public class App extends Application {

    public static Context context;
    public static App app;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public static ExecutorService cachedThreadPool;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        App.app = this;
        cachedThreadPool = Executors.newCachedThreadPool();
    }
}
