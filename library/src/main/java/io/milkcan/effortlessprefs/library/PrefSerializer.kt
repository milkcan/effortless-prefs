package io.milkcan.effortlessprefs.library

import android.content.SharedPreferences
import java.lang.reflect.Type

/**
 * @author Eric Bachhuber
 * @version 2.0.0
 * @since 1.1.0
 */
interface PrefSerializer {

    /**
     * NOTE: This should only be called internally by Prefs and not by any client code.
     */
    fun setSharedPreferenceInstance(sharedPreferences: SharedPreferences)

    /**
     * Stores an Object.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun <T> putObject(key: String, value: T, type: Type)

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @return Deserialized representation of the object at [key].
     */
    fun <T> getObject(key: String, type: Type): T?

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Deserialized representation of the object at [key].
     */
    fun <T> getObject(key: String, defaultValue: T, type: Type): T

}
