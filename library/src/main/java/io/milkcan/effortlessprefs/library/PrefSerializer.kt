package io.milkcan.effortlessprefs.library

import android.content.SharedPreferences

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface PrefSerializer {

    fun setPreferences(sharedPreferences: SharedPreferences)

    fun putObject(key: String, value: Any)

    fun <T> getObject(key: String, defaultValue: T): T

    fun <T> getObject(key: String): T?

}
