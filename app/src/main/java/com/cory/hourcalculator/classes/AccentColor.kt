package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.widget.Toast
import com.cory.hourcalculator.R

class AccentColor(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setAccentState(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("Accent", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadAccent(): Int {
        val state = sharedPreferences.getInt("Accent", 0)
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
                return R.style.AlertDialogStyleSystemAccent
            }
            else -> return R.style.AlertDialogStyle
        }

    }

    fun menuTheme(context: Context): Int {

        when (DarkThemeData(context).loadDarkModeState()) {
            0 -> {
                return R.style.PopupMenuLight

            }
            1 -> {
                return R.style.PopupMenuDark
            }
            2 -> {
                return R.style.PopupMenuDark
            }
            3 -> {
                when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> return R.style.PopupMenuLight
                    Configuration.UI_MODE_NIGHT_YES -> return R.style.PopupMenuDark
                }
            }
        }
        return R.style.PopupMenuLight
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
                return R.color.systemAccent
            }
        }
        return R.color.colorPrimary
    }
}