package io.milkcan.effortlessprefs.sample;

import android.app.Application;
import android.content.ContextWrapper;

import io.milkcan.effortlessprefs.library.Prefs;

public class PrefsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

}