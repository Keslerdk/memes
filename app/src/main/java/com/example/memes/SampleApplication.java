package com.example.memes;

import android.app.Application;
import android.content.Intent;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VK.addTokenExpiredHandler(tockenTracker);
    }
    VKTokenExpiredHandler tockenTracker = new VKTokenExpiredHandler() {
        @Override
        public void onTokenExpired() {
            Intent intent = new Intent(SampleApplication.this, Profile.class);
            startActivity(intent);
        }
    };
}
