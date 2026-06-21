package com.example.buurterij.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Persists the user's chosen main and optional secondary display language for plant names.
 * Backed by SharedPreferences since the app has no DataStore dependency.
 */
class LanguagePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var mainLanguage: Language
        get() = prefs.getString(KEY_MAIN, null)?.let { runCatching { Language.valueOf(it) }.getOrNull() }
            ?: Language.DUTCH
        set(value) = prefs.edit().putString(KEY_MAIN, value.name).apply()

    var secondaryLanguage: Language?
        get() = prefs.getString(KEY_SECONDARY, null)?.let { runCatching { Language.valueOf(it) }.getOrNull() }
        set(value) = prefs.edit().putString(KEY_SECONDARY, value?.name).apply()

    companion object {
        private const val PREFS_NAME = "language_preferences"
        private const val KEY_MAIN = "main_language"
        private const val KEY_SECONDARY = "secondary_language"
    }
}
