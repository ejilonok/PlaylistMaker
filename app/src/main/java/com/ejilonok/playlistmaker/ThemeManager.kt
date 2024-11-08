package com.ejilonok.playlistmaker

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager {
    companion object {
        private fun setLightTheme() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        private fun setDarkTheme() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        fun setDarkTheme(isDark : Boolean) {
            if (isDark)
                setDarkTheme()
            else
                setLightTheme()
        }

        fun getSavedThemeState(activity: Activity) : Boolean {
            val sharedPreferences = activity.getSharedPreferences("com.ejilonok.playlistmaker.ThemeSettings", Context.MODE_PRIVATE)
            val systemTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            return sharedPreferences.getBoolean("isDarkTheme", systemTheme)
        }

        fun saveThemeState(activity: Activity, isDark: Boolean) {
            val sharedPreferences = activity.getSharedPreferences("com.ejilonok.playlistmaker.ThemeSettings", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isDarkTheme", isDark)
            editor.apply()
        }
    }
}