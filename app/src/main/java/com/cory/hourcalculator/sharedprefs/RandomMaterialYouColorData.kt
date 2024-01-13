package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class RandomMaterialYouColorData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setRandomState(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("randomMaterialYou", state)
        editor.apply()
    }

    fun loadRandomState(): Int {
        val state = sharedPreferences.getInt("randomMaterialYou", 1)
        return (state)
    }
}