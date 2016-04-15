package com.sample.lightsoo.kakaotalk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by LG on 2016-04-15.
 */
public class MyApplication extends Application{

    private Activity currentTopActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSDK.init(new KakaoAdapter() {
            @Override
            public IApplicationConfig getApplicationConfig() {
                return new IApplicationConfig() {
                    @Override
                    public Activity getTopActivity() {
                        return currentTopActivity;
                    }

                    @Override
                    public Context getApplicationContext() {
                        return MyApplication.this;
                    }
                };
            }
        });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentTopActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (currentTopActivity != null && currentTopActivity == activity) {
                    currentTopActivity = null;
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
