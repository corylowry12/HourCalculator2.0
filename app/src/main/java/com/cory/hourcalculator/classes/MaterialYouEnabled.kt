package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class MaterialYouEnabled(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMaterialYouState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("materialYou", state)
        editor.apply()
    }

    fun loadMaterialYou(): Boolean {
        var state = sharedPreferences.getBoolean("materialYou", false)
        if (Build.VERSION.SDK_INT >= 31) {
            state = sharedPreferences.getBoolean("materialYou", true)
        }

        return (state)
    }
}