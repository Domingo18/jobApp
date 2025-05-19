package com.example.project_2_cmp431.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log import com.example.project_2_cmp431.util.TAG

/**  app shared preferences interface   This interface defines the shared preferences for the app. /
 *
 */
interface AppSharedPreferences {
    fun getSharedPreferences(): SharedPreferences }

/**  app shared preferences implementation   This class implements the app shared preferences interface. /
 *
 */
class AppPreferences(private val context: Context) : AppSharedPreferences
{ private val appPreferencesKey = "app_prefs"

    /**  Get the shared preferences.   @return the shared preferences /
     *
     */
    override fun getSharedPreferences(): SharedPreferences {
        Log.i(TAG, "getting shared preferences")
        return context.getSharedPreferences(appPreferencesKey, MODE_PRIVATE)
    }
}
