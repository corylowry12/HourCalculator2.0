package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class TwoColumnTabletLayoutData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setTwoColumnLayout(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("TwoColumnTabletLayout", state!!)
        editor.apply()
    }

    fun loadTwoColumnLayout(): Boolean {
        val state = sharedPreferences.getBoolean("TwoColumnTabletLayout", true)
        return (state)
    }
}