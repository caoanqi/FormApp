package com.android.formapp;

import android.app.Application;

public class MyApp extends Application {
    public static MyApp instnce;

    public static MyApp getInstance() {
        if ( instnce == null ) {
            synchronized ( MyApp.class ) {
                if ( instnce == null ) {
                    instnce = new MyApp();
                }
            }
        }
        return instnce;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instnce = this;
    }
}
