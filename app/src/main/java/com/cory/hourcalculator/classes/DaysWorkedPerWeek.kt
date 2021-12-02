package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DaysWorkedPerWeek(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the wage amount
    fun setDaysWorked(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("Days_Worked", state)
        editor.apply()
    }

    // this will load wage amount
    fun loadDaysWorked(): Int {
        val state = sharedPreferences.getInt("Days_Worked", 7)
        return (state)
    }
}