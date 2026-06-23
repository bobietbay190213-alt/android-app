package com.modernapp.app.utils

object Constants {
    // API
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // Database
    const val DATABASE_NAME = "modern_app_database"
    const val DATABASE_VERSION = 1

    // DataStore
    const val DATASTORE_NAME = "modern_app_prefs"
    const val KEY_THEME_MODE = "theme_mode"
    const val KEY_LANGUAGE = "language"

    // Theme
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"

    // Pagination
    const val PAGE_SIZE = 20
    const val INITIAL_PAGE = 1

    // Animation durations (ms)
    const val SPLASH_DELAY = 2000L
    const val ANIMATION_SHORT = 300
    const val ANIMATION_MEDIUM = 500
    const val ANIMATION_LONG = 800
}
