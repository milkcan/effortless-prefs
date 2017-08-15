package io.milkcan.effortlessprefs.moshiserializer

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import io.milkcan.effortlessprefs.library.PrefSerializer

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MoshiSerializer(val moshi: Moshi) : PrefSerializer {

    lateinit var prefs: SharedPreferences

    override fun setSharedPreferenceInstance(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
    }

    override fun putObject(key: String, value: Any) {
        val adapter = moshi.adapter<Any>(value::class.java)
        val json = adapter.toJson(value)

        prefs.edit().putString(key, json).apply()
    }

    override fun <T : Any> getObject(key: String, defaultValue: T): T {
        val adapter = moshi.adapter<T>(defaultValue::class.java)
        val json = prefs.getString(key, "")

        return if (json.isNullOrBlank()) {
            defaultValue
        } else {
            adapter.fromJson(json) as T
        }
    }

    override fun <T : Any> getObject(key: String): T? {
        val adapter = moshi.adapter<T>(T::class.java)
        val json = prefs.getString(key, "")

        return if (json.isNullOrBlank()) {
            null
        } else {
            adapter.fromJson(json) as T
        }
    }

}
