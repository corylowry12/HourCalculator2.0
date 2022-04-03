package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import com.cory.hourcalculator.R

class AccentColor(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)
    val followSystemVersion = FollowSystemVersion(context)

    fun setAccentState(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("Accent", state)
        editor.apply()
    }

    fun loadAccent(): Int {
        var state = sharedPreferences.getInt("Accent", 0)
        if (Build.VERSION.SDK_INT >= 31) {
            state = sharedPreferences.getInt("Accent", 4)
        }

        return (state)
    }

    fun alertTheme(): Int {

        when (this.loadAccent()) {
            0 -> {
                return R.style.AlertDialogStyle

            }
            1 -> {
                return R.style.AlertDialogStylePink
            }
            2 -> {
                return R.style.AlertDialogStyleOrange
            }
            3 -> {
                return R.style.AlertDialogStyleRed
            }
            4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    return R.style.AlertDialogStyleSystemAccent
                }
                else {
                    return R.style.AlertDialogStyleSystemAccentGoogle
                }

            }
            else -> return R.style.AlertDialogStyle
        }

    }

    fun followSystemTheme(context: Context): Int {

        when (FollowSystemThemeChoice(context).loadFollowSystemThemePreference()) {
            0 -> return R.style.Theme_AMOLED
            1 -> return R.style.Theme_DarkTheme
        }
        return R.style.Theme_AMOLED
    }

    fun followSystemThemeSplashScreen(context: Context): Int {

        when (FollowSystemThemeChoice(context).loadFollowSystemThemePreference()) {
            0 -> return R.style.SplashBlackTheme
            1 -> return R.style.SplashDarkTheme
        }
        return R.style.SplashBlackTheme
    }

    fun dateDialogTheme(context: Context): Int {

        when (DarkThemeData(context).loadDarkModeState()) {
            0 -> {
                if (this.loadAccent() == 4 && FollowSystemVersion(context).loadSystemColor()) {
                    return R.style.datePickerGoogleLight
                }
                return R.style.datePickerLight
            }
            1 -> {
                if (this.loadAccent() == 4 && FollowSystemVersion(context).loadSystemColor()) {
                    return R.style.datePickerGoogleDark
                }
                return R.style.datePickerDark
            }
            2 -> {
                if (this.loadAccent() == 4 && FollowSystemVersion(context).loadSystemColor()) {
                    return R.style.datePickerGoogleDark
                }
                return R.style.datePickerDark
            }
            3 -> {
                when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        if (this.loadAccent() == 4 && FollowSystemVersion(context).loadSystemColor()) {
                            return R.style.datePickerGoogleLight
                        }
                        return R.style.datePickerLight
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        if (this.loadAccent() == 4 && FollowSystemVersion(context).loadSystemColor()) {
                            return R.style.datePickerGoogleDark
                        }
                        return R.style.datePickerDark
                    }
                }
            }

        }
        return R.style.datePickerLight
    }

    fun snackbarActionTextColor(): Int {

        when (this.loadAccent()) {
            0 -> {
                return R.color.colorPrimary

            }
            1 -> {
                return R.color.pinkAccent
            }
            2 -> {
                return R.color.orangeAccent
            }
            3 -> {
                return R.color.redAccent
            }
            4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    return R.color.systemAccent
                }
                else {
                    return R.color.systemAccentGoogleDark_light
                }
            }
        }
        return R.color.colorPrimary
    }
}