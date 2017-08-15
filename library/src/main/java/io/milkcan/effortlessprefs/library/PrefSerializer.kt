package io.milkcan.effortlessprefs.library

import android.content.SharedPreferences

/**
 * @author Eric Bachhuber
 * @version 1.1.0
 * @since 1.1.0
 */
interface PrefSerializer {

    /**
     * This should only be called internally by Prefs and not by any client code.
     *
     * @param sharedPreferences
     */
    fun setSharedPreferenceInstance(sharedPreferences: SharedPreferences)

    /**
     * @param key
     * @param value
     */
    fun putObject(key: String, value: Any)

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    fun <T : Any> getObject(key: String, defaultValue: T): T

    /**
     * @param key
     * @return
     */
    fun <T : Any> getObject(key: String, clazz: Class<T>): T?

}
