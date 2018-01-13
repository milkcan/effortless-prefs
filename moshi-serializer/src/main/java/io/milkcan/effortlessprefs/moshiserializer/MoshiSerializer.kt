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

    /**
     * Stores an Object using Moshi.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     */
    override fun putObject(key: String, value: Any) {
        val adapter = moshi.adapter<Any>(value::class.java)
        val json = adapter.toJson(value)

        prefs.edit().putString(key, json).apply()
    }

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Deserialized representation of the object at [key], or [defaultValue] if unavailable
     * or an [IOException] is thrown while deserializing.
     */
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

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param clazz Class that the preference will be deserialized as.
     * @return Deserialized representation of the object at [key], or null if unavailable or an
     * [IOException] is thrown while deserializing.
     */
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
