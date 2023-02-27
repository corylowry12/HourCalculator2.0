package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DialogData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setOnboardingComplete(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Onboarding", state)
        editor.apply()
    }

    fun loadOnboardingComplete(): Boolean {
        val state = sharedPreferences.getBoolean("Onboarding", false)
        return (state)
    }

    fun setDateDialogState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DateDialog", state)
        editor.apply()
    }

    fun loadDateDialogState(): Boolean {
        val state = sharedPreferences.getBoolean("DateDialog", false)
        return (state)
    }
}