package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ChosenAppIconData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setChosenAppIcon(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("chosenAppIcon", state!!)
        editor.apply()
    }

    fun loadChosenAppIcon(): Int? {
        val state = sharedPreferences.getInt("chosenAppIcon", 0)
        return (state)
    }
}