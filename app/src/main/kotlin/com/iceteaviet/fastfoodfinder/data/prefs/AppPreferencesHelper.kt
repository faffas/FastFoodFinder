package com.iceteaviet.fastfoodfinder.data.prefs

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Created by tom on 7/24/18.
 *
 *
 * Entry point contains methods that help interacting with application's SharedPreferences
 */

class AppPreferencesHelper(context: Context) : PreferencesHelper {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean?) {
        sharedPreferences.edit()
                .putBoolean(key, value!!)
                .apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean? {
        return sharedPreferences.getBoolean(key, defaultValue!!)
    }

    override fun setStringSet(key: String, set: MutableSet<String>) {
        sharedPreferences.edit()
                .putStringSet(key, set)
                .apply()
    }

    override fun getStringSet(key: String, defaultValue: MutableSet<String>): MutableSet<String> {
        return sharedPreferences.getStringSet(key, defaultValue)
    }

    override fun getAppLaunchFirstTime(): Boolean? {
        return sharedPreferences.getBoolean(KEY_APP_LAUNCH_FIRST_TIME, true)
    }

    override fun setAppLaunchFirstTime(isFirstTime: Boolean?) {
        sharedPreferences.edit()
                .putBoolean(KEY_APP_LAUNCH_FIRST_TIME, isFirstTime!!)
                .apply()
    }

    override fun getNumberOfStores(): Int {
        return sharedPreferences.getInt(KEY_NUMBER_OF_STORES, 0)
    }

    override fun setNumberOfStores(numberOfStores: Int) {
        sharedPreferences.edit()
                .putInt(KEY_NUMBER_OF_STORES, numberOfStores)
                .apply()
    }

    override fun getCurrentUserUid(): String {
        return sharedPreferences.getString(KEY_CURRENT_USER_UID, "")
    }

    override fun setCurrentUserUid(uid: String) {
        sharedPreferences.edit()
                .putString(KEY_CURRENT_USER_UID, uid)
                .apply()
    }

    override fun getSearchHistories(): MutableSet<String> {
        return sharedPreferences.getStringSet(KEY_SEARCH_HISTORIES, TreeSet())
    }

    override fun setSearchHistories(set: MutableSet<String>) {
        setStringSet(KEY_SEARCH_HISTORIES, set)
    }

    companion object {
        private const val PREFS_NAME = "english_now_android"
        private const val KEY_APP_LAUNCH_FIRST_TIME = "app_launch_first_time"
        private const val KEY_NUMBER_OF_STORES = "number_of_stores"
        private const val KEY_CURRENT_USER_UID = "current_user_uid"
        private const val KEY_SEARCH_HISTORIES = "search_histories"
    }
}
