package com.mike.firebaseapp;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Application class to instantiate Firebase
 *
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // Initialize Firebase
        Firebase.setAndroidContext(this);
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
