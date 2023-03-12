package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class GenerateARandomColorData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setGenerateARandomColorOnAppLaunch(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("generateARandomColor", state)
        editor.apply()
    }

    fun loadGenerateARandomColorOnAppLaunch(): Boolean {
        val state = sharedPreferences.getBoolean("generateARandomColor", false)
        return (state)
    }
}