package io.milkcan.effortlessprefs.sample

import android.app.Application
import android.content.ContextWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import io.milkcan.effortlessprefs.gsonserializer.GsonSerializer
import io.milkcan.effortlessprefs.moshiserializer.MoshiSerializer
import io.milkcan.effortlessprefs.library.Prefs

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val gson: Gson = GsonBuilder()
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .create()

        val moshi: Moshi = Moshi.Builder().build()

        val gsonSerializer = GsonSerializer(gson)
        val moshiSerializer = MoshiSerializer(moshi)

        // Initialize Effortless Prefs
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .setPrefSerializer(gsonSerializer)
                .build()
    }

}