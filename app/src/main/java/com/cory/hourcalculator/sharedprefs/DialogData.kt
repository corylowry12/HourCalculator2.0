package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class DialogData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setDateDialogState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DateDialog", state)
        editor.apply()
    }

    fun loadDateDialogState(): Boolean {
        val state = sharedPreferences.getBoolean("DateDialog", false)
        return (state)
    }
}