@file:JvmName("MoshiSerializer")

package io.milkcan.effortlessprefs.moshiserializer

import android.content.SharedPreferences
import android.util.Log
import com.squareup.moshi.Moshi
import io.milkcan.effortlessprefs.library.PrefSerializer
import java.io.IOException

/**
 * @author Eric Bachhuber
 * @version 1.1.0
 * @since 1.1.0
 */
class MoshiSerializer(private val moshi: Moshi) : PrefSerializer {

    companion object {
        @JvmStatic val TAG: String = MoshiSerializer::class.java.simpleName
    }

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

        return try {
            adapter.fromJson(json) as T
        } catch (ex: IOException) {
            Log.d(TAG, "Error deserializing object, returning default value. ${ex.message}", ex)
            defaultValue
        }
    }

    override fun <T : Any> getObject(key: String, clazz: Class<T>): T? {
        val adapter = moshi.adapter<T>(clazz)
        val json = prefs.getString(key, "")

        return try {
            adapter.fromJson(json) as T
        } catch (ex: IOException) {
            Log.d(TAG, "Error deserializing object, returning null. ${ex.message}", ex)
            null
        }
    }

}
