package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class CalculationType(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setCalculationState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Calculation", state)
        editor.apply()
    }

    fun loadCalculationState(): Boolean {
        val state = sharedPreferences.getBoolean("Calculation", true)
        return (state)
    }
}