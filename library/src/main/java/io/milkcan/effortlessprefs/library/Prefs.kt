@file:Suppress("unused", "MemberVisibilityCanPrivate")

package io.milkcan.effortlessprefs.library

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.text.TextUtils

/**
 * @author Eric Bachhuber
 * @version 2.0.0
 * @since 1.0.0
 */
object Prefs {

    var prefSerializer: PrefSerializer? = null
        private set

    private const val DEFAULT_SUFFIX = "_preferences"
    private const val LENGTH = "#LENGTH"
    private var sharedPreferences: SharedPreferences? = null

    private fun initPrefs(context: Context, prefsName: String, mode: Int, prefSerializer: PrefSerializer?) {
        sharedPreferences = context.getSharedPreferences(prefsName, mode)

        prefSerializer?.setSharedPreferenceInstance(sharedPreferences!!)
        this.prefSerializer = prefSerializer
    }

    /**
     * Returns the underlying SharedPreferences instance
     *
     * @return an instance of the SharedPreferences
     * @throws IllegalStateException if SharedPreferences instance has not been instantiated yet.
     */
    private val preferences: SharedPreferences
        get() {
            if (sharedPreferences != null) {
                return sharedPreferences as SharedPreferences
            }
            throw IllegalStateException(
                    "Prefs class not correctly instantiated. Please call Builder.setContext().build() " +
                            "in your Application class onCreate().")
        }

    /**
     * @return Key/value map of all stored preferences.
     * @see android.content.SharedPreferences.getAll
     */
    fun getAll(): Map<String, *> = preferences.all

    /**
     * Retrieves a stored int value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not
     * an int.
     * @see android.content.SharedPreferences.getInt
     */
    fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    /**
     * Retrieves a stored boolean value.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a boolean.
     * @see android.content.SharedPreferences.getBoolean
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean = preferences.getBoolean(key, defaultValue)

    /**
     * Retrieves a stored long value.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see android.content.SharedPreferences.getLong
     */
    fun getLong(key: String, defaultValue: Long): Long = preferences.getLong(key, defaultValue)

    /**
     * Returns the double that has been saved as a long raw bits value in the long preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue the double Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see android.content.SharedPreferences.getLong
     */
    fun getDouble(key: String, defaultValue: Double): Double =
            java.lang.Double.longBitsToDouble(preferences.getLong(key, java.lang.Double.doubleToLongBits(defaultValue)))

    /**
     * Retrieves a stored float value.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a float.
     * @see android.content.SharedPreferences.getFloat
     */
    fun getFloat(key: String, defaultValue: Float): Float = preferences.getFloat(key, defaultValue)

    /**
     * Retrieves a stored String value.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * @throws ClassCastException if there is a preference with this name that is not a String.
     * @see android.content.SharedPreferences.getString
     */
    fun getString(key: String, defaultValue: String): String = preferences.getString(key, defaultValue)

    /**
     * Retrieves a Set of Strings as stored by [putStringSet].
     * NOTE: The native implementation of [SharedPreferences.getStringSet] does not reliably preserve
     * the order of Strings in the Set.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defaultValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set.
     * @see android.content.SharedPreferences.getStringSet
     */
    fun getStringSet(key: String, defaultValue: Set<String>): Set<String> = preferences.getStringSet(key, defaultValue)

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Deserialized representation of the object at [key], or [defaultValue] if unavailable
     * or an exception is thrown while deserializing.
     * @see io.milkcan.effortlessprefs.library.PrefSerializer
     */
    inline fun <reified T> getObject(key: String, defaultValue: T): T {
        return if (prefSerializer != null) {
            prefSerializer!!.getObject(key, defaultValue, T::class.java)
        } else throw IllegalStateException("PrefSerializer not correctly instantiated. Please call " +
                "Builder.setPrefSerializer().build() in your Application class onCreate().")
    }

    /**
     * Retrieves a stored Object.
     *
     * @param key The name of the preference to retrieve.
     * @return Deserialized representation of the object at [key], or null if unavailable or an
     * exception is thrown while deserializing.
     * @see io.milkcan.effortlessprefs.library.PrefSerializer
     */
    inline fun <reified T> getObject(key: String): T? {
        return if (prefSerializer != null) {
            prefSerializer!!.getObject<T>(key, T::class.java)
        } else {
            throw IllegalStateException("PrefSerializer not properly instantiated. Please call " +
                    "Builder.setPrefSerializer().build() in your Application class onCreate().")
        }
    }

