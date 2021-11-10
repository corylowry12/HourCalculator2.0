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

        val darkThemeData = DarkThemeData(context)

        if (state == 0) {
            return R.style.AlertDialogStyle

        } else if (state == 1) {
            return R.style.AlertDialogStylePink
        } else if (state == 2) {
            return R.style.AlertDialogStyleOrange
        } else if (state == 3) {
            return R.style.AlertDialogStyleRed
        } else if (state == 4) {
            return R.style.AlertDialogStyleSystemAccent
        }
        Toast.makeText(context.applicationContext, darkThemeData.loadDarkModeState().toString() , Toast.LENGTH_LONG).show()
        return R.style.AlertDialogStyle
    }
}