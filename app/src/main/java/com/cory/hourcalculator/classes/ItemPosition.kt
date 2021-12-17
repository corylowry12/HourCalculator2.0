package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ItemPosition(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setPosition(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("position", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadPosition(): Int {
        val state = sharedPreferences.getInt("position", -1)
        return (state)
    }
}