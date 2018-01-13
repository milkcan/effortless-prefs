package io.milkcan.effortlessprefs.library

import android.content.SharedPreferences

/**
 * @author Eric Bachhuber
 * @version 1.1.0
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
    fun putObject(key: String, value: Any)

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Deserialized representation of the object at [key].
     */
    fun <T : Any> getObject(key: String, defaultValue: T): T

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param clazz Class that the preference will be deserialized as.
     * @return Deserialized representation of the object at [key].
     */
    fun <T : Any> getObject(key: String, clazz: Class<T>): T?

}
