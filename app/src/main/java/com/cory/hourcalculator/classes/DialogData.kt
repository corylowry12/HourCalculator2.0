package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DialogData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setDialogState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dialog", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadDialogState(): Boolean {
        val state = sharedPreferences.getBoolean("Dialog", false)
        return (state)
    }
}