//package com.example.memes;
//
//import android.app.Application;
//
//import com.vk.api.sdk.VK;
//import com.vk.api.sdk.VKTokenExpiredHandler;
//import com.vk.api.sdk.auth.VKAccessToken;
//import com.vk.api.sdk.auth.VKScope;
//
//
//public class VkAuth extends android.app.Application {
//    VKTokenExpiredHandler tockenTracker = new VKTokenExpiredHandler() {
//        @Override
//        public void onTokenExpired() {
//
//        }
//    };
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        VK.initialize(this);
//    }
//
//}
