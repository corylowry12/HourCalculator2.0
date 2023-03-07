package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ChosenAppIconData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setChosenAppIcon(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("chosenIcon", state)
        editor.apply()
    }

    fun loadChosenAppIcon(): String? {
        val state = sharedPreferences.getString("chosenIcon", "auto")
        return (state)
    }
}