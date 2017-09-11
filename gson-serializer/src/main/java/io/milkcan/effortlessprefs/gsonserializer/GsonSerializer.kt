@file:JvmName("GsonSerializer")

package io.milkcan.effortlessprefs.gsonserializer

import android.content.SharedPreferences
import com.google.gson.Gson
import io.milkcan.effortlessprefs.library.PrefSerializer

/**
 * @author Eric Bachhuber
 * @version 1.1.0
 * @since 1.1.0
 */
class GsonSerializer(val gson: Gson) : PrefSerializer {

    lateinit var prefs: SharedPreferences

    override fun setSharedPreferenceInstance(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
    }

    override fun putObject(key: String, value: Any) {
        val json = gson.toJson(value)

        prefs.edit().putString(key, json).apply()
    }

    override fun <T : Any> getObject(key: String, defaultValue: T): T {
        val json = prefs.getString(key, "")

        return if (json.isNullOrBlank()) {
            defaultValue
        } else {
            gson.fromJson(json, defaultValue::class.java)
        }
    }

    override fun <T : Any> getObject(key: String, clazz: Class<T>): T? {
        val json = prefs.getString(key, "")

        return if (json.isNullOrBlank()) {
            null
        } else {
            gson.fromJson(json, clazz)
        }
    }

}
