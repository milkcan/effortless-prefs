package io.milkcan.effortlessprefs.library

import android.content.SharedPreferences

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
internal class GsonSerializer : PrefSerializer {

    lateinit var prefs: SharedPreferences

    override fun setPreferences(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
    }

    override fun putObject(key: String, value: Any) {
        TODO("Not yet implemented.")
    }

    override fun <T> getObject(key: String, defaultValue: T): T {
        TODO("Not yet implemented.")
    }

    override fun <T> getObject(key: String): T? {
        TODO("Not yet implemented.")
    }

}
