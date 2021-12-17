package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class AppearanceScrollPosition(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setScroll(state: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat("scrollPosition", state)
        editor.apply()
    }

    fun loadScroll(): Float {
        val state = sharedPreferences.getFloat("scrollPosition", 0f)
        return (state)
    }
}