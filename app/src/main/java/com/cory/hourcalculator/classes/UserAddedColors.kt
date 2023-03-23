package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class UserAddedColors(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun addColor(state: MutableSet<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("userAddedColors", state)
        editor.apply()
    }

    fun loadColors(): MutableSet<String>? {
        val state = sharedPreferences.getStringSet("userAddedColors", emptySet())
        return (state)
    }

    fun clear() {
        sharedPreferences.edit().remove("userAddedColors").commit()
    }
}