    /**
     * Stores an Object.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see io.milkcan.effortlessprefs.library.PrefSerializer
     */
    inline fun <reified T> putObject(key: String, value: T) {
        return if (prefSerializer != null) {
            prefSerializer!!.putObject(key, value, T::class.java)
        } else throw IllegalStateException("PrefSerializer not correctly instantiated. Please call " +
                "Builder.setPrefSerializer().build() in your Application class onCreate().")
    }

    /**
     * Stores a long value.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putLong
     */
    fun putLong(key: String, value: Long) = preferences.edit().putLong(key, value).apply()

    /**
     * Stores an integer value.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putInt
     */
    fun putInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()

    /**
     * Stores a double value as a long raw bits value.
     *
     * @param key The name of the preference to modify.
     * @param value The double value to be save in the preferences.
     * @see android.content.SharedPreferences.Editor.putLong
     */
    fun putDouble(key: String, value: Double) {
        preferences.edit()
                .putLong(key, java.lang.Double.doubleToRawLongBits(value))
                .apply()
    }

    /**
     * Stores a float value.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putFloat
     */
    fun putFloat(key: String, value: Float) = preferences.edit().putFloat(key, value).apply()

    /**
     * Stores a boolean value.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putBoolean
     */
    fun putBoolean(key: String, value: Boolean) = preferences.edit().putBoolean(key, value).apply()

    /**
     * Stores a String value.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putString
     */
    fun putString(key: String, value: String) = preferences.edit().putString(key, value).apply()

    /**
     * Stores a Set of Strings.
     *
     * **Note that the native implementation of [Editor.putStringSet] does not reliably preserve the order of the Strings in the Set.**
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putStringSet
     */
    fun putStringSet(key: String, value: Set<String>) = preferences.edit().putStringSet(key, value).apply()

    /**
     * Removes a preference value.
     *
     * @param key The name of the preference to remove.
     * @see android.content.SharedPreferences.Editor.remove
     */
    fun remove(key: String) = preferences.edit().remove(key).apply()

    /**
     * Checks if a value is stored for the given key.
     *
     * @param key The name of the preference to check.
     * @return True if the storage contains this key value.
     * @see android.content.SharedPreferences.contains
     */
    operator fun contains(key: String): Boolean = preferences.contains(key)

    /**
     * Removed all the stored keys and values.
     *
     * @return The [Editor] for chaining. The changes have already been committed/applied
     * through the execution of this function.
     * @see android.content.SharedPreferences.Editor.clear
     */
    fun clear(): Editor {
        val editor = preferences.edit().clear()
        editor.apply()

        return editor
    }

    /**
     * Returns the Editor of the underlying SharedPreferences instance.
     *
     * @return An Editor
     */
    @SuppressLint("CommitPrefEdits")
    fun edit(): Editor = preferences.edit()

    /**
     * Builder class for the [Prefs] instance. This should only be called once in your application's
     * [android.app.Application.onCreate] function.
     */
    class Builder {

        private var key: String? = null
        private var context: Context? = null
        private var useDefault = false
        private var prefSerializer: PrefSerializer? = null

        /**
         * Set the filename of the SharedPreference instance. Usually this is the application's
         * packagename.xml but it can be modified for migration purposes or customization.
         *
         * @param prefsName the filename used for the SharedPreference
         * @return the [Prefs.Builder] object.
         */
        fun setPrefsName(prefsName: String): Builder {
            this.key = prefsName
            return this
        }

        /**
         * Set the Context used to instantiate the SharedPreferences
         *
         * @param context the application context
         * @return the [Prefs.Builder] object.
         */
        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        /**
         * Set the default SharedPreference file name. Often the package name of the application is
         * used, but if the [android.preference.PreferenceActivity] or [ ] is used the system will append that with
         * _preference.
         *
         * @param defaultSharedPreference true if default SharedPreference name should used.
         * @return the [Prefs.Builder] object.
         */
        fun setUseDefaultSharedPreference(defaultSharedPreference: Boolean): Builder {
            useDefault = defaultSharedPreference
            return this
        }

        fun setPrefSerializer(prefSerializer: PrefSerializer): Builder {
            this.prefSerializer = prefSerializer
            return this
        }

        /**
         * Builds a [Prefs] instance to used in the application.
         *
         * @throws RuntimeException if Context has not been set.
         */
        fun build() {
            if (context == null) {
                throw RuntimeException("Please set context before building the Prefs instance.")
            }

            if (TextUtils.isEmpty(key)) {
                key = context!!.packageName
            }

            if (useDefault) {
                key += DEFAULT_SUFFIX
            }

            Prefs.initPrefs(context!!, key!!, ContextWrapper.MODE_PRIVATE, prefSerializer)
        }

    }

}