package io.milkcan.effortlessprefs.sample

import android.app.Application
import android.content.ContextWrapper

import io.milkcan.effortlessprefs.library.Prefs

class PrefsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Effortless Prefs
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()
    }

}