package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class MaterialYouOptionData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMaterialOption(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("material_option", state)
        editor.apply()
    }

    fun loadMaterialOption(): Int {
        val state = sharedPreferences.getInt("material_option", 2)
        return (state)
    }
}