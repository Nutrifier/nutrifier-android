package fi.nutrifier.utils

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

/**
 * Composition local providing access to the application context.
 *
 * This composition local allows accessing the application context from anywhere
 * within the Jetpack Compose hierarchy. It is used to pass the application context
 * down to composables that need it, such as those requiring access to shared preferences
 * or other application-wide resources.
 */
val LocalApplicationContext = compositionLocalOf<Context> {
    error("No Shared Prefs found")
}
