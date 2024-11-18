package com.t2f4.timebrew;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InitApplication extends Application {
    private static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}
                @Override
                public void onActivityStarted(@NonNull Activity activity) {}
                @Override
                public void onActivityResumed(@NonNull Activity activity) {
                    Log.d("activityTest", "onActivityResumed: " + activity.toString());
                    currentActivity = activity;
                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {
                    if(currentActivity == activity)
                        currentActivity = null; //Memory 누수 방지
                }
                @Override
                public void onActivityStopped(@NonNull Activity activity) {}
                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {}
            });
        }
    }

    public static Activity getCurrentActivity(){
        return currentActivity;
    }
}
