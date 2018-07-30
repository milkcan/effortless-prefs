@file:JvmName("GsonSerializer")

package io.milkcan.effortlessprefs.gsonserializer

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.milkcan.effortlessprefs.library.PrefSerializer

/**
 * @author Eric Bachhuber
 * @version 2.0.0
 * @since 1.1.0
 */
class GsonSerializer(private val gson: Gson) : PrefSerializer {

    companion object {
        @JvmStatic val TAG: String = GsonSerializer::class.java.simpleName
    }

    lateinit var prefs: SharedPreferences

    override fun setSharedPreferenceInstance(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
    }

    /**
     * Stores an Object using Gson.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     */
    override fun putObject(key: String, value: Any) {
        val json = gson.toJson(value)

        prefs.edit().putString(key, json).apply()
    }

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Deserialized representation of the object at [key], or [defaultValue] if unavailable
     * or a [JsonSyntaxException] is thrown while deserializing.
     */
    override fun <T : Any> getObject(key: String, defaultValue: T): T {
        val json = prefs.getString(key, "")

        return if (json.isNullOrBlank()) {
            defaultValue
        } else try {
            gson.fromJson(json, defaultValue::class.java)
        } catch (ex: JsonSyntaxException) {
            Log.d(TAG, "Error deserializing object, returning default value. ${ex.message}", ex)
            defaultValue
        }
    }

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param clazz Class that the preference will be deserialized as.
     * @return Deserialized representation of the object at [key], or null if unavailable or a
     * [JsonSyntaxException] is thrown while deserializing.
     */
    override fun <T : Any> getObject(key: String, clazz: Class<T>): T? {
        val json = prefs.getString(key, "")

        return try {
            gson.fromJson(json, clazz)
        } catch (ex: JsonSyntaxException) {
            Log.d(TAG, "Error deserializing object, returning null. ${ex.message}", ex)
            null
        }
    }

}
