package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ChosenAppIconData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setChosenAppIcon(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("chosenAppIcon", state)
        editor.apply()
    }

    fun loadChosenAppIcon(): String? {
        val state = sharedPreferences.getString("chosenAppIcon", "teal")
        return (state)
    }
}