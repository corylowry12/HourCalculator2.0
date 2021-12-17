package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DialogData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setDialogState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dialog", state)
        editor.apply()
    }

    fun loadDialogState(): Boolean {
        val state = sharedPreferences.getBoolean("Dialog", false)
        return (state)
    }

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