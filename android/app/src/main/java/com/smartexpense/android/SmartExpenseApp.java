package com.smartexpense.android;

import android.app.Application;

import com.smartexpense.android.data.remote.RetrofitClient;

public class SmartExpenseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(this);
    }
}
