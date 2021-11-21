package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class CalculationType(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setCalculationState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Calculation", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadCalculationState(): Boolean {
        val state = sharedPreferences.getBoolean("Calculation", true)
        return (state)
    }
}