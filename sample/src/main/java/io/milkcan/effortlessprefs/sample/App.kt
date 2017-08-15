package io.milkcan.effortlessprefs.sample

import android.app.Application
import android.content.ContextWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.milkcan.effortlessprefs.library.GsonSerializer

import io.milkcan.effortlessprefs.library.Prefs

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val gson: Gson = GsonBuilder()
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .create()

        // Initialize Effortless Prefs
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .setPrefSerializer(GsonSerializer(gson))
                .build()
    }

}