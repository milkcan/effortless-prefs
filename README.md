Effortless Prefs
===

A lightweight wrapper for Android's SharedPreferences -- written in Kotlin.

This library is a fork of EasyPrefs by Pixplicity. For the original Java library, [click here.](https://github.com/Pixplicity/EasyPrefs)

With this library you can initialize a global instance of SharedPreferences inside your Application class.

```Kotlin
class PrefsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Effortless Prefs
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()
    }

}
```

# Usage

After initialization, you can use simple one-line methods to save values to SharedPreferences anywhere in your app, such as:

- `Prefs.putString(key, string)`
- `Prefs.putLong(key, long)`
- `Prefs.putBoolean(key, boolean)` 

Retrieving data from SharedPreferences can be as simple as:

    String data = Prefs.getString(key, default value)

If SharedPreferences contains the key, the string will be obtained, otherwise the method returns the default string provided. No need for those pesky `contains()` or `data != null` checks!

For some examples, see the sample App.

## Bonus feature: ordered sets

The default implementation of `getStringSet` on Android **does not preserve the order of the strings in the set**. For this purpose, EasyPrefs adds the methods:

    void Prefs.putOrderedStringSet(String key, Set<String> value);

and

    Set<String> Prefs.getOrderedStringSet(String key, Set<String> defaultValue);

which internally use Java's LinkedHashSet to retain a predictable iteration order. These methods have the added benefit of adding the missing functionality of storing sets to pre-Honeycomb devices.

# Coming Soon
RxJava support, JSON helpers, and more!

# Download
Grab the latest dependency through Gradle:
```Groovy
dependencies {
    implementation "io.milkcan:effortless-prefs:1.0.0"
}
```

For older versions, [see Bintray](https://bintray.com/bachhuberdesign/maven/effortless-prefs).

## License

This project is licensed under Apache 2.0 -- [see the full license here.](https://github.com/milkcan/effortless-prefs/blob/master/license.txt)