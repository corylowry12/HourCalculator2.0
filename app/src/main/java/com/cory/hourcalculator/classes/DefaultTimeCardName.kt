package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DefaultTimeCardName(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setDefaultName(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("defaultName", state)
        editor.apply()
    }

    fun loadDefaultName(): String {
        val state = sharedPreferences.getString( "defaultName", "")
        return (state!!)
    }
}