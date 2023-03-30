package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class GenerateARandomColorMethodData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setGenerateARandomColorMethod(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("generateARandomColorMethod", state)
        editor.apply()
    }

    fun loadGenerateARandomColorMethod(): Int {
        val state = sharedPreferences.getInt("generateARandomColorMethod", 0)
        return (state)
    }
}