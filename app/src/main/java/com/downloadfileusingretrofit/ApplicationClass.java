package com.downloadfileusingretrofit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by surya on 20/4/18.
 */

public class ApplicationClass extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mmm", "onCreate Called");
        context = getApplicationContext();
        checkDatabase();


    }

    private void checkDatabase() {

        SharedPreferences prefs = getSharedPreferences(context.getResources().getString(R.string.firsttimeprefs), MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(context.getResources().getString(R.string.isfirsttime), true);
        if (firstRun) {
            Log.d("mmmm", "Application Installed at first time");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(context.getResources().getString(R.string.isfirsttime), false);
            editor.apply();
            MainActivity.downloadService();
        } else {
            Log.d("mmm", "Application Installed already");

        }
    }

}
