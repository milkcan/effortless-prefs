@file:JvmName("GsonSerializer")

package io.milkcan.effortlessprefs.gsonserializer

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.milkcan.effortlessprefs.library.PrefSerializer

/**
 * @author Eric Bachhuber
 * @version 1.1.0
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

    override fun putObject(key: String, value: Any) {
        val json = gson.toJson(value)

        prefs.edit().putString(key, json).apply()
    }

    override fun <T : Any> getObject(key: String, defaultValue: T): T {
        val json = prefs.getString(key, "")

        return try {
            gson.fromJson(json, defaultValue::class.java)
        } catch (ex: JsonSyntaxException) {
            Log.d(TAG, "Error deserializing object, returning default value. ${ex.message}", ex)
            defaultValue
        }
    }

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
