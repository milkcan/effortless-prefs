@file:Suppress("unused", "MemberVisibilityCanPrivate")

package io.milkcan.effortlessprefs.library

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.text.TextUtils
import java.util.*

/**
 * @author Eric Bachhuber
 * @version 1.1.0
 * @since 1.0.0
 */
object Prefs {

    private const val DEFAULT_SUFFIX = "_preferences"
    private const val LENGTH = "#LENGTH"
    private var mPrefs: SharedPreferences? = null
    private var prefSerializer: PrefSerializer? = null

    private fun initPrefs(context: Context, prefsName: String, mode: Int, prefSerializer: PrefSerializer?) {
        mPrefs = context.getSharedPreferences(prefsName, mode)

        prefSerializer?.setSharedPreferenceInstance(mPrefs!!)
        this.prefSerializer = prefSerializer
    }

    /**
     * Returns the underlying SharedPreference instance
     *
     * @return an instance of the SharedPreference
     * @throws RuntimeException if SharedPreference instance has not been instantiated yet.
     */
    val preferences: SharedPreferences
        get() {
            if (mPrefs != null) {
                return mPrefs as SharedPreferences
            }
            throw RuntimeException(
                    "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate.")
        }

    /**
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @see android.content.SharedPreferences.getAll
     */
    val all: Map<String, *>
        get() = preferences.all

    /**
     * Retrieves a stored int value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not
     * an int.
     * @see android.content.SharedPreferences.getInt
     */
    fun getInt(key: String, defValue: Int): Int = preferences.getInt(key, defValue)

    /**
     * Retrieves a stored boolean value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a boolean.
     * @see android.content.SharedPreferences.getBoolean
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

    /**
     * Retrieves a stored long value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see android.content.SharedPreferences.getLong
     */
    fun getLong(key: String, defValue: Long): Long = preferences.getLong(key, defValue)

    /**
     * Returns the double that has been saved as a long raw bits value in the long preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue the double Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see android.content.SharedPreferences.getLong
     */
    fun getDouble(key: String, defValue: Double): Double =
            java.lang.Double.longBitsToDouble(preferences.getLong(key, java.lang.Double.doubleToLongBits(defValue)))

    /**
     * Retrieves a stored float value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a float.
     * @see android.content.SharedPreferences.getFloat
     */
    fun getFloat(key: String, defValue: Float): Float = preferences.getFloat(key, defValue)

    /**
     * Retrieves a stored String value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a String.
     * @see android.content.SharedPreferences.getString
     */
    fun getString(key: String, defValue: String): String? = preferences.getString(key, defValue)

    /**
     * Retrieves a Set of Strings as stored by [putStringSet].
     * **Note that the native implementation of [SharedPreferences.getStringSet] does not reliably preserve the order of the Strings in the Set.**
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set.
     * @see android.content.SharedPreferences.getStringSet
     * @see getOrderedStringSet
     */
    fun getStringSet(key: String, defValue: Set<String>): Set<String> =
            preferences.getStringSet(key, defValue)

    /**
     * Retrieves a Set of Strings as stored by [putOrderedStringSet],
     * preserving the original order. Note that this implementation is heavier than the native
     * [getStringSet] function (which does not guarantee to preserve order).
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set of
     * Strings.
     * @see getStringSet
     */
    fun getOrderedStringSet(key: String, defValue: Set<String>): Set<String> {
        val prefs = preferences

        if (prefs.contains(key + LENGTH)) {
            val set = LinkedHashSet<String>()

            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                (0 until stringSetLength).mapTo(set) { prefs.getString("$key[$it]", null) }
            }

            return set
        }
        return defValue
    }

    /**
     * @param key
     * @param value
     */
    fun putObject(key: String, value: Any) {
        prefSerializer!!.putObject(key, value)
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    fun <T : Any> getObject(key: String, defaultValue: T): T {
        return prefSerializer!!.getObject(key, defaultValue)
    }

    /**
     * @param key
     * @return
     */
    fun <T : Any> getObject(key: String, clazz: Class<T>): T? {
        return prefSerializer!!.getObject(key, clazz)
    }

    /**
     * Stores a long value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putLong
     */
    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * Stores an integer value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putInt
     */
    fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Stores a double value as a long raw bits value.
     *
     * @param key   The name of the preference to modify.
     * @param value The double value to be save in the preferences.
     * @see android.content.SharedPreferences.Editor.putLong
     */
    fun putDouble(key: String, value: Double) {
        val editor = preferences.edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        editor.apply()
    }

    /**
     * Stores a float value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putFloat
     */
    fun putFloat(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    /**
     * Stores a boolean value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putBoolean
     */
    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Stores a String value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putString
     */
    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Stores a Set of Strings.
     *
     * **Note that the native implementation of [Editor.putStringSet] does not reliably preserve the order of the Strings in the Set.**
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see android.content.SharedPreferences.Editor.putStringSet
     * @see putOrderedStringSet
     */
    fun putStringSet(key: String, value: Set<String>) {
        val editor = preferences.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    /**
     * Stores a Set of Strings, preserving the order.
     * Note that this function is heavier that the native implementation [putStringSet]
     * (which does not reliably preserve the order of the Set). To preserve the order of the
     * items in the Set, the Set implementation must be one that as an iterator with predictable
     * order, such as [LinkedHashSet].
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see putStringSet
     * @see getOrderedStringSet
     */
    fun putOrderedStringSet(key: String, value: Set<String>) {
        val editor = preferences.edit()
        var stringSetLength = 0
        if (mPrefs!!.contains(key + LENGTH)) {
            // First read what the set was
            stringSetLength = mPrefs!!.getInt(key + LENGTH, -1)
        }

        editor.putInt(key + LENGTH, value.size)
        var i = 0
        value.forEach { v ->
            editor.putString("$key[$i]", v)
            i++
        }

        while (i < stringSetLength) {
            // Remove any remaining set
            editor.remove("$key[$i]")
            i++
        }

        editor.apply()
    }

    /**
     * Removes a preference value.
     *
     * @param key The name of the preference to remove.
     * @see android.content.SharedPreferences.Editor.remove
     */
    fun remove(key: String) {
        val prefs = preferences
        val editor = prefs.edit()

        // TODO: remove
        if (prefs.contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH)
                for (i in 0 until stringSetLength) {
                    editor.remove("$key[$i]")
                }
            }
        }

        editor.remove(key)
        editor.apply()
    }

    /**
     * Checks if a value is stored for the given key.
     *
     * @param key The name of the preference to check.
     * @return `true` if the storage contains this key value, `false` otherwise.
     * @see android.content.SharedPreferences.contains
     */
    operator fun contains(key: String): Boolean = preferences.contains(key)

    /**
     * Removed all the stored keys and values.
     *
     * @return the [Editor] for chaining. The changes have already been committed/applied
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