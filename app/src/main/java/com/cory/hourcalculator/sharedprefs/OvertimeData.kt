package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class OvertimeData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setOverTimeAmount(state: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat("overTimeAmount", state)
        editor.apply()
    }

    fun loadOverTimeAmount(): Float {
        val state = sharedPreferences.getFloat("overTimeAmount", 1.5f)
        return (state)
    }
}