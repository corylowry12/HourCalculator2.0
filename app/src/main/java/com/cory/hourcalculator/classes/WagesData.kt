package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class WagesData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setWageAmount(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("Wages", state!!)
        editor.apply()
    }

    fun loadWageAmount(): String? {
        val state = sharedPreferences.getString("Wages", "7.25")
        return (state)
    }
}