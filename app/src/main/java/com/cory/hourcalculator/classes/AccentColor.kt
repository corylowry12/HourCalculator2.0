package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.cory.hourcalculator.R

class AccentColor(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun alertTheme(): Int {
        return R.style.AlertDialogStyle
    }

    fun dateDialogTheme(context: Context): Int {
        val darkThemeData = DarkThemeData(context)
        when (darkThemeData.loadDarkModeState()) {
             1 -> {
                return R.style.datePickerNew_dark
            }
            0 -> {
                return R.style.datePickerNew
            }
            2 -> {
                return R.style.datePickerNew_dark
            }
            3 -> {
                when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return R.style.datePickerNew
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return R.style.datePickerNew_dark
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return R.style.datePickerNew_dark
                    }
                }
            }
        }
        return R.style.datePickerNew
    }
}