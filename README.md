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

If you prefer Maven or downloading the AAR manually; please check [the Bintray project page](https://bintray.com/pixplicity/android/EasyPrefs).

# License

```
Copyright 2014 Pixplicity (http://pixplicity.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
