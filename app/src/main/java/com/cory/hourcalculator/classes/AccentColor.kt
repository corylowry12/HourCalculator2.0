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

    fun alertTheme(context: Context): Int {
        val state = sharedPreferences.getInt("Accent", 0)

        when (state) {
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
        val state = sharedPreferences.getInt("Accent", 0)

        when (state) {
            0 -> {
                return R.style.PopupMenuTeal

            }
            1 -> {
                return R.style.PopupMenuPink
            }
            2 -> {
                return R.style.PopupMenuOrange
            }
            3 -> {
                return R.style.PopupMenuRed
            }
            4 -> {
                return R.style.PopupMenuSystem
            }
            else -> return R.style.AlertDialogStyle
        }

    }
